package com.fideuram.tracking.opr.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MailSenderImpl implements MailSender{
	
	public Logger logger = Logger.getLogger(MailSenderImpl.class);
	@Autowired
	private Session mailServ;
	
	@Autowired
	@Qualifier("getProperties")
	private Properties prop;

	@Override
	public boolean sendMail(List<String> listTo, String subject, String text, List<Object> attach)
			throws Exception {
		if(listTo==null||listTo.isEmpty())
			{
			logger.warn("nessun destinatario mail passato");
			return false;
			}
		for (String to : listTo) {
			
			try {
				Message message = new MimeMessage(mailServ);
				message.setFrom(new InternetAddress(prop.getProperty("mail.from")));
				message.setRecipient(
						Message.RecipientType.TO,
						new InternetAddress(to)
						);
//per inviare direttamente ad una lista
//				message.setRecipients(
//						Message.RecipientType.TO,
//						InternetAddress.parse(to)
//						);
				message.setSubject(subject);
				message.setText(text);
				
				Transport.send(message);
			}catch (Exception e) {
				logger.error(String.format("mail non inviata per il destinatario %S Errore: %s", to,e.getMessage()));
				throw new Exception(String.format("mail non inviata per il destinatario %S Errore: %s", to,e.getMessage()));
			}
			logger.info(String.format("mail correttamente inviata a %S", to));
		}
		return true;
	}
	
	
	

}
