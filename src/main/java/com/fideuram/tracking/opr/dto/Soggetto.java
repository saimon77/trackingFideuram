package com.fideuram.tracking.opr.dto;

public class Soggetto {

	private String tipo;
	private String nome;
	private String cognome;
	private String ragioneSociale;
	private String id;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public interface Campi{
		public final static String ID="id";
		public final static String NOME="Nome";
		public final static String COGNOME="Cognome";
		public final static String TIPO="tipo";
		public static final String RAGIONE_SOCIALE = "ragioneSociale";
	}

}
