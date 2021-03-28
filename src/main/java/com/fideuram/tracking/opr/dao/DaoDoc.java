package com.fideuram.tracking.opr.dao;

import java.util.List;

import com.fideuram.tracking.opr.dto.Documento;

public interface DaoDoc {
	
	
	public List<Documento> getDocumentiInviatPerPratica(String numPratica,String c_prodotto) throws Exception ;
	
	
	public List<Documento> getDocumentiTotPerPratica(String numPratica) throws Exception ;
		
}
