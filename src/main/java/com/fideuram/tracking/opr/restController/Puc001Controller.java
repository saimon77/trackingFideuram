package com.fideuram.tracking.opr.restController;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fideuram.tracking.opr.dto.Agenda;
import com.fideuram.tracking.opr.dto.Beneficiario;
import com.fideuram.tracking.opr.dto.MapStringObj;
import com.fideuram.tracking.opr.dto.Operazione;
import com.fideuram.tracking.opr.dto.Pagamento;
import com.fideuram.tracking.opr.dto.ReferenteTerzo;
import com.fideuram.tracking.opr.dto.TipoCrud;
import com.fideuram.tracking.opr.dto.response.RespBeneficiari;
import com.fideuram.tracking.opr.dto.response.RespElaborazione;
import com.fideuram.tracking.opr.dto.response.RespPagamento;
import com.fideuram.tracking.opr.dto.response.RespReferenteTer;
import com.fideuram.tracking.opr.dto.response.ResponseFile;
import com.fideuram.tracking.opr.dto.response.SimpleResponse;
import com.fideuram.tracking.opr.service.OprService;

@Controller
@RequestMapping(value = "/puc")
public class Puc001Controller {

	public Logger logger = Logger.getLogger(Puc001Controller.class);

	@Autowired
	OprService serv;

	@RequestMapping(value = "/LastOpr", method = RequestMethod.GET)
	public @ResponseBody RespElaborazione lastOpr(@RequestParam(required = false) String polizza,
			@RequestParam(required = false) String cf, @RequestParam(required = false) String categoria,
			@RequestParam(required = false) String sottoCategoria) {
		logger.info(
				"ricevuta richiesta LastOpr con " + polizza + " - " + cf + " - " + categoria + " - " + sottoCategoria);
		RespElaborazione resp;
		if (cf == null || "".equals(cf.trim())) {
			resp = new RespElaborazione();
			resp.setErrorMessage("Il codice cf risulta vuoto o nullo");
		}
		if (categoria == null && sottoCategoria != null) {
			resp = new RespElaborazione();
			resp.setErrorMessage("la categoria non può essere vuota se è valorizzata anche la sottoCategoria");
			return resp;
		}
		if (sottoCategoria == null && categoria != null) {
			resp = new RespElaborazione();
			resp.setErrorMessage("la sottoCategoria non può essere vuota se è valorizzata anche la categoria");
			return resp;
		}
		List<Operazione> objResp = null;
		resp = new RespElaborazione();
		Agenda agenda = null;
		try {
			objResp = serv.getUltimeElaborazioni(polizza, cf, categoria, sottoCategoria);
			if (objResp != null && cf != null) {
				agenda = serv.getAgenda(objResp, cf);
				serv.updateLastView(cf);
			} else if (cf != null)
				serv.updateLastView(cf);
		} catch (Exception e) {
			resp.setErrorMessage("ATTENZIONE Errore nel recupero dati: " + e.getMessage());
			logger.error(e.getMessage(),e);
			return resp;
		}

		if (objResp == null || objResp != null && objResp.isEmpty())
			resp.setErrorMessage("nessun elemento trovato con i parametri polizza= " + polizza + " CF= " + cf
					+ " - categoria= " + categoria + " - sottoCategoria= " + sottoCategoria);

		else {
			resp.setListOpr(objResp);
			if (agenda != null)
				resp.setAgenda(agenda);
		}
		logger.info("terminata richiesta LastOpr");
		return resp;
	}

