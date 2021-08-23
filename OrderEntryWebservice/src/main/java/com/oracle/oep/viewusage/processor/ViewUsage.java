package com.oracle.oep.viewusage.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.oracle.oep.viewusage.xsd.USAGERECORDS;

public class ViewUsage {
	
	private String networkSessionid = "";
	
	private String usageType = "";
	
	private String unitName = "";
	
	private Long startT;
	
	private Long endT;
	
	private String originSid = "";
	
	private String locAreaCode = "";
	
	private String terminateCause = "";
	
	private String calledTo = "";
	
	private Long quantity;
	
	private String impactCategory = "";
	
	private BigDecimal amount;
	
	private String eventNo = "";
	
	private String descr = "";
	
	private String ebiResourceId = "";
	
	private Long ebiQty;
	
	private BigDecimal ebiAmt;
	
	private String offerName = "";
	
	private List<UsageCharges> chargesList;
	
	private String usageClass = "";

	public String getNetworkSessionid() {
		return networkSessionid;
	}

	public void setNetworkSessionid(String networkSessionid) {
		this.networkSessionid = networkSessionid;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getStartT() {
		return startT;
	}

	public void setStartT(Long startT) {
		this.startT = startT;
	}

	public Long getEndT() {
		return endT;
	}

	public void setEndT(Long endT) {
		this.endT = endT;
	}

	public String getOriginSid() {
		return originSid;
	}

	public void setOriginSid(String originSid) {
		this.originSid = originSid;
	}

	public String getLocAreaCode() {
		return locAreaCode;
	}

	public void setLocAreaCode(String locAreaCode) {
		this.locAreaCode = locAreaCode;
	}

	public String getTerminateCause() {
		return terminateCause;
	}

	public void setTerminateCause(String terminateCause) {
		this.terminateCause = terminateCause;
	}

	public String getCalledTo() {
		return calledTo;
	}

	public void setCalledTo(String calledTo) {
		this.calledTo = calledTo;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getImpactCategory() {
		return impactCategory;
	}

	public void setImpactCategory(String impactCategory) {
		this.impactCategory = impactCategory;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getEbiResourceId() {
		return ebiResourceId;
	}

	public void setEbiResourceId(String ebiResourceId) {
		this.ebiResourceId = ebiResourceId;
	}

	public Long getEbiQty() {
		return ebiQty;
	}

	public void setEbiQty(Long ebiQty) {
		this.ebiQty = ebiQty;
	}

	public BigDecimal getEbiAmt() {
		return ebiAmt;
	}

	public void setEbiAmt(BigDecimal ebiAmt) {
		this.ebiAmt = ebiAmt;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	
	public void addCharges (UsageCharges charge) {
		this.chargesList.add(charge);
	}
	
    public List<UsageCharges> getChargesList() {
        if (chargesList == null) {
        	chargesList = new ArrayList<UsageCharges>();
        }
        return this.chargesList;
    }

	public String getUsageClass() {
		return usageClass;
	}

	public void setUsageClass(String usageClass) {
		this.usageClass = usageClass;
	}
	
    

}
