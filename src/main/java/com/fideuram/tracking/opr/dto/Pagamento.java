package com.fideuram.tracking.opr.dto;

import java.math.BigDecimal;

public class Pagamento {
	
	private BigDecimal importoRichiesto;

	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}
	
	public interface Campi{
		public final static String ImportoNetto="ImportoNetto";
	}

}
