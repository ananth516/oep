package com.oracle.oep.order.presistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the OEP_MILESTONE_HISTORY database table.
 * 
 */
@Entity
@Table(name="OEP_MILESTONE_HISTORY")
@NamedQuery(name="OepMilestoneHistory.findAll", query="SELECT o FROM OepMilestoneHistory o")
public class OepMilestoneHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="LAST_MODIFIED_DATE")
	private Timestamp lastModifiedDate;

	@Column(name="MILESTONE_CODE")
	private String milestoneCode;

	@Column(name="MILESTONE_DESCRIPTION")
	private String milestoneDescription;

	@Id
	@SequenceGenerator(name="OEP_MILESTONE_ID_GENERATOR", sequenceName="OEP_MILESTONE_HISTORY_ID_SEQ", allocationSize =1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OEP_MILESTONE_ID_GENERATOR")
	@Column(name="MILESTONE_HIST_ID")
	private BigDecimal milestoneHistId;

	@Column(name="ORDER_LINE_ROWID",insertable=false, updatable=false)
	private String orderLineRowid;
	
	@Column(name="ORDER_LINE_ID",insertable=false, updatable=false)
	private String orderLineId;
	
	//bi-directional many-to-one association to OepOrderLine
	@ManyToOne
	@JoinColumn(name="ORDER_LINE_ROWID")
	private OepOrderLine oepOrderLine;

	public OepMilestoneHistory() {
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMilestoneCode() {
		return this.milestoneCode;
	}

	public void setMilestoneCode(String milestoneCode) {
		this.milestoneCode = milestoneCode;
	}

	public String getMilestoneDescription() {
		return this.milestoneDescription;
	}

	public void setMilestoneDescription(String milestoneDescription) {
		this.milestoneDescription = milestoneDescription;
	}

	public BigDecimal getMilestoneHistId() {
		return this.milestoneHistId;
	}

	public void setMilestoneHistId(BigDecimal milestoneHistId) {
		this.milestoneHistId = milestoneHistId;
	}
	


	public OepOrderLine getOepOrderLine() {
		return this.oepOrderLine;
	}

	public void setOepOrderLine(OepOrderLine oepOrderLine) {
		this.oepOrderLine = oepOrderLine;
	}

	public String getOrderLineRowid() {
		return this.orderLineRowid;
	}

	public void setOrderLineRowid(String orderLineRowid) {
		this.orderLineRowid = orderLineRowid;
	}

	public String getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	
	
}