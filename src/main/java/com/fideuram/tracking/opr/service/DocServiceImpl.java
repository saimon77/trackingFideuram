package com.fideuram.tracking.opr.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fideuram.tracking.opr.dao.DaoDoc;
import com.fideuram.tracking.opr.dto.Documento;

@Service
public class DocServiceImpl implements DocService {

	@Autowired
	DaoDoc dao;

	public List<Documento> getDocmentiMancantiPerPratica(String numPratica, String c_prodotto) throws Exception {

		List<Documento> docTotali = dao.getDocumentiTotPerPratica(numPratica);
		if (docTotali.isEmpty())
			return null;
		List<Documento> docInviati = getDocmentiInviatiPerPratica(numPratica, c_prodotto);
		List<Documento> docDaInviare = new ArrayList<Documento>();

		for (Documento doc : docTotali) {
			if (!docIsPresent(docInviati, doc))
				docDaInviare.add(doc);
		}
		Collections.sort(docDaInviare, new Comparator<Documento>() {

			public int compare(Documento o1, Documento o2) {
				return Integer.parseInt(o1.getC_documento()) - Integer.parseInt(o2.getC_documento());
			}
		});
		return docDaInviare;

	}

	private boolean docIsPresent(List<Documento> list, Documento doc) {
		for (Documento d : list) {
			if (d.getC_documento().equals(doc.getC_documento()))
				return true;

		}
		return false;
	}

	@Override
	public List<Documento> getDocmentiInviatiPerPratica(String numPratica, String c_prodotto) throws Exception {
		return dao.getDocumentiInviatPerPratica(numPratica, c_prodotto);
	}
	
	

}
