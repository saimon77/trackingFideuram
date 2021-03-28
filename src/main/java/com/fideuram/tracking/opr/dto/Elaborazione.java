package com.fideuram.tracking.opr.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Elaborazione {

	private int elaborazioneID;
	private String polizza;
	private String contraente;
	private int tipoOperazioneTrackingID;
	private int tipoStatoTrackingID;
	private String statoSistema;
	private String canale;
	private String numeroPratica;
	private String codiceProdotto;
	private String modalitaPagamento;
	private Date dataStimaFinire;
	private BigDecimal importoOperazione;
	private String iban;
	private String contoBancario;
	private boolean isDocumentiMancanti;
	private Date dataEstrazione;
	private Date dataRichiesta;
	private int operazioneID;
	private String operazione;
	private boolean isPratica;
	private String tipoCategoria;
	private String tipoSottoCategoria;
	private boolean isBeneficiario;
	private boolean isAttiva;
	private int statoID;
	private Integer stato_precedente;
	private String stato;
	private int ordine;
	private boolean isVisibile;
	private boolean aggiornate;
	private String idProdotto;
	private String descrizioneStatoElaborazione;
	private BigDecimal importoRichiesto;
	private List<TipoStato> elencoStatiOperazione;

	public int getElaborazioneID() {
		return elaborazioneID;
	}

	public void setElaborazioneID(int elaborazioneID) {
		this.elaborazioneID = elaborazioneID;
	}

	public String getPolizza() {
		return polizza;
	}

	public void setPolizza(String polizza) {
		this.polizza = polizza;
	}

	public String getContraente() {
		return contraente;
	}

	public void setContraente(String contraente) {
		this.contraente = contraente;
	}

	public int getOperazioneID() {
		return operazioneID;
	}

	public void setOperazioneID(int operazioneID) {
		this.operazioneID = operazioneID;
	}

	public String getOperazione() {
		return operazione;
	}

	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	public boolean isPratica() {
		return isPratica;
	}

	public void setPratica(boolean isPratica) {
		this.isPratica = isPratica;
	}

	public String getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(String tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	public String getTipoSottoCategoria() {
		return tipoSottoCategoria;
	}

	public void setTipoSottoCategoria(String tipoSottoCategoria) {
		this.tipoSottoCategoria = tipoSottoCategoria;
	}

	public boolean isBeneficiario() {
		return isBeneficiario;
	}

	public void setBeneficiario(boolean isBeneficiario) {
		this.isBeneficiario = isBeneficiario;
	}

	public boolean isAttiva() {
		return isAttiva;
	}

	public void setAttiva(boolean isAttiva) {
		this.isAttiva = isAttiva;
	}

	public int getStatoID() {
		return statoID;
	}

	public void setStatoID(int statoID) {
		this.statoID = statoID;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

	public boolean isVisibile() {
		return isVisibile;
	}

	public void setVisibile(boolean isVisibile) {
		this.isVisibile = isVisibile;
	}

	public int getTipoStatoTrackingID() {
		return tipoStatoTrackingID;
	}

	public void setTipoStatoTrackingID(int tipoStatoTrackingID) {
		this.tipoStatoTrackingID = tipoStatoTrackingID;
	}

	public String getStatoSistema() {
		return statoSistema;
	}

	public void setStatoSistema(String statoSistema) {
		this.statoSistema = statoSistema;
	}

	public String getCanale() {
		return canale;
	}

	public void setCanale(String canale) {
		this.canale = canale;
	}

	public String getNumeroPratica() {
		return numeroPratica;
	}

	public void setNumeroPratica(String numeroPratica) {
		this.numeroPratica = numeroPratica;
	}

	public String getCodiceProdotto() {
		return codiceProdotto;
	}

	public void setCodiceProdotto(String codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}

	public String getModalitaPagamento() {
		return modalitaPagamento;
	}

	public void setModalitaPagamento(String modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}

	public Date getDataStimaFinire() {
		return dataStimaFinire;
	}

	public void setDataStimaFinire(Date dataStimaFinire) {
		this.dataStimaFinire = dataStimaFinire;
	}

	public BigDecimal getImportoOperazione() {
		return importoOperazione;
	}

	public void setImportoOperazione(BigDecimal importoOperazione) {
		this.importoOperazione = importoOperazione;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getContoBancario() {
		return contoBancario;
	}

	public void setContoBancario(String contoBancario) {
		this.contoBancario = contoBancario;
	}

	public boolean isDocumentiMancanti() {
		return isDocumentiMancanti;
	}

	public void setDocumentiMancanti(boolean isDocumentiMancanti) {
		this.isDocumentiMancanti = isDocumentiMancanti;
	}

	public Date getDataEstrazione() {
		return dataEstrazione;
	}

	public void setDataEstrazione(Date dataEstrazione) {
		this.dataEstrazione = dataEstrazione;
	}



	
	public interface Campi{
		public final static String ELABORAZIONEID="ElaborazioneID";
		public final static String POLIZZA="Polizza";
		public final static String CONTRAENTE="Contraente";
		public final static String TIPOOPERAZIONETRACKINGID="TipoOperazioneTrackingiD";
		public final static String TIPOSTATOTRACKINGID="TipoStatoTrackingID";
		public final static String STATOSISTEMA="StatoSistema";
		public final static String CANALE="Canale";
		public final static String NUMEROPRATICA="NumeroPratica";
		public final static String CODICEPRODOTTO="CodiceProdotto";
		public final static String MODALITAPAGAMENTO="ModalitaPagamento";
		public final static String DATASTIMAFINIRE="DataStimaFinire";
		public final static String IMPORTOOPERAZIONE="ImportoOperazione";
		public final static String IBAN="Iban";
		public final static String CONTOBANCARIO="ContoBancario";
		public final static String ISDOCUMENTIMANCANTI="isDocumentiMancanti";
		public final static String DATAESTRAZIONE="DataEstrazione";
		public final static String OPERAZIONEID="OperazioneID";
		public final static String OPERAZIONE="Operazione";
		public final static String ISPRATICA="isPratica";
		public final static String TIPOCATEGORIA="TipoCategoria";
		public final static String TIPOSOTTOCATEGORIA="TipoSottoCategoria";
		public final static String ISBENEFICIARIO="isBeneficiario";
		public final static String ISATTIVA="isAttiva";
		public final static String STATOID="StatoID";
		public final static String STATO="Stato";
		public final static String ORDINE="Ordine";
		public final static String ISVISIBILE="isVisibile";
		public final static String STATOPRECEDENTE="stato_precedente";
		public final static String DATARICHIESTA="DataRichiesta";
		public final static String ID_PRODOTTO="idProdotto";
		public final static String DESCRIZIONE_STATO_SISTEMA="DescrizioneStatoSistema";
		public final static String IMPORTO_RICHIESTO="ImportoRichiesto";

	}



	public int getTipoOperazioneTrackingID() {
		return tipoOperazioneTrackingID;
	}

	public void setTipoOperazioneTrackingID(int tipoOperazioneTrackingID) {
		this.tipoOperazioneTrackingID = tipoOperazioneTrackingID;
	}

	public boolean isAggiornate() {
		return aggiornate;
	}

	public void setAggiornate(boolean aggiornate) {
		this.aggiornate = aggiornate;
	}

	public Integer getStato_precedente() {
		return stato_precedente;
	}

	public void setStato_precedente(Integer stato_precedente) {
		this.stato_precedente = stato_precedente;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public String getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getDescrizioneStatoElaborazione() {
		return descrizioneStatoElaborazione;
	}

	public void setDescrizioneStatoElaborazione(String descrizioneStatoElaborazione) {
		this.descrizioneStatoElaborazione = descrizioneStatoElaborazione;
	}

	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public List<TipoStato> getElencoStatiOperazione() {
		return elencoStatiOperazione;
	}

	public void setElencoStatiOperazione(List<TipoStato> elencoStatiOperazione) {
		this.elencoStatiOperazione = elencoStatiOperazione;
	}


}
