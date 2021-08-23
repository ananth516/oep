package com.oracle.oep.bulkprocessing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@XmlType


/**
 * The persistent class for the OEP_ORDER database table.
 * 
 */
@Entity
@Table(name="OEP_ORDER")
@NamedQuery(name="OepOrder.findAll", query="SELECT o FROM OepOrder o")
public class OepOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OEP_ORDER_ORDER_ROW_ID_GENERATOR", sequenceName="ORDER_ROW_ID_SEQ", allocationSize =1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OEP_ORDER_ORDER_ROW_ID_GENERATOR")	
/*    @GenericGenerator(name = "order_sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequenceName", value = "order_sequence"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "order_sequence", strategy=GenerationType.SEQUENCE)*/
	@Column(name="ORDER_ROW_ID")
	private Long orderRowId;

	@Column(name="\"ACTION\"")
	private String action;

	@Column(name="CHANNEL_NAME")
	private String channelName;

	private String csrid;

	@Column(name="IS_FUTURE_DATE")
	private String isFutureDate;

	@Column(name="IS_PARK")
	private String isPark;

	@Column(name="ORDER_END_DATE")
	private Timestamp orderEndDate;

	@Column(name="ORDER_ID")
	private String orderId;

	@Column(name="ORDER_START_DATE")
	private Timestamp orderStartDate;

	@Lob
	@Column(name="PAYLOAD_XML")
	private String payloadXml;

	@Column(name="POLLING_STATUS")
	private String pollingStatus;

	@Column(name="SERVICE_NO")
	private String serviceNo;

	@Column(name="STATUS_CODE")
	private String statusCode;

	@Column(name="STATUS_DESCRIPTION")
	private String statusDescription;
	
	@Column(name="BATCH_ID",insertable=false, updatable=false)
	private Integer batchId;

	//bi-directional many-to-one association to BulkBatchMaster
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private BulkBatchMaster bulkBatchMaster;

	//bi-directional many-to-one association to OepOrderLine
	@OneToMany(mappedBy="oepOrder", cascade=CascadeType.ALL)
	private List<OepOrderLine> oepOrderLines = new ArrayList<OepOrderLine>();
	
	@Column(name="ORDER_CREATION_DATE")
	private Timestamp orderCreationDate;	
	
	@Column(name="LAST_MODIFIED_DATE")
	private Timestamp lastModifiedDate;
	
	@Lob
	@Column(name="OSM_PAYLOAD_XML")
	private String osmPayloadXml;
	
	@Column(name="BA_ACCOUNT_NO")
	private String baAccountNo;

	public Timestamp getOrderCreationDate() {
		return orderCreationDate;
	}

	public void setOrderCreationDate(Timestamp orderCreationDate) {
		this.orderCreationDate = orderCreationDate;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getOsmPayloadXml() {
		return osmPayloadXml;
	}

	public void setOsmPayloadXml(String osmPayloadXml) {
		this.osmPayloadXml = osmPayloadXml;
	}

	public String getBaAccountNo() {
		return baAccountNo;
	}

	public void setBaAccountNo(String baAccountNo) {
		this.baAccountNo = baAccountNo;
	}

	public OepOrder() {
	}

	public Long getOrderRowId() {
		return this.orderRowId;
	}

	public void setOrderRowId(Long orderRowId) {
		this.orderRowId = orderRowId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCsrid() {
		return this.csrid;
	}

	public void setCsrid(String csrid) {
		this.csrid = csrid;
	}

	public String getIsFutureDate() {
		return this.isFutureDate;
	}

	public void setIsFutureDate(String isFutureDate) {
		this.isFutureDate = isFutureDate;
	}

	public String getIsPark() {
		return this.isPark;
	}

	public void setIsPark(String isPark) {
		this.isPark = isPark;
	}

	public Timestamp getOrderEndDate() {
		return this.orderEndDate;
	}

	public void setOrderEndDate(Timestamp orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Timestamp getOrderStartDate() {
		return this.orderStartDate;
	}

	public void setOrderStartDate(Timestamp orderStartDate) {
		this.orderStartDate = orderStartDate;
	}

	public String getPayloadXml() {
		return this.payloadXml;
	}

	public void setPayloadXml(String payloadXml) {
		this.payloadXml = payloadXml;
	}

	public String getPollingStatus() {
		return this.pollingStatus;
	}

	public void setPollingStatus(String pollingStatus) {
		this.pollingStatus = pollingStatus;
	}

	public String getServiceNo() {
		return this.serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
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

	public BulkBatchMaster getBulkBatchMaster() {
		return this.bulkBatchMaster;
	}

	public void setBulkBatchMaster(BulkBatchMaster bulkBatchMaster) {
		this.bulkBatchMaster = bulkBatchMaster;
	}

	public List<OepOrderLine> getOepOrderLines() {
		return this.oepOrderLines;
	}

	public void setOepOrderLines(List<OepOrderLine> oepOrderLines) {
		this.oepOrderLines = oepOrderLines;
	}	

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public OepOrderLine addOepOrderLine(OepOrderLine oepOrderLine) {
		getOepOrderLines().add(oepOrderLine);
		oepOrderLine.setOepOrder(this);

		return oepOrderLine;
	}

	public OepOrderLine removeOepOrderLine(OepOrderLine oepOrderLine) {
		getOepOrderLines().remove(oepOrderLine);
		oepOrderLine.setOepOrder(null);

		return oepOrderLine;
	}

}