	// query con polizza più pratica e se presente dataRichiesta (requeired=false)
	// info deve contenere "DescrizioneStatoSistema"
	// verificare le date che corrispondano al campo "Data_richiesta"
	// ???? contraente e assicurato tipoBeneficiario 1 assic 8 contraente
	@RequestMapping(value = "/DetailOpr", method = RequestMethod.GET)
	public @ResponseBody RespElaborazione detailOpr(@RequestParam(required = false) String cf,
			@RequestParam(required = false) String categoria, @RequestParam(required = false) String sottoCategoria,
			@RequestParam(required = false) String numeroPratica, @RequestParam String polizza,
			@RequestParam(required = false) String dataRichiesta) {
		logger.info("ricevuta richiesta DetailOpr con " + cf + " - " + " - " + polizza + " - " + dataRichiesta + " - "
				+ categoria + " - " + sottoCategoria);
		RespElaborazione resp;
//		if(!isOk(numeroPratica,categoria,sottoCategoria)){
//			resp = new RespElaborazione();
//			resp.setErrorMessage("il numero pratica la categoria e la sottogateria o sono nulli o sono devono essere tutti presenti");
//			return resp;
//		}
		if (categoria == null && sottoCategoria != null) {
			resp = new RespElaborazione();
			resp.setErrorMessage("la categoria non può essere vuota se è valorizzata anche la sottoCategoria");
			return resp;
		}
		if (sottoCategoria == null && categoria != null) {
			resp = new RespElaborazione();
			resp.setErrorMessage("la sottoCategoria non può essere vuota se è valorizzata anche la categoria");
			return resp;
		}
		List<Operazione> objResp = null;
		resp = new RespElaborazione();
		try {
			objResp = serv.getDettaglioElaborazione(cf, categoria, sottoCategoria, polizza, numeroPratica,
					dataRichiesta);
		} catch (Exception e) {
			resp.setErrorMessage("ATTENZIONE Errore nel recupero dati: " + e.getMessage());
			logger.error(e.getMessage(),e);
			return resp;
		}

		if (objResp == null || objResp != null && objResp.isEmpty())
			resp.setErrorMessage("nessun elemento trovato con i parametri CF= " + cf + " - categoria= " + categoria
					+ " - sottoCategoria= " + sottoCategoria);

		else {
			resp.setListOpr(objResp);
		}
		logger.info("terminata richiesta DetailOpr");
		return resp;

	}

	private boolean isOk(String numeroPratica, String categoria, String sottoCategoria) {
		if (numeroPratica != null && categoria != null && sottoCategoria != null)
			return true;
		if (numeroPratica == null && categoria == null && sottoCategoria == null)
			return true;
		else
			return false;

	}
//	@RequestMapping(value = "/DetailOpr", method = RequestMethod.GET)
//	public @ResponseBody RespElaborazione getDettaglioElaborazioneLess(@RequestParam String cf,
//			 @RequestParam String numeroPratica) {
//		return getDettaglioElaborazione(cf, null, null, numeroPratica);
//	}

