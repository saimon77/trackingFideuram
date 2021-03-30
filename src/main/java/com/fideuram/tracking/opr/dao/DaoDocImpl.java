package com.fideuram.tracking.opr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.fideuram.tracking.opr.dto.Documento;
import com.fideuram.tracking.opr.utility.Constant;

@Repository
public class DaoDocImpl extends DaoAbstract implements DaoDoc {
	
	public Logger logger = Logger.getLogger(DaoDocImpl.class);
	@Autowired
	@Qualifier("getProperties")
	private Properties prop;
	
//	String url = "jdbc:jtds:sqlserver://fvdsitfv01c.fidevita.bancafideuram.it:1433;databaseName=fide_prod";
//	String user = "puc001";
//	String password = "puc0010";

	public List<Documento> getDocumentiInviatPerPratica(String numPratica,String c_prodotto) throws Exception {

		Connection conn = null;
		PreparedStatement st = null;
		List<Documento> list = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PROD_URL),prop.getProperty(Constant.PROD_USER),prop.getProperty(Constant.PROD_PWD),prop.getProperty(Constant.PROD_CLASS_NAME));
			st = conn.prepareStatement("select pl.n_pratica,d.c_documento,d.descrizione from dbo.PraticaLiquidazione as pl\r\n"
					+ "	join dbo.DocumentoPraticaLiquidazione as dpl on dpl.c_tipo_liquidazione=pl.c_tipo_liquidazione\r\n"
					+ "	join dbo.Documento as d on d.c_documento=dpl.c_documento\r\n"
					+ "where pl.n_pratica=? and dpl.c_prodotto=?");
			st.setString(1, numPratica);
			st.setString(2, c_prodotto);

			boolean executeUpdate = st.execute();
			if (executeUpdate) {
				list=new ArrayList<Documento>();
				Documento doc=null;
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					doc=new Documento();
					doc.setC_documento(rs.getString(Documento.Campi.C_DOCUEMNTO));
					doc.setDescrizione(rs.getString(Documento.Campi.DESCRIZIONE));
					list.add(doc);
				}
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
			return null;
		} finally {
			try {
				if(st!=null)
				st.close();
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				logger.info(e.getMessage());
				throw new Exception("errore in accesso al DB");
			}
		}
		logger.info("trovati elementi: "+(list==null?"0":list.size()));
		return list;
	}
	
	
	public List<Documento> getDocumentiTotPerPratica(String numPratica) throws Exception {
		
		Connection conn = null;
		PreparedStatement st = null;
		List<Documento> list = null;
		try {
			conn = getConnection(prop.getProperty(Constant.PROD_URL),prop.getProperty(Constant.PROD_USER),prop.getProperty(Constant.PROD_PWD),prop.getProperty(Constant.PROD_CLASS_NAME));
			st = conn.prepareStatement("select dp.n_pratica,d.c_documento,d.descrizione from dbo.DocumentoPratica as dp \r\n"
					+ "	join dbo.Documento as d on d.c_documento=dp.c_documento\r\n"
					+ "where dp.n_pratica=?");
			st.setString(1, numPratica);
			
			boolean executeUpdate = st.execute();
			if (executeUpdate) {
				list=new ArrayList<Documento>();
				Documento doc=null;
				ResultSet rs = st.getResultSet();
				while (rs.next()) {
					doc=new Documento();
					doc.setC_documento(rs.getString(Documento.Campi.C_DOCUEMNTO));
					doc.setDescrizione(rs.getString(Documento.Campi.DESCRIZIONE));
					list.add(doc);
				}
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
			return null;
		} finally {
			try {
				if(st!=null)
					st.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				logger.info(e.getMessage());
				throw new Exception("errore in accesso al DB");
			}
		}
		logger.info("trovati elementi: "+(list==null?"0":list.size()));
		return list;
	}

}
