package com.oracle.oep.brm.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OEPConfigFeaturesKey implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="REC_ID")
	private BigDecimal recId;

	@Column(name="REC_ID2")
	private BigDecimal recId2;

	public BigDecimal getRecId() {
		return recId;
	}

	public void setRecId(BigDecimal recId) {
		this.recId = recId;
	}

	public BigDecimal getRecId2() {
		return recId2;
	}

	public void setRecId2(BigDecimal recId2) {
		this.recId2 = recId2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((recId2 == null) ? 0 : recId2.hashCode());
		result = prime * result + recId2.intValue();
		return result;
	}
 
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OEPConfigFeaturesKey other = (OEPConfigFeaturesKey) obj;
		if (recId == null) {
			if (other.recId != null)
				return false;
		} else if (!recId.equals(other.recId))
			return false;
		if (recId2 != other.recId2)
			return false;
		return true;
	} 
	

}
