package com.oracle.oep.troubleticket.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the OAP_TICKET_UPDATE_HISTORY_T database table.
 * 
 */
@Entity
@Table(name="OAP_TICKET_UPDATE_HISTORY_T")
@NamedQuery(name="OapTicketUpdateHistoryT.findAll", query="SELECT o FROM OapTicketUpdateHistoryT o")
public class OapTicketUpdateHistoryT implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private OapTicketUpdateHistoryKeys id;
	
	@Column(name="ACK_SLA_VIOLATED")
	private int ackSlaViolated;

	@Column(name="ACTION_ID")
	private long actionId;

	@Column(name="AREA_ID")
	private long areaId;

	@Column(name="CALL_S")
	private int callS;

	@Column(name="CALLBACK_REQUIRED")
	private int callbackRequired;

	@Column(name="CATEGORY_ID")
	private long categoryId;

	@Column(name="CLOSED_BY")
	private String closedBy;

	@Column(name="CLOSED_ON")
	private Timestamp closedOn;

	@Column(name="COORDINATOR_ID")
	private String coordinatorId;
	
	@Column(name="DEALER_SHOP_CODE")
	private String dealerShopCode;

	@Column(name="DEALER_SHOP_NAME")
	private String dealerShopName;

	@Column(name="DEVICE_DETAILS")
	private String deviceDetails;

	@Column(name="ENGINEER_ID")
	private String engineerId;

	@Column(name="MODIFIED_LAST_BY")
	private String modifiedLastBy;

	@Column(name="ORDER_MATERIAL_TYPE")
	private String orderMaterialType;

	@Column(name="ORDERED_QUANTITY")
	private long orderedQuantity;

	@Column(name="PREFERRED_TIME")
	private Timestamp preferredTime;

	private String priority;

	@Column(name="QUEUE_ID")
	private long queueId;

	@Column(name="RES_SLA_VIOLATED")
	private int resSlaViolated;

	@Column(name="RESOLUTION_ID")
	private int resolutionId;

	private String status;

	@Column(name="SUB_CAT_ID")
	private long subCatId;

	@Column(name="TEAM_ID")
	private long teamId;

	@Column(name="TRANSFERRED_ON")
	private Timestamp transferredOn;

	@Column(name="TYPE_ID")
	private long typeId;

	@Column(name="UPDATE_REMARKS")
	private String updateRemarks;

	@Column(name="USER_ID")
	private String userId;

	@Column(name="USER_TYPE")
	private String userType;

	@Column(name="WO_MODIFIED_LAST")
	private Timestamp woModifiedLast;
	
	//bi-directional many-to-one association to OapCallLogMasterT
	@ManyToOne
	@JoinColumn(name="CUS_LOG_ID")
	private OapCallLogMasterT oapCallLogMasterT;

	//bi-directional many-to-one association to OapWorkOrderMasterT
	@ManyToOne
	@JoinColumn(name="WORK_ORDER_ID",insertable=true, updatable=false)
	private OapWorkOrderMasterT oapWorkOrderMasterT;

	public OapTicketUpdateHistoryT() {
	}

	public int getAckSlaViolated() {
		return this.ackSlaViolated;
	}

	public void setAckSlaViolated(int ackSlaViolated) {
		this.ackSlaViolated = ackSlaViolated;
	}

	public long getActionId() {
		return this.actionId;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	public long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public int getCallS() {
		return this.callS;
	}

	public void setCallS(int callS) {
		this.callS = callS;
	}

	public int getCallbackRequired() {
		return this.callbackRequired;
	}

	public void setCallbackRequired(int callbackRequired) {
		this.callbackRequired = callbackRequired;
	}

	public long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getClosedBy() {
		return this.closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public Timestamp getClosedOn() {
		return this.closedOn;
	}

	public void setClosedOn(Timestamp closedOn) {
		this.closedOn = closedOn;
	}

	public String getCoordinatorId() {
		return this.coordinatorId;
	}

	public void setCoordinatorId(String coordinatorId) {
		this.coordinatorId = coordinatorId;
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

	public String getEngineerId() {
		return this.engineerId;
	}

	public void setEngineerId(String engineerId) {
		this.engineerId = engineerId;
	}

	public String getModifiedLastBy() {
		return this.modifiedLastBy;
	}

	public void setModifiedLastBy(String modifiedLastBy) {
		this.modifiedLastBy = modifiedLastBy;
	}

	public String getOrderMaterialType() {
		return this.orderMaterialType;
	}

	public void setOrderMaterialType(String orderMaterialType) {
		this.orderMaterialType = orderMaterialType;
	}

	public long getOrderedQuantity() {
		return this.orderedQuantity;
	}

	public void setOrderedQuantity(long orderedQuantity) {
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

	public long getQueueId() {
		return this.queueId;
	}

	public void setQueueId(long queueId) {
		this.queueId = queueId;
	}

	public int getResSlaViolated() {
		return this.resSlaViolated;
	}

	public void setResSlaViolated(int resSlaViolated) {
		this.resSlaViolated = resSlaViolated;
	}

	public int getResolutionId() {
		return this.resolutionId;
	}

	public void setResolutionId(int resolutionId) {
		this.resolutionId = resolutionId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getSubCatId() {
		return this.subCatId;
	}

	public void setSubCatId(long subCatId) {
		this.subCatId = subCatId;
	}

	public long getTeamId() {
		return this.teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public Timestamp getTransferredOn() {
		return this.transferredOn;
	}

	public void setTransferredOn(Timestamp transferredOn) {
		this.transferredOn = transferredOn;
	}

	public long getTypeId() {
		return this.typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public String getUpdateRemarks() {
		return this.updateRemarks;
	}

	public void setUpdateRemarks(String updateRemarks) {
		this.updateRemarks = updateRemarks;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Timestamp getWoModifiedLast() {
		return this.woModifiedLast;
	}

	public void setWoModifiedLast(Timestamp woModifiedLast) {
		this.woModifiedLast = woModifiedLast;
	}

	public OapCallLogMasterT getOapCallLogMasterT() {
		return this.oapCallLogMasterT;
	}

	public void setOapCallLogMasterT(OapCallLogMasterT oapCallLogMasterT) {
		this.oapCallLogMasterT = oapCallLogMasterT;
	}

	public OapWorkOrderMasterT getOapWorkOrderMasterT() {
		return this.oapWorkOrderMasterT;
	}

	public void setOapWorkOrderMasterT(OapWorkOrderMasterT oapWorkOrderMasterT) {
		this.oapWorkOrderMasterT = oapWorkOrderMasterT;
	}
	
	public OapTicketUpdateHistoryKeys getId() {
		return id;
	}

	public void setId(OapTicketUpdateHistoryKeys id) {
		this.id = id;
	}


}