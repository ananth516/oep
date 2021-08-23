package com.oracle.oep.troubleticket.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the OAP_WORK_ORDER_MASTER_T database table.
 * 
 */
@Entity
@Table(name="OAP_WORK_ORDER_MASTER_T")
@NamedQuery(name="OapWorkOrderMasterT.findAll", query="SELECT o FROM OapWorkOrderMasterT o")
public class OapWorkOrderMasterT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OAP_WORK_ORDER_MASTER_T_WORKORDERID_GENERATOR", sequenceName="WORK_ORDER_MASTER_SEQ", allocationSize =1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OAP_WORK_ORDER_MASTER_T_WORKORDERID_GENERATOR")
	@Column(name="WORK_ORDER_ID")
	private long workOrderId;

	@Column(name="ACK_SLA_VIOLATED")
	private BigDecimal ackSlaViolated;

	@Column(name="CLOSED_BY")
	private String closedBy;

	@Column(name="CLOSED_ON")
	private Timestamp closedOn;

	@Column(name="COORDINATOR_ID")
	private String coordinatorId;

	@Column(name="COORDINATOR_REMARKS")
	private String coordinatorRemarks;

	@Column(name="CREATED_ON")
	private Timestamp createdOn;

	@Column(name="ENGINEER_ID")
	private String engineerId;

	@Column(name="ENGINEER_REMARKS")
	private String engineerRemarks;

	@Column(name="MODIFIED_LAST")
	private Timestamp modifiedLast;

	@Column(name="MODIFIED_LAST_BY")
	private String modifiedLastBy;

	@Column(name="QUEUE_ID")
	private BigDecimal queueId;

	@Column(name="RES_SLA_VIOLATED")
	private BigDecimal resSlaViolated;

	private String status;

	@Column(name="TEAM_ID")
	private BigDecimal teamId;

	@Column(name="TRANSFERRED_ON")
	private Timestamp transferredOn;

	//bi-directional many-to-one association to OapTicketUpdateHistoryT
	@OneToMany(mappedBy="oapWorkOrderMasterT")
	private List<OapTicketUpdateHistoryT> oapTicketUpdateHistoryTs;

	//bi-directional many-to-one association to OapCallLogMasterT
	@ManyToOne
	@JoinColumn(name="CUS_LOG_ID")
	private OapCallLogMasterT oapCallLogMasterT;

	public OapWorkOrderMasterT() {
	}

	public long getWorkOrderId() {
		return this.workOrderId;
	}

	public void setWorkOrderId(long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public BigDecimal getAckSlaViolated() {
		return this.ackSlaViolated;
	}

	public void setAckSlaViolated(BigDecimal ackSlaViolated) {
		this.ackSlaViolated = ackSlaViolated;
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

	public String getCoordinatorRemarks() {
		return this.coordinatorRemarks;
	}

	public void setCoordinatorRemarks(String coordinatorRemarks) {
		this.coordinatorRemarks = coordinatorRemarks;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getEngineerId() {
		return this.engineerId;
	}

	public void setEngineerId(String engineerId) {
		this.engineerId = engineerId;
	}

	public String getEngineerRemarks() {
		return this.engineerRemarks;
	}

	public void setEngineerRemarks(String engineerRemarks) {
		this.engineerRemarks = engineerRemarks;
	}

	public Timestamp getModifiedLast() {
		return this.modifiedLast;
	}

	public void setModifiedLast(Timestamp modifiedLast) {
		this.modifiedLast = modifiedLast;
	}

	public String getModifiedLastBy() {
		return this.modifiedLastBy;
	}

	public void setModifiedLastBy(String modifiedLastBy) {
		this.modifiedLastBy = modifiedLastBy;
	}

	public BigDecimal getQueueId() {
		return this.queueId;
	}

	public void setQueueId(BigDecimal queueId) {
		this.queueId = queueId;
	}

	public BigDecimal getResSlaViolated() {
		return this.resSlaViolated;
	}

	public void setResSlaViolated(BigDecimal resSlaViolated) {
		this.resSlaViolated = resSlaViolated;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTeamId() {
		return this.teamId;
	}

	public void setTeamId(BigDecimal teamId) {
		this.teamId = teamId;
	}

	public Timestamp getTransferredOn() {
		return this.transferredOn;
	}

	public void setTransferredOn(Timestamp transferredOn) {
		this.transferredOn = transferredOn;
	}

	public List<OapTicketUpdateHistoryT> getOapTicketUpdateHistoryTs() {
		return this.oapTicketUpdateHistoryTs;
	}

	public void setOapTicketUpdateHistoryTs(List<OapTicketUpdateHistoryT> oapTicketUpdateHistoryTs) {
		this.oapTicketUpdateHistoryTs = oapTicketUpdateHistoryTs;
	}

	public OapTicketUpdateHistoryT addOapTicketUpdateHistoryT(OapTicketUpdateHistoryT oapTicketUpdateHistoryT) {
		getOapTicketUpdateHistoryTs().add(oapTicketUpdateHistoryT);
		oapTicketUpdateHistoryT.setOapWorkOrderMasterT(this);

		return oapTicketUpdateHistoryT;
	}

	public OapTicketUpdateHistoryT removeOapTicketUpdateHistoryT(OapTicketUpdateHistoryT oapTicketUpdateHistoryT) {
		getOapTicketUpdateHistoryTs().remove(oapTicketUpdateHistoryT);
		oapTicketUpdateHistoryT.setOapWorkOrderMasterT(null);

		return oapTicketUpdateHistoryT;
	}

	public OapCallLogMasterT getOapCallLogMasterT() {
		return this.oapCallLogMasterT;
	}

	public void setOapCallLogMasterT(OapCallLogMasterT oapCallLogMasterT) {
		this.oapCallLogMasterT = oapCallLogMasterT;
	}

}