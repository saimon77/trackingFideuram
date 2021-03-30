package com.fideuram.tracking.opr.dao;

import java.sql.Timestamp;
import java.util.List;

import com.fideuram.tracking.opr.dto.Agenda;
import com.fideuram.tracking.opr.dto.Beneficiario;
import com.fideuram.tracking.opr.dto.Elaborazione;
import com.fideuram.tracking.opr.dto.ElencoStati;
import com.fideuram.tracking.opr.dto.Pagamento;
import com.fideuram.tracking.opr.dto.ReferenteTerzo;
import com.fideuram.tracking.opr.dto.Soggetto;
import com.fideuram.tracking.opr.dto.TipoStato;
import com.fideuram.tracking.opr.dto.response.RespElaborazione;

public interface DaoOpr {
	
	public List<Elaborazione> getUltimeElaborazioni(String numeroPratica,String cf,String categoria,String sottoCategoria) throws Exception;

	public List<Elaborazione> getDettaglioElaborazione(String cf,String categoria,String sottoCategoria,String polizza,String numeroPratica,String dataRichiesta,int tipoOperazioneTrackingID) throws Exception;

	public boolean updateLastView(String cf) throws Exception;
	
	public Timestamp getLastvieW(String cf) throws Exception;
	
	public List<ElencoStati> getElencoStati() throws Exception;
	
	public boolean sentDoc(String cf,String polizza,String utente,String nomeFile,String nomeUpload) throws Exception;
	
	public Agenda getAgenda(String cf) throws Exception;

	public List<Beneficiario> listBen(String polizza)throws Exception;

	public List<ReferenteTerzo> listRefTer(String polizza) throws Exception;

	public boolean updateRef(ReferenteTerzo ref,String tipoModifica,String utente)throws Exception;

	public boolean updateBen(Beneficiario ben, String tipoModifica, String utente)throws Exception;

	public boolean aggiungiReferenteTerzo(String codiceFiscaleReferente, Integer numeroReferente, String cognomeReferente,
			String nomeReferente, String indirizzoReferente, String localitaReferente, String provinciaReferente,
			String capReferente, String nazioneReferente, String polizza, String userID, String parametro)throws Exception;

	public boolean revocaReferenteTerzo(String codiceFiscaleReferente, Integer numeroReferente, String cognomeReferente,
			String nomeReferente, String indirizzoReferente, String localitaReferente, String provinciaReferente,
			String capReferente, String nazioneReferente, String polizza, String userID, String parametro)throws Exception;

	public Soggetto getAssicurato(String polizza)throws Exception;

	public Soggetto getContraente(String polizza)throws Exception;

	public int geDocInviati(String cf,String polizza)throws Exception;
	
	public Pagamento getPagamento(String cf, String polizza, String numeroPratica) throws Exception;

}
