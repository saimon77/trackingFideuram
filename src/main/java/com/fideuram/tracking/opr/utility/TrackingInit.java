package com.fideuram.tracking.opr.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class TrackingInit {

	private final static Logger LOGGER = Logger.getLogger(TrackingInit.class);

	private Properties prop = new Properties();
	
	


	
	public String init() {
		LOGGER.info("Inizializzo il context v1");
		
		LOGGER.info("provo le configurazione esterne tramite SystemProperties");
		String externaPath = System.getProperty("trackExternal");
		String tipoPropCaricate="";
		if (externaPath == null) {
			LOGGER.error("path properties tramite SystemProperties non trovato");
		} else {
			try {
//				inizializzo il log4j
				PropertyConfigurator.configure(externaPath + "log4j.properties");
				LOGGER.info("Caricata configurazione log esterna");
			} catch (Exception e) {
				LOGGER.error("Configurazione log esterna non caricata:  File non trovato");
			}
			try {
//				inizializzo le properties esterne
				prop.load(new FileInputStream(new File(externaPath + "app.properties")));
				tipoPropCaricate="SystemProperties";
				LOGGER.info("SystemProperties caricate correttamente");
			} catch (Exception e) {
				LOGGER.error("errore durante il caricamento delle System properties ");
			}

		}
		if (prop.isEmpty()) {
			Context ctx;
			Context initCtx = null;
			try {
				LOGGER.info("provo le configurazione esterne tramite JNDI");
				ctx = new InitialContext();
				initCtx = (Context) ctx.lookup("java:/comp/env");
				LOGGER.info("contesto JNDI trovato");
				if (initCtx != null) {
//				cerco le prop esterne
						LOGGER.info("Cerco le properties tramite JNDI");
						try {
							externaPath = (String) initCtx.lookup("trackExternal");
							LOGGER.info("path configurazione esterne: " + externaPath);
						} catch (Exception e) {
							LOGGER.error("path properties tramite JNDI non trovato");
						}
				if (externaPath != null) {
					FileInputStream is = null;
					try {
//						inizializzo il log4j
						is = new FileInputStream(new File(externaPath + "log4j.properties"));
						PropertyConfigurator.configureAndWatch(externaPath + "log4j.properties");
						LOGGER.info("Caricata configurazione log esterna");
						if (is != null)
							is.close();
					} catch (Exception e) {
						LOGGER.error("Configurazione log esterna non caricata:  File non trovato");
					}
					try {
//						inizializzo le properties esterne
						prop.load(new FileInputStream(new File(externaPath + "app.properties")));
						tipoPropCaricate="JNDI";
						LOGGER.info("JNDI properties caricate correttamente");
					} catch (Exception e) {
						LOGGER.error("Configurazione app esterna non caricata:  File non trovato");
					}
				}
				}else {
					LOGGER.info("contesto JNDI non trovato");
				}
				
			} catch (NamingException e) {
				LOGGER.error(e.getMessage());
				LOGGER.error("errore nella creazione del contesto esterno tramite JNDI");
			}

		}

		if (prop.isEmpty()) {
			try {
				LOGGER.info("caricate le properties interne");
				prop.load(TrackingInit.class.getClassLoader().getResourceAsStream("app.properties"));
				tipoPropCaricate="INTERNE";
			} catch (IOException e) {
				LOGGER.error("properties interne non trovate");
			}
		}
		LOGGER.info("***** PROPERTIES CARICATE: "+tipoPropCaricate+" *****");
		for (Object p : prop.keySet()) {
			LOGGER.info((String) p + " = " + prop.getProperty((String) p));
		}
		LOGGER.info("************************************************");
		LOGGER.info("Context inizializzato");
		return "";
	}
	
	@Bean
	public Session getJavaMailSender() {
		init();
//	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	    mailSender.setHost(prop.getProperty("mail.host"));
//	    try {
//	    mailSender.setPort(Integer.parseInt(prop.getProperty("mail.port")));
//	    }catch (Exception e) {
//			LOGGER.error(String.format("la porta %s non è una porta valida", prop.getProperty("mail.port")));
//		}
//	    mailSender.setUsername(prop.getProperty("mail.username"));
//	    mailSender.setPassword(prop.getProperty("mail.password"));
	    
	    Properties propsMail = new Properties();
	    propsMail.put("mail.smtp.host", prop.getProperty("mail.host"));
	    propsMail.put("mail.smtp.port", prop.getProperty("mail.port"));
	    propsMail.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
	    propsMail.put("mail.smtp.starttls.enable", prop.getProperty("smtp.starttls.enable"));
	    propsMail.put("mail.debug", prop.getProperty("mail.debug"));
	    Session session = Session.getInstance(propsMail,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(prop.getProperty("mail.username"), prop.getProperty("mail.password"));
                    }
                });
	    
	    return  session;
	}
	
