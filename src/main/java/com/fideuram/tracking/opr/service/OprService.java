package com.fideuram.tracking.opr.service;

import java.util.List;

import com.fideuram.tracking.opr.dto.Agenda;
import com.fideuram.tracking.opr.dto.Beneficiario;
import com.fideuram.tracking.opr.dto.Elaborazione;
import com.fideuram.tracking.opr.dto.MapStringObj;
import com.fideuram.tracking.opr.dto.Operazione;
import com.fideuram.tracking.opr.dto.Pagamento;
import com.fideuram.tracking.opr.dto.ReferenteTerzo;

public interface OprService {
	
	public List<Operazione>  getUltimeElaborazioni(String polizza,String cf,String categoria,String sottoCategoria) throws Exception;
	
	public List<Operazione> getDettaglioElaborazione(String cf,String categoria,String sottoCategoria,String polizza,String numeroPratica,String dataRichiesta) throws Exception;

	public boolean updateLastView(String cf) throws Exception;
	
	public boolean sentDoc(String cf, String polizza,String utente,MapStringObj filedata) throws Exception;

	public Agenda getAgenda(List<Operazione> objResp,String cf) throws Exception;

	public List<Beneficiario> listBen(String polizza)throws Exception;

	public List<ReferenteTerzo> listRefTer(String polizza) throws Exception;

	public boolean updateRef(ReferenteTerzo ref,String mod,String utente)throws Exception;
	
	public boolean updateBen(Beneficiario ben,String mod,String utente)throws Exception;

	public byte[] getFile(String polizza, String idFile)throws Exception;
	
	public Pagamento getPagamento(String cf,String polizza,String numeroPratica) throws Exception;
	
}
