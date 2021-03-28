package com.fideuram.tracking.opr.restController;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fideuram.tracking.opr.dao.DaoOprImpl;
import com.fideuram.tracking.opr.dto.Documento;
import com.fideuram.tracking.opr.dto.Operazione;
import com.fideuram.tracking.opr.dto.response.RespDocumenti;
import com.fideuram.tracking.opr.dto.response.RespElaborazione;
import com.fideuram.tracking.opr.service.DocService;

@Controller
@RequestMapping(value="/fide")
public class Fide_PrudController {

	public Logger logger = Logger.getLogger(Fide_PrudController.class);

	@Autowired
	DocService serv;
	
	@RequestMapping(value = "/docDaInviare", method = RequestMethod.GET)
	public @ResponseBody RespDocumenti getDocumenti(@RequestParam String numPratica,@RequestParam String c_prodotto) {
		logger.info("ricevuta richiesta per getDocumenti con pratica: " + numPratica +" - prodotto: "+c_prodotto);
		List<Documento> list;
		RespDocumenti resp=new RespDocumenti();
		try {
			list = serv.getDocmentiMancantiPerPratica(numPratica,c_prodotto);
		} catch (Exception e) {
			resp.setErrorMessage("Errore nella chiamata: "+e.getMessage());
			return resp;
		}
		if(list==null) {
			resp.setErrorMessage("Attenzione non sono stati trovati documenti da inviare per la pratica: "+numPratica);
		return resp;	
		}else if(list.isEmpty())
			resp.setErrorMessage("Non ci sono ulteriori documenti da inviare");
		else
			resp.setListDoc(list);
		return resp;
	}
	
	@RequestMapping(value = "/docInviati", method = RequestMethod.GET)
	public @ResponseBody RespDocumenti getDocumentiInviati(@RequestParam String numPratica,@RequestParam String c_prodotto) {
		logger.info("ricevuta richiesta per getDocumentiInviati con pratica: " + numPratica +" - prodotto: "+c_prodotto);
		List<Documento> list;
		RespDocumenti resp=new RespDocumenti();
		try {
			list = serv.getDocmentiInviatiPerPratica(numPratica,c_prodotto);
		} catch (Exception e) {
			resp.setErrorMessage("Errore nella chiamata: "+e.getMessage());
			return resp;
		}
		if(list==null) {
			resp.setErrorMessage("Attenzione non sono stati trovati documenti da inviare per la pratica: "+numPratica);
			return resp;	
		}else if(list.isEmpty())
			resp.setErrorMessage("Non ci sono ulteriori documenti da inviare");
		else
			resp.setListDoc(list);
		return resp;
	}
}
