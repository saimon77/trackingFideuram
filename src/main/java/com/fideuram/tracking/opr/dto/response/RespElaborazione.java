package com.fideuram.tracking.opr.dto.response;

import java.util.List;

import com.fideuram.tracking.opr.dto.Agenda;
import com.fideuram.tracking.opr.dto.Operazione;

public class RespElaborazione extends RespDto {

	private List<Operazione> listOpr;
	private Agenda agenda;

	public List<Operazione> getListOpr() {
		return listOpr;
	}

	public void setListOpr(List<Operazione> listOpr) {
		this.listOpr = listOpr;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

}
