package com.fideuram.tracking.opr.dto;

import java.util.ArrayList;
import java.util.List;

public class ElencoStati {
	
	private String idOperazione;
	List<TipoStato> listaStati;
	public String getIdOperazione() {
		return idOperazione;
	}
	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}
	public List<TipoStato> getListaStati() {
		if(listaStati==null)
			listaStati=new ArrayList<TipoStato>();
		return listaStati;
	}
	public void setListaStati(List<TipoStato> listaStati) {
		this.listaStati = listaStati;
	}

}
