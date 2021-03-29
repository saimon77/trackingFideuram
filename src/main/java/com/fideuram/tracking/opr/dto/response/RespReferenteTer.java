package com.fideuram.tracking.opr.dto.response;

import java.util.List;

import com.fideuram.tracking.opr.dto.ReferenteTerzo;

public class RespReferenteTer extends RespDto{
	
	private List<ReferenteTerzo> list;

	public List<ReferenteTerzo> getList() {
		return list;
	}

	public void setList(List<ReferenteTerzo> list) {
		this.list = list;
	}


}
