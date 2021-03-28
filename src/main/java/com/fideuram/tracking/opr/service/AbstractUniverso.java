package com.fideuram.tracking.opr.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fideuram.aggiornauniverso.ws.UniversoWsService;
import com.fideuram.aggiornauniverso.ws.UniversoWsServiceService;

public class AbstractUniverso {
	public static Logger logger = Logger.getLogger(AbstractUniverso.class);

	@Autowired
	@Qualifier("getProperties")
	Properties prop;

	public com.fideuram.aggiornauniverso.ws.UniversoWsService getUniversoWS() throws Exception {
		logger.info("chiamo il client");
		UniversoWsService ported;
		URL url = null;
		String wsdl = prop.getProperty("wsdlUniverso.url");
		logger.info("url universo: " + wsdl);
//				String wsdl = "http://fvbsappl081c.fidevita.bancafideuram.it:8080/UniversoWS/UniversoWsService?wsdl";
		try {
//		            URL baseUrl;
			// baseUrl =
			// com.fideuram.puc.service.impl.SchedaTrasformazioneServices.class.getResource(".");
//		            baseUrl = UniversoWsServiceService.class.getResource(".");
//		            url = new URL(baseUrl, wsdl);
			url = new URL(wsdl);
		} catch (MalformedURLException e) {
			logger.error("Failed to create URL for the wsdl Location: '" + wsdl + " '");
			e.printStackTrace();
		}
		ported = new UniversoWsServiceService(url,
				new QName("http://com.fidevita.bancafideuram.it", "UniversoWsServiceService"))
						.getUniversoWsServicePort();
		if (ported == null) {
			logger.error("errore nella risposta del client ");
			throw new Exception("errore nella risposta del client ");
		}
		return ported;
	}

}
