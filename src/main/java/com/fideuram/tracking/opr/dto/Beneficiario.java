package com.fideuram.tracking.opr.dto;

import java.sql.Date;

public class Beneficiario {
	
	private String polizzaID;
	private String tipologia_beneficiario;
	private String descrizione;
	private int iD;
	private String tipoCasoBeneficiarioID;
	private String tipoBeneficiarioID;
	private String codiceFiscale_PartitaIVA;
	private String discriminator;
	private Date dataInizio;
	private Date dataFine;
	private String cognome_Denominazione;
	private String nome;
	private String sesso;
	private Date dataNascita;
	private String localitaNascita;
	private String nazioneNascita;
	private String indirizzo;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private Double percentualeBeneficio;
	private Date timestamp;
	private int tipoRelazioneBeneficiarioID_Contraente;
	private int tipoRelazioneBeneficiarioID_Assicurato;
	
	public String getPolizzaID() {
		return polizzaID;
	}
	public void setPolizzaID(String polizzaID) {
		this.polizzaID = polizzaID;
	}
	public String getTipologia_beneficiario() {
		return tipologia_beneficiario;
	}
	public void setTipologia_beneficiario(String tipologia_beneficiario) {
		this.tipologia_beneficiario = tipologia_beneficiario;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getiD() {
		return iD;
	}
	public void setiD(int iD) {
		this.iD = iD;
	}
	public String getTipoCasoBeneficiarioID() {
		return tipoCasoBeneficiarioID;
	}
	public void setTipoCasoBeneficiarioID(String tipoCasoBeneficiarioID) {
		this.tipoCasoBeneficiarioID = tipoCasoBeneficiarioID;
	}
	public String getTipoBeneficiarioID() {
		return tipoBeneficiarioID;
	}
	public void setTipoBeneficiarioID(String tipoBeneficiarioID) {
		this.tipoBeneficiarioID = tipoBeneficiarioID;
	}
	public String getCodiceFiscale_PartitaIVA() {
		return codiceFiscale_PartitaIVA;
	}
	public void setCodiceFiscale_PartitaIVA(String codiceFiscale_PartitaIVA) {
		this.codiceFiscale_PartitaIVA = codiceFiscale_PartitaIVA;
	}
	public String getDiscriminator() {
		return discriminator;
	}
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
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
	public String getCognome_Denominazione() {
		return cognome_Denominazione;
	}
	public void setCognome_Denominazione(String cognome_Denominazione) {
		this.cognome_Denominazione = cognome_Denominazione;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getLocalitaNascita() {
		return localitaNascita;
	}
	public void setLocalitaNascita(String localitaNascita) {
		this.localitaNascita = localitaNascita;
	}
	public String getNazioneNascita() {
		return nazioneNascita;
	}
	public void setNazioneNascita(String nazioneNascita) {
		this.nazioneNascita = nazioneNascita;
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
	public Double getPercentualeBeneficio() {
		return percentualeBeneficio;
	}
	public void setPercentualeBeneficio(Double percentualeBeneficio) {
		this.percentualeBeneficio = percentualeBeneficio;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public int getTipoRelazioneBeneficiarioID_Contraente() {
		return tipoRelazioneBeneficiarioID_Contraente;
	}
	public void setTipoRelazioneBeneficiarioID_Contraente(int tipoRelazioneBeneficiarioID_Contraente) {
		this.tipoRelazioneBeneficiarioID_Contraente = tipoRelazioneBeneficiarioID_Contraente;
	}
	public int getTipoRelazioneBeneficiarioID_Assicurato() {
		return tipoRelazioneBeneficiarioID_Assicurato;
	}
	public void setTipoRelazioneBeneficiarioID_Assicurato(int tipoRelazioneBeneficiarioID_Assicurato) {
		this.tipoRelazioneBeneficiarioID_Assicurato = tipoRelazioneBeneficiarioID_Assicurato;
	}
	
	public interface CAMPI{
		public final String POLIZZAID="polizzaID";
		public final String TIPOLOGIA_BENEFICIARIO="tipologia_beneficiario";
		public final String DESCRIZIONE="Descrizione";
		public final String ID="ID";
		public final String TIPOCASOBENEFICIARIOID="TipoCasoBeneficiarioID";
		public final String TIPOBENEFICIARIOID="TipoBeneficiarioID";
		public final String CODICEFISCALE_PARTITAIVA="CodiceFiscale_PartitaIVA";
		public final String DISCRIMINATOR="Discriminator";
		public final String DATAINIZIO="DataInizio";
		public final String DATAFINE="DataFine";
		public final String COGNOME_DENOMINAZIONE="Cognome_Denominazione";
		public final String NOME="Nome";
		public final String SESSO="Sesso";
		public final String DATANASCITA="DataNascita";
		public final String LOCALITANASCITA="LocalitaNascita";
		public final String NAZIONENASCITA="NazioneNascita";
		public final String INDIRIZZO="Indirizzo";
		public final String CAP="Cap";
		public final String LOCALITA="Localita";
		public final String PROVINCIA="Provincia";
		public final String NAZIONE="Nazione";
		public final String PERCENTUALEBENEFICIO="PercentualeBeneficio";
		public final String TIMESTAMP="Timestamp";
		public final String TIPORELAZIONEBENEFICIARIOID_CONTRAENTE="TipoRelazioneBeneficiarioID_Contraente";
		public final String TIPORELAZIONEBENEFICIARIOID_ASSICURATO="TipoRelazioneBeneficiarioID_Assicurato";

	}


}
