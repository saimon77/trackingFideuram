package com.fideuram.tracking.opr.dto.response;

import java.util.List;

import com.fideuram.tracking.opr.dto.Beneficiario;

public class RespBeneficiari extends RespDto{
	
	private List<Beneficiario> list;
	

	public List<Beneficiario> getList() {
		return list;
	}

	public void setList(List<Beneficiario> list) {
		this.list = list;
	}


}
