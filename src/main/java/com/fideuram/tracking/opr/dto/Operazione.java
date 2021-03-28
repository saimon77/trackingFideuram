package com.fideuram.tracking.opr.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operazione {
	
	
	private int tipoOperazioneTrackingID;
	private String operazione;
	private Map<String,List<Elaborazione>> categorie;
//	private List<TipoStato> elencoStatiOperazione;
	
	
	public int getTipoOperazioneTrackingID() {
		return tipoOperazioneTrackingID;
	}
	public void setTipoOperazioneTrackingID(int tipoOperazioneTrackingID) {
		this.tipoOperazioneTrackingID = tipoOperazioneTrackingID;
	}
	public String getOperazione() {
		return operazione;
	}
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}
//	public List<TipoStato> getElencoStatiOperazione() {
//		return elencoStatiOperazione;
//	}
//	public void setElencoStatiOperazione(List<TipoStato> elencoStatiOperazione) {
//		this.elencoStatiOperazione = elencoStatiOperazione;
//	}
	public Map<String, List<Elaborazione>> getCategorie() {
		if(categorie==null)
			categorie=new HashMap<String, List<Elaborazione>>();
		return categorie;
	}
	public void setCategorie(Map<String, List<Elaborazione>> categorie) {
		this.categorie = categorie;
	}

	
	

}