	@RequestMapping(value = "/insertLastView", method = RequestMethod.PUT)
	public @ResponseBody SimpleResponse insertLastView(@RequestParam String cf) {
		logger.info("ricevuta richiesta per timestamp con " + cf);
		SimpleResponse resp = null;
		try {
			boolean update = serv.updateLastView(cf);
			resp = new SimpleResponse();
			resp.setExecute(update);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		if (resp == null) {
			resp = new SimpleResponse();
			resp.setExecute(false);
			resp.setMsg("Update cf non eseguito correttamente");

		}
		logger.info("terminata richiesta insertLastView");
		return resp;
	}

	@RequestMapping(value = "/sendDoc", method = RequestMethod.POST)
	public @ResponseBody SimpleResponse sendDoc(@RequestParam String polizza, @RequestParam String cf,
			@RequestParam String utente, @RequestBody MapStringObj filedata) {
		logger.info("ricevuta richiesta per sendDoc " + cf + " - " + polizza + " - " + utente);
		SimpleResponse resp = new SimpleResponse();
		try {
			boolean result = serv.sentDoc(cf, polizza, utente, filedata);
			resp.setExecute(result);
			resp.setMsg("documenti inviati correttamente");
		} catch (Exception e) {
			resp.setExecute(false);
			resp.setErrorMessage(e.getMessage());
			logger.error(e.getMessage(),e);
		}
		logger.info("terminata richiesta sendDoc");
		return resp;
	}

	@RequestMapping(value = "/listBen", method = RequestMethod.GET)
	public @ResponseBody RespBeneficiari listBen(@RequestParam String polizza) {
		logger.info("ricevuta richiesta per listBen " + polizza);
		RespBeneficiari resp = null;
		try {
			List<Beneficiario> listBen = serv.listBen(polizza);
			resp = new RespBeneficiari();
			resp.setList(listBen);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resp = new RespBeneficiari();
			resp.setErrorMessage(e.getMessage());
		}
		logger.info("terminata richiesta listBen");
		return resp;
	}

	@RequestMapping(value = "/listRefTer", method = RequestMethod.GET)
	public @ResponseBody RespReferenteTer listRefTer(@RequestParam String polizza) {
		logger.info("ricevuta richiesta per listRefTer " + polizza);
		RespReferenteTer resp = null;
		try {
			List<ReferenteTerzo> listRefTer = serv.listRefTer(polizza);
			resp = new RespReferenteTer();
			if (listRefTer.isEmpty()) {
				logger.info("nessun referente trovato per la polizza " + polizza);
				resp.setMsg("nessun referente trovato per la polizza " + polizza);
			} else {
				logger.info("trovati " + listRefTer.size() + " Referenti per la polizza " + polizza);
				resp.setList(listRefTer);
				resp.setMsg("trovati " + listRefTer.size() + " Referenti per la polizza " + polizza);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resp = new RespReferenteTer();
			resp.setErrorMessage(e.getMessage());
		}
		logger.info("terminata richiesta listRefTer");
		return resp;
	}

	@RequestMapping(value = "/updateRef", method = RequestMethod.PUT)
	public @ResponseBody SimpleResponse updateRef(@RequestBody ReferenteTerzo ref, @RequestParam String utente,
			TipoCrud tipoModifica) {
		logger.info("ricevuta richiesta per updateRef");
		SimpleResponse resp = null;
		try {
			if (ref == null)
				throw new Exception("Il soggetto inviato risulta essere nullo");
			if (tipoModifica == null)
				throw new Exception("il tipo modifica non può essere nullo");

			logger.info(
					"numRef: " + ref.getNumReferente() + " - utente: " + utente + " - tipoMod: " + tipoModifica.name());
			boolean execute = serv.updateRef(ref, tipoModifica.name(), utente);
			resp = new SimpleResponse();
			resp.setExecute(execute);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resp = new SimpleResponse();
			resp.setErrorMessage(e.getMessage());
		}
		logger.info("terminata richiesta updateRef");
		return resp;
	}

	@RequestMapping(value = "/updateBen", method = RequestMethod.PUT)
	public @ResponseBody SimpleResponse updateBef(@RequestBody Beneficiario ben, @RequestParam String utente,
			TipoCrud tipoModifica) {
		logger.info("ricevuta richiesta per updateBen");
		SimpleResponse resp = null;
		try {
			if (ben == null)
				throw new Exception("Il soggetto inviato risulta essere nullo");
			if (tipoModifica == null)
				throw new Exception("il tipo modifica non può essere nullo");

			logger.info("id: " + ben.getiD() + " - utente: " + utente + " - tipoMod: " + tipoModifica.name());
			boolean execute = serv.updateBen(ben, tipoModifica.name(), utente);
			resp = new SimpleResponse();
			resp.setExecute(execute);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resp = new SimpleResponse();
			resp.setErrorMessage(e.getMessage());
		}
		logger.info("terminata richiesta updateBen");
		return resp;
	}

	@RequestMapping(value = "/getFile", method = RequestMethod.GET)
	public @ResponseBody ResponseFile getFile(@RequestParam String polizza, @RequestParam String idFile) {
		logger.info("ricevuta richiesta per getFile " + polizza);
		byte[] pdf = null;
		try {
			pdf = serv.getFile(polizza, idFile);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		ResponseFile resp = new ResponseFile();
		resp.getListFile().add(pdf);
		logger.info("terminata richiesta getFile");
		return resp;
	}
	
	
	@RequestMapping(value = "/getPagamento", method = RequestMethod.GET)
	public @ResponseBody RespPagamento getPagamento(@RequestParam String polizza, @RequestParam String cf,@RequestParam String numeroPratica) {
		logger.info(String.format("ricevuta richiesta per getPagamento: polizza(%s) - cf(%s) - pratica(%s) ", polizza,cf,numeroPratica));
		RespPagamento resp=new RespPagamento();
		try {
			Pagamento pag = serv.getPagamento(cf, polizza, numeroPratica);
			resp.setPagamento(pag);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resp.setErrorMessage(e.getMessage());
		}
		
		return resp;
	
		
		
	}

}
