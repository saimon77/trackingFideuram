package com.fideuram.tracking.opr.dto.response;

import java.util.List;

import com.fideuram.tracking.opr.dto.Documento;
import com.fideuram.tracking.opr.dto.Operazione;

public class RespDocumenti extends RespDto{

	private List<Documento> listDoc;
	private List<Operazione> listOpr;


	public List<Documento> getListDoc() {
		return listDoc;
	}

	public void setListDoc(List<Documento> listDoc) {
		this.listDoc = listDoc;
	}


	public List<Operazione> getListOpr() {
		return listOpr;
	}

	public void setListOpr(List<Operazione> listOpr) {
		this.listOpr = listOpr;
	}

}
