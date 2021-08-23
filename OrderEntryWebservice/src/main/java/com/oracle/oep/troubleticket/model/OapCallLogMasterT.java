package com.oracle.oep.troubleticket.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the OAP_CALL_LOG_MASTER_T database table.
 * 
 */
@Entity
@Table(name="OAP_CALL_LOG_MASTER_T")
@NamedQuery(name="OapCallLogMasterT.findAll", query="SELECT o FROM OapCallLogMasterT o")
public class OapCallLogMasterT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OAP_CALL_LOG_MASTER_T_CUSLOGID_GENERATOR", sequenceName="CALL_LOG_MASTER_SEQ", allocationSize =1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OAP_CALL_LOG_MASTER_T_CUSLOGID_GENERATOR")
	@Column(name="CUS_LOG_ID")
	private long cusLogId;

	@Column(name="ACTION_ID")
	private BigDecimal actionId;

	@Column(name="ALT_MOBILE_NUMBER")
	private BigDecimal altMobileNumber;

	@Column(name="AREA_ID")
	private BigDecimal areaId;

	@Column(name="BILLING_ACC_NO")
	private String billingAccNo;

	@Column(name="CALL_S")
	private BigDecimal callS;

	@Column(name="CALLBACK_REQUIRED")
	private BigDecimal callbackRequired;

	@Column(name="CATEGORY_ID")
	private BigDecimal categoryId;

	@Column(name="CLASSIFIED_AS")
	private String classifiedAs;

	private Timestamp created;

	@Column(name="CSR_LOG_REMARKS")
	private String csrLogRemarks;

	@Column(name="CUS_FIRST_NAME")
	private String cusFirstName;

	@Column(name="CUS_LAST_NAME")
	private String cusLastName;

	@Column(name="CUST_ACC_NO")
	private String custAccNo;

	@Column(name="DEALER_SHOP_CODE")
	private String dealerShopCode;

	@Column(name="DEALER_SHOP_NAME")
	private String dealerShopName;

	@Column(name="DEVICE_DETAILS")
	private String deviceDetails;

	private Timestamp modified;

	@Column(name="ORDER_FAILURE_AREA")
	private String orderFailureArea;

	@Column(name="ORDER_FAILURE_CODE")
	private String orderFailureCode;

	@Column(name="ORDER_FAILURE_DESC")
	private String orderFailureDesc;

	@Column(name="ORDER_FAILURE_SYSTEM")
	private String orderFailureSystem;

	@Column(name="ORDER_MATERIAL_TYPE")
	private String orderMaterialType;

	@Column(name="ORDERED_QUANTITY")
	private BigDecimal orderedQuantity;

	@Column(name="PREFERRED_TIME")
	private Timestamp preferredTime;

	private String priority;

	@Column(name="REG_MOBILE_NUMBER")
	private BigDecimal regMobileNumber;

	@Column(name="RESOLUTION_ID")
	private BigDecimal resolutionId;

	@Column(name="SERV_ACC_NO")
	private String servAccNo;

	@Column(name="SERVICE_GROUP")
	private String serviceGroup;

	@Column(name="SERVICE_NO")
	private String serviceNo;

	@Column(name="SERVICE_ORDER_ID")
	private String serviceOrderId;

	@Column(name="SERVICE_TYPE")
	private String serviceType;

	@Column(name="SUB_CAT_ID")
	private BigDecimal subCatId;

	@Column(name="TYPE_ID")
	private BigDecimal typeId;

	@Column(name="USER_ID")
	private String userId;

	//bi-directional many-to-one association to OapTicketUpdateHistoryT
	@OneToMany(mappedBy="oapCallLogMasterT")
	private List<OapTicketUpdateHistoryT> oapTicketUpdateHistoryTs;

	//bi-directional many-to-one association to OapWorkOrderMasterT
	@OneToMany(mappedBy="oapCallLogMasterT")
	private List<OapWorkOrderMasterT> oapWorkOrderMasterTs;

	public OapCallLogMasterT() {
	}

	public long getCusLogId() {
		return this.cusLogId;
	}

	public void setCusLogId(long cusLogId) {
		this.cusLogId = cusLogId;
	}

	public BigDecimal getActionId() {
		return this.actionId;
	}

	public void setActionId(BigDecimal actionId) {
		this.actionId = actionId;
	}

	public BigDecimal getAltMobileNumber() {
		return this.altMobileNumber;
	}

	public void setAltMobileNumber(BigDecimal altMobileNumber) {
		this.altMobileNumber = altMobileNumber;
	}

	public BigDecimal getAreaId() {
		return this.areaId;
	}

	public void setAreaId(BigDecimal areaId) {
		this.areaId = areaId;
	}

	public String getBillingAccNo() {
		return this.billingAccNo;
	}

	public void setBillingAccNo(String billingAccNo) {
		this.billingAccNo = billingAccNo;
	}

	public BigDecimal getCallS() {
		return this.callS;
	}

	public void setCallS(BigDecimal callS) {
		this.callS = callS;
	}

	public BigDecimal getCallbackRequired() {
		return this.callbackRequired;
	}

	public void setCallbackRequired(BigDecimal callbackRequired) {
		this.callbackRequired = callbackRequired;
	}

	public BigDecimal getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(BigDecimal categoryId) {
		this.categoryId = categoryId;
	}

	public String getClassifiedAs() {
		return this.classifiedAs;
	}

	public void setClassifiedAs(String classifiedAs) {
		this.classifiedAs = classifiedAs;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getCsrLogRemarks() {
		return this.csrLogRemarks;
	}

	public void setCsrLogRemarks(String csrLogRemarks) {
		this.csrLogRemarks = csrLogRemarks;
	}

	public String getCusFirstName() {
		return this.cusFirstName;
	}

	public void setCusFirstName(String cusFirstName) {
		this.cusFirstName = cusFirstName;
	}

	public String getCusLastName() {
		return this.cusLastName;
	}

	public void setCusLastName(String cusLastName) {
		this.cusLastName = cusLastName;
	}

	public String getCustAccNo() {
		return this.custAccNo;
	}

	public void setCustAccNo(String custAccNo) {
		this.custAccNo = custAccNo;
	}

	public String getDealerShopCode() {
		return this.dealerShopCode;
	}

	public void setDealerShopCode(String dealerShopCode) {
		this.dealerShopCode = dealerShopCode;
	}

	public String getDealerShopName() {
		return this.dealerShopName;
	}

	public void setDealerShopName(String dealerShopName) {
		this.dealerShopName = dealerShopName;
	}

	public String getDeviceDetails() {
		return this.deviceDetails;
	}

	public void setDeviceDetails(String deviceDetails) {
		this.deviceDetails = deviceDetails;
	}

	public Timestamp getModified() {
		return this.modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public String getOrderFailureArea() {
		return this.orderFailureArea;
	}

	public void setOrderFailureArea(String orderFailureArea) {
		this.orderFailureArea = orderFailureArea;
	}

	public String getOrderFailureCode() {
		return this.orderFailureCode;
	}

	public void setOrderFailureCode(String orderFailureCode) {
		this.orderFailureCode = orderFailureCode;
	}

	public String getOrderFailureDesc() {
		return this.orderFailureDesc;
	}

	public void setOrderFailureDesc(String orderFailureDesc) {
		this.orderFailureDesc = orderFailureDesc;
	}

	public String getOrderFailureSystem() {
		return this.orderFailureSystem;
	}

	public void setOrderFailureSystem(String orderFailureSystem) {
		this.orderFailureSystem = orderFailureSystem;
	}

	public String getOrderMaterialType() {
		return this.orderMaterialType;
	}

	public void setOrderMaterialType(String orderMaterialType) {
		this.orderMaterialType = orderMaterialType;
	}

	public BigDecimal getOrderedQuantity() {
		return this.orderedQuantity;
	}

	public void setOrderedQuantity(BigDecimal orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public Timestamp getPreferredTime() {
		return this.preferredTime;
	}

	public void setPreferredTime(Timestamp preferredTime) {
		this.preferredTime = preferredTime;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public BigDecimal getRegMobileNumber() {
		return this.regMobileNumber;
	}

	public void setRegMobileNumber(BigDecimal regMobileNumber) {
		this.regMobileNumber = regMobileNumber;
	}

	public BigDecimal getResolutionId() {
		return this.resolutionId;
	}

	public void setResolutionId(BigDecimal resolutionId) {
		this.resolutionId = resolutionId;
	}

	public String getServAccNo() {
		return this.servAccNo;
	}

	public void setServAccNo(String servAccNo) {
		this.servAccNo = servAccNo;
	}

	public String getServiceGroup() {
		return this.serviceGroup;
	}

	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	public String getServiceNo() {
		return this.serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	public String getServiceOrderId() {
		return this.serviceOrderId;
	}

	public void setServiceOrderId(String serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public BigDecimal getSubCatId() {
		return this.subCatId;
	}

	public void setSubCatId(BigDecimal subCatId) {
		this.subCatId = subCatId;
	}

	public BigDecimal getTypeId() {
		return this.typeId;
	}

	public void setTypeId(BigDecimal typeId) {
		this.typeId = typeId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<OapTicketUpdateHistoryT> getOapTicketUpdateHistoryTs() {
		return this.oapTicketUpdateHistoryTs;
	}

	public void setOapTicketUpdateHistoryTs(List<OapTicketUpdateHistoryT> oapTicketUpdateHistoryTs) {
		this.oapTicketUpdateHistoryTs = oapTicketUpdateHistoryTs;
	}

	public OapTicketUpdateHistoryT addOapTicketUpdateHistoryT(OapTicketUpdateHistoryT oapTicketUpdateHistoryT) {
		getOapTicketUpdateHistoryTs().add(oapTicketUpdateHistoryT);
		oapTicketUpdateHistoryT.setOapCallLogMasterT(this);

		return oapTicketUpdateHistoryT;
	}

	public OapTicketUpdateHistoryT removeOapTicketUpdateHistoryT(OapTicketUpdateHistoryT oapTicketUpdateHistoryT) {
		getOapTicketUpdateHistoryTs().remove(oapTicketUpdateHistoryT);
		oapTicketUpdateHistoryT.setOapCallLogMasterT(null);

		return oapTicketUpdateHistoryT;
	}

	public List<OapWorkOrderMasterT> getOapWorkOrderMasterTs() {
		return this.oapWorkOrderMasterTs;
	}

	public void setOapWorkOrderMasterTs(List<OapWorkOrderMasterT> oapWorkOrderMasterTs) {
		this.oapWorkOrderMasterTs = oapWorkOrderMasterTs;
	}

	public OapWorkOrderMasterT addOapWorkOrderMasterT(OapWorkOrderMasterT oapWorkOrderMasterT) {
		getOapWorkOrderMasterTs().add(oapWorkOrderMasterT);
		oapWorkOrderMasterT.setOapCallLogMasterT(this);

		return oapWorkOrderMasterT;
	}

	public OapWorkOrderMasterT removeOapWorkOrderMasterT(OapWorkOrderMasterT oapWorkOrderMasterT) {
		getOapWorkOrderMasterTs().remove(oapWorkOrderMasterT);
		oapWorkOrderMasterT.setOapCallLogMasterT(null);

		return oapWorkOrderMasterT;
	}

}