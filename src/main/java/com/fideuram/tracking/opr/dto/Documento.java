package com.fideuram.tracking.opr.dto;

public class Documento {
	
	private String c_documento;
	private String descrizione;
	public String getC_documento() {
		return c_documento;
	}
	public void setC_documento(String c_documento) {
		this.c_documento = c_documento;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public interface Campi{
		public final static String C_DOCUEMNTO="c_documento";
		public final static String DESCRIZIONE="descrizione";
	}

}
