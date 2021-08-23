package com.oracle.oep.troubleticket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OapTicketUpdateHistoryKeys implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="CUS_LOG_ID",insertable=false, updatable=false)
	private long cusLogId;

	@Column(name="WORK_ORDER_ID",insertable=false, updatable=false)
	private long workOrderId;

	public long getCusLogId() {
		return this.cusLogId;
	}

	public void setCusLogId(long cusLogId) {
		this.cusLogId = cusLogId;
	}

	public long getWorkOrderId() {
		return this.workOrderId;
	}

	public void setWorkOrderId(long workOrderId) {
		this.workOrderId = workOrderId;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(getCusLogId(), getWorkOrderId());
    }
	
 
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OapTicketUpdateHistoryKeys other = (OapTicketUpdateHistoryKeys) obj;
		
		return Objects.equals(getCusLogId(), other.getCusLogId()) &&
				Objects.equals(getWorkOrderId(), other.getWorkOrderId());
	} 
	

}