//	@Bean
//	public String init() {
//		LOGGER.info("Inizializzo il context");
//		Context ctx;
//		Context initCtx = null;
//		try {
//			LOGGER.info("provo le configurazione esterne tramite JNDI");
//			ctx = new InitialContext();
//			initCtx = (Context) ctx.lookup("java:/comp/env");
//			LOGGER.info("contesto JNDI trovato");
//			
//		} catch (NamingException e) {
//			LOGGER.error(e.getMessage());
//			LOGGER.error("errore nella creazione del contesto esterno tramite JNDI");
////			return "";
//		}
//		
//		String externaPath = null;
//		if (initCtx != null) {
////	cerco le prop esterne
//			LOGGER.info("Cerco le properties tramite JNDI");
//			try {
//				externaPath = (String) initCtx.lookup("trackExternal");
//				LOGGER.info("path configurazione esterne: " + externaPath);
//			} catch (Exception e) {
//				LOGGER.error("path properties non trovato");
//			}
//			if (externaPath != null) {
//				FileInputStream is = null;
//				try {
////					inizializzo il log4j
//					is = new FileInputStream(new File(externaPath + "log4j.properties"));
//					PropertyConfigurator.configureAndWatch(externaPath + "log4j.properties");
//					LOGGER.info("Caricata configurazione log esterna");
//					if (is != null)
//						is.close();
//				} catch (Exception e) {
//					LOGGER.error("Configurazione log esterna non caricata:  File non trovato");
//				}
//				try {
////					inizializzo le properties esterne
//					prop.load(new FileInputStream(new File(externaPath + "app.properties")));
//					LOGGER.info("JNDI properties caricate correttamente");
//				} catch (Exception e) {
//					LOGGER.error("Configurazione app esterna non caricata:  File non trovato");
//				}
//			}
//		}
//		if (prop.isEmpty()) {
//			LOGGER.info("provo le configurazione esterne tramite SystemProperties");
//			externaPath = System.getProperty("trackExternal");
//			if (externaPath == null) {
//				LOGGER.error("path properties non trovato");
//			} else {
//				try {
////					inizializzo il log4j
//					PropertyConfigurator.configure(externaPath + "log4j.properties");
//					LOGGER.info("Caricata configurazione log esterna");
//				} catch (Exception e) {
//					LOGGER.error("Configurazione log esterna non caricata:  File non trovato");
//				}
//				try {
////					inizializzo le properties esterne
//					prop.load(new FileInputStream(new File(externaPath + "app.properties")));
//					
//					LOGGER.info("SystemProperties caricate correttamente");
//				} catch (Exception e) {
//					LOGGER.error("errore durante il caricamento delle System properties ");
//				}
//				
//			}
//		}
//		
//		if (prop.isEmpty()) {
//			try {
//				LOGGER.info("caricate le properties interne");
//				prop.load(TrackingInit.class.getClassLoader().getResourceAsStream("app.properties"));
//			} catch (IOException e) {
//				LOGGER.error("properties interne non trovate");
//			}
//		}
//		for (Object p : prop.keySet()) {
//			LOGGER.info((String) p + " = " + prop.getProperty((String) p));
//		}
//		LOGGER.info("Context inizializzato");
//		return "";
//	}

	@Bean
	Properties getProperties() {
		return prop;
	}

}
