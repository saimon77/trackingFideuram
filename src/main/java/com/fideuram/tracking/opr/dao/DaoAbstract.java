package com.fideuram.tracking.opr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fideuram.tracking.opr.dto.ElencoStati;
import com.fideuram.tracking.opr.restController.Puc001Controller;
import com.fideuram.tracking.opr.utility.Constant;

public class DaoAbstract {
	
	public Logger logger = Logger.getLogger(DaoAbstract.class);
	
	@Autowired
	@Qualifier("getProperties")
	private Properties prop;

	
	public Connection getConnection(String url,String user,String password,String driver) throws Exception {
		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			Class.forName(driver);

			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.info(e);
			throw new Exception("errore in accesso al DB");
//		} catch (ClassNotFoundException e) {
//		logger.info(e);
//		throw new Exception("errore in accesso al DB");
		}
	}

}
