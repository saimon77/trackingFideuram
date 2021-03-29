package com.fideuram.tracking.opr.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ResponseFile extends RespDto{

	private List<byte[]> listFile;

	public List<byte[]> getListFile() {
		if(listFile==null)
			listFile=new ArrayList<byte[]>();
		return listFile;
	}

	public void setListFile(List<byte[]> listFile) {
		this.listFile = listFile;
	}
}
