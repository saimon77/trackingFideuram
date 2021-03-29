package com.fideuram.tracking.opr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.fideuram.tracking.opr.restController.Puc001Controller;

public class DaoAbstract {
	
	public Logger logger = Logger.getLogger(DaoAbstract.class);

	
	public Connection getConnection(String url,String user,String password) throws Exception {
		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Class.forName("net.sourceforge.jtds.jdbc.Driver");

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
