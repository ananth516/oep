package com.oracle.oep.bulkprocessing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@XmlType


/**
 * The persistent class for the OEP_ORDER_LINES database table.
 * 
 */
@Entity
@Table(name="OEP_ORDER_LINES")
@NamedQuery(name="OepOrderLine.findAll", query="SELECT o FROM OepOrderLine o")
public class OepOrderLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OEP_ORDER_LINES_ORDER_LINE_ID_GENERATOR", sequenceName="ORDER_LINE_ROW_ID_SEQ", allocationSize =1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OEP_ORDER_LINES_ORDER_LINE_ID_GENERATOR")
/*    @GenericGenerator(name = "order_line_sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequenceName", value = "order_line_sequence"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "order_line_sequence", strategy=GenerationType.SEQUENCE)*/
	@Column(name="ORDER_LINE_ROWID")
	private int orderLineRowid;

	@Column(name="ACCOUNT_ID")
	private String accountId;

	@Column(name="EXPECTED_DELIVERY_DATE")
	private Timestamp expectedDeliveryDate;

	@Column(name="MILESTONE_CODE")
	private String milestoneCode;

	@Column(name="ORDER_LINE_ID")
	private String orderLineId;

	@Column(name="PARENT_ORDER_LINE_ID")
	private String parentOrderLineId;

	@Column(name="PLAN_ACTION")
	private String planAction;

	@Column(name="PLAN_NAME")
	private String planName;

	@Column(name="PRODUCT_SPECIFICATION")
	private String productSpecification;

	@Column(name="SERVICE_TYPE")
	private String serviceType;

	@Column(name="STATUS_CODE")
	private String statusCode;

	@Column(name="STATUS_DESCRIPTION")
	private String statusDescription;

	//bi-directional many-to-one association to OepOrder
	@ManyToOne
	@JoinColumn(name="ORDER_ROW_ID")
	private OepOrder oepOrder;

	public OepOrderLine() {
	}

	public int getOrderLineRowid() {
		return this.orderLineRowid;
	}

	public void setOrderLineRowid(int orderLineRowid) {
		this.orderLineRowid = orderLineRowid;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Timestamp getExpectedDeliveryDate() {
		return this.expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Timestamp expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public String getMilestoneCode() {
		return this.milestoneCode;
	}

	public void setMilestoneCode(String milestoneCode) {
		this.milestoneCode = milestoneCode;
	}

	public String getOrderLineId() {
		return this.orderLineId;
	}

	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

	public String getParentOrderLineId() {
		return this.parentOrderLineId;
	}

	public void setParentOrderLineId(String parentOrderLineId) {
		this.parentOrderLineId = parentOrderLineId;
	}

	public String getPlanAction() {
		return this.planAction;
	}

	public void setPlanAction(String planAction) {
		this.planAction = planAction;
	}

	public String getPlanName() {
		return this.planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getProductSpecification() {
		return this.productSpecification;
	}

	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return this.statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public OepOrder getOepOrder() {
		return this.oepOrder;
	}

	public void setOepOrder(OepOrder oepOrder) {
		this.oepOrder = oepOrder;
	}

}