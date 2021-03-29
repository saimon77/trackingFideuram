package com.fideuram.tracking.opr.service;

import java.util.List;

public interface MailSender {
	
	
	public boolean sendMail(
		      List<String> to, String subject, String text,List<Object> attach) throws Exception;

}
