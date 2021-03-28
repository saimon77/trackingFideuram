package com.fideuram.tracking.opr.dto.response;

import com.fideuram.tracking.opr.dto.Pagamento;

public class RespPagamento extends RespDto{
	
	private Pagamento pagamento;

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

}
