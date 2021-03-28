package com.fideuram.tracking.opr.service;

import java.util.List;

import com.fideuram.tracking.opr.dto.Documento;

public interface DocService {
	
	
	public List<Documento> getDocmentiMancantiPerPratica(String numPratica,String c_prodotto) throws Exception ;
	public List<Documento> getDocmentiInviatiPerPratica(String numPratica,String c_prodotto) throws Exception ;
		
}
