package com.fideuram.tracking.opr.dto;

import java.sql.Date;

public class ReferenteTerzo {
	
	private String polizzaID;
	private int numReferente;
	private Date dataInizio;
	private Date dataFine;
	private String codFiscale;
	private String cognome;
	private String nome;
	private String indirizzo;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private Date tIMESTAMP;
	public String getPolizzaID() {
		return polizzaID;
	}
	public void setPolizzaID(String polizzaID) {
		this.polizzaID = polizzaID;
	}
	public int getNumReferente() {
		return numReferente;
	}
	public void setNumReferente(int numReferente) {
		this.numReferente = numReferente;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getLocalita() {
		return localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public Date gettIMESTAMP() {
		return tIMESTAMP;
	}
	public void settIMESTAMP(Date tIMESTAMP) {
		this.tIMESTAMP = tIMESTAMP;
	}

	public interface CAMPI{
		public final String POLIZZAID="polizzaID";
		public final String NUMREFERENTE="numReferente";
		public final String DATAINIZIO="dataInizio";
		public final String DATAFINE="dataFine";
		public final String CODFISCALE="codFiscale";
		public final String COGNOME="cognome";
		public final String NOME="nome";
		public final String INDIRIZZO="indirizzo";
		public final String CAP="cap";
		public final String LOCALITA="localita";
		public final String PROVINCIA="provincia";
		public final String NAZIONE="nazione";
		public final String TIMESTAMP="TIMESTAMP";

	}

}
