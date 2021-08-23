package com.oracle.oep.viewusage.processor;

import java.math.BigDecimal;

public class UsageCharges {
	
	private Long quantity;
	
	private BigDecimal amount;
	
	private String resourceId = "";
	
	private String offerName = "";

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	
	
	

}
