package com.fideuram.tracking.opr.dto;

import java.sql.Date;

public class Agenda {

	private int numeroNuoviMovimenti;
	private Date dataUltimoAccesso;

	
	public interface Campi{
		public final static String DATA_ULTIMO_ACCESSO="data_ultimo_accesso";
		public final static String TOT_DOC_MOVIMENTATI="tot_doc_movimentati";
		
	}


	public int getNumeroNuoviMovimenti() {
		return numeroNuoviMovimenti;
	}


	public void setNumeroNuoviMovimenti(int numeroNuoviMovimenti) {
		this.numeroNuoviMovimenti = numeroNuoviMovimenti;
	}


	public Date getDataUltimoAccesso() {
		return dataUltimoAccesso;
	}


	public void setDataUltimoAccesso(Date dataUltimoAccesso) {
		this.dataUltimoAccesso = dataUltimoAccesso;
	}


}
