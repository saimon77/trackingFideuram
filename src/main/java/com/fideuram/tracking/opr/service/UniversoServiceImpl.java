package com.fideuram.tracking.opr.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fideuram.aggiornauniverso.ws.UniversoWsService;
import com.fideuram.tracking.opr.dao.DaoOpr;
import com.fideuram.tracking.opr.dto.ReferenteTerzo;

@Service
public class UniversoServiceImpl extends AbstractUniverso implements UniversoService {
	
	public Logger logger = Logger.getLogger(AbstractUniverso.class);
	
	@Autowired
	private DaoOpr dao;

	@Override
	public boolean insertReferenteAmministrativo(ReferenteTerzo ref,String parametro) throws Exception {
		logger.info("chiamo uvierso");
		UniversoWsService ws = getUniversoWS();
		String codiceFiscaleReferente=ref.getCodFiscale();
		Integer numeroReferente=ref.getNumReferente();
		String cognomeReferente=ref.getCognome();
		String nomeReferente=ref.getNome();
		String indirizzoReferente=ref.getIndirizzo();
		String localitaReferente=ref.getLocalita();
		String provinciaReferente=ref.getProvincia();
		String capReferente=ref.getCap();
		String nazioneReferente=ref.getNazione();
		String polizza=ref.getPolizzaID();
		String userID="V000000";;
		Integer resultUniverso=null;
		try {
		resultUniverso = ws.insertReferenteAmministrativo(codiceFiscaleReferente,numeroReferente,cognomeReferente,nomeReferente,indirizzoReferente,localitaReferente,provinciaReferente,capReferente,nazioneReferente,polizza,userID,parametro);
		logger.info("INSERIMENTO AVVENUTO CON SUCCESSO per la polizza : "+polizza);
		}catch (Exception e) {
			logger.error("ERRORE NELL'INSERIMENTO SU UNIVERSO per la polizza : "+polizza);
			return false;
		}
		 //inserisco su puc solo se universo è andato bene
	 	if(resultUniverso !=null){
		 	
		 	boolean inserimentoPuc = false;
		 	
		 	numeroReferente = resultUniverso;
			 		
			   	 if(parametro.equalsIgnoreCase("aggiungi")){
			   		
			   		inserimentoPuc = dao.aggiungiReferenteTerzo( codiceFiscaleReferente, numeroReferente, cognomeReferente, nomeReferente, indirizzoReferente, localitaReferente, provinciaReferente, capReferente, nazioneReferente, polizza, userID,  parametro);
			   	 
			   	 }
			   	 else if (parametro.equalsIgnoreCase("modifica")){
			   		 
			   		inserimentoPuc = dao.revocaReferenteTerzo( codiceFiscaleReferente, numeroReferente, cognomeReferente, nomeReferente, indirizzoReferente, localitaReferente, provinciaReferente, capReferente, nazioneReferente, polizza, userID,  parametro);
			  		 
			   		if(inserimentoPuc){
			   			
			   			inserimentoPuc = dao.aggiungiReferenteTerzo( codiceFiscaleReferente, numeroReferente, cognomeReferente, nomeReferente, indirizzoReferente, localitaReferente, provinciaReferente, capReferente, nazioneReferente, polizza, userID,  parametro);
			   	   	 
			   		}
			   		 
			   	 }
			   	 else if (parametro.equalsIgnoreCase("revoca")){
			   		 
			   		inserimentoPuc = dao.revocaReferenteTerzo( codiceFiscaleReferente, numeroReferente, cognomeReferente, nomeReferente, indirizzoReferente, localitaReferente, provinciaReferente, capReferente, nazioneReferente, polizza, userID,  parametro);
			   		 
			   	 }
		}
		
		
		return true;
	}
	
	

}
