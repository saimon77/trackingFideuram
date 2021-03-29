package com.fideuram.tracking.opr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class FTPUploader {

	public Logger logger = Logger.getLogger(FTPUploader.class);

	private String server = "";
	private String path;
	private String user = "";
	private String pass = "";
	private int port;
	private FTPClient ftpClient;

	public FTPUploader(String server, String user, String pass, int port, String path) {
		this.server = server;
		this.user = user;
		this.pass = pass;
		this.port = port;
		this.path = path;
		this.ftpClient = new FTPClient();
	}

	public FTPUploader(String server, String user, String pass, String path) {
		this.server = server;
		this.user = user;
		this.pass = pass;
		this.path = path;
		this.port = 21;

		this.ftpClient = new FTPClient();
	}

	public FTPUploader(String server, String user, String pass) {
		this.server = server;
		this.user = user;
		this.pass = pass;
		this.path = "/";
		this.port = 21;

		this.ftpClient = new FTPClient();
	}

	public FTPUploader(String server, String user, String pass, int port) {
		this.server = server;
		this.user = user;
		this.pass = pass;
		this.path = "/";
		this.port = port;

		this.ftpClient = new FTPClient();
	}

	public boolean sendFile(File localFile) throws Exception {
		boolean done = false;

		try {
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			String remoteFile = path + localFile.getName();
			logger.info("invio ftp: " + remoteFile);

			InputStream inputStream = new FileInputStream(localFile);

			done = ftpClient.storeFile(remoteFile, inputStream);
			inputStream.close();
			logger.info("file uploadato con successo");
//          // APPROACH #2: uploads second file using an OutputStream
//          File secondLocalFile = new File("E:/Test/Report.doc");
//          String secondRemoteFile = "test/Report.doc";
//          inputStream = new FileInputStream(secondLocalFile);
//
//          System.out.println("Start uploading second file");
//          OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
//          byte[] bytesIn = new byte[4096];
//          int read = 0;
//
//          while ((read = inputStream.read(bytesIn)) != -1) {
//              outputStream.write(bytesIn, 0, read);
//          }
//          inputStream.close();
//          outputStream.close();
//
//          boolean completed = ftpClient.completePendingCommand();
//          if (completed) {
//              System.out.println("The second file is uploaded successfully.");
//          }
			
			if(!done)
				throw new Exception("Invio ftp non eseguito verificare la connessione");

		} catch (IOException ex) {
			logger.error(ex);
			throw new Exception("Invio ftp non eseguito verificare la connessione al server ftp");
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return done;
	}

}
