package com.oracle.oep.bulkprocessing.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType


/**
 * The persistent class for the BULK_BATCH_MASTER database table.
 * 
 */
@Entity
@Table(name="BULK_BATCH_MASTER")
@NamedQuery(name="BulkBatchMaster.findAll", query="SELECT b FROM BulkBatchMaster b")
public class BulkBatchMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BATCH_ID")
	private int batchId;

	@Column(name="BATCH_NAME")
	private String batchName;

	//bi-directional many-to-one association to BulkJobMaster
	@ManyToOne
	@JoinColumn(name="JOB_ID")
	private BulkJobMaster bulkJobMaster;

	//bi-directional many-to-one association to OepOrder
	@OneToMany(mappedBy="bulkBatchMaster", cascade=CascadeType.ALL)
	private List<OepOrder> oepOrders = new ArrayList<OepOrder>();
	
	@Column(name="BATCH_CREATION_DATE")
	private Timestamp batchCreationDate;
	
	@Column(name="BATCH_COMPLETED_DATE")
	private Timestamp batchCompletedDate;

	
	public BulkBatchMaster() {
	}

	public int getBatchId() {
		return this.batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public BulkJobMaster getBulkJobMaster() {
		return this.bulkJobMaster;
	}

	public void setBulkJobMaster(BulkJobMaster bulkJobMaster) {
		this.bulkJobMaster = bulkJobMaster;
	}

	public List<OepOrder> getOepOrders() {
		return this.oepOrders;
	}

	public void setOepOrders(List<OepOrder> oepOrders) {
		this.oepOrders = oepOrders;
	}

	public OepOrder addOepOrder(OepOrder oepOrder) {
		getOepOrders().add(oepOrder);
		oepOrder.setBulkBatchMaster(this);

		return oepOrder;
	}

	public OepOrder removeOepOrder(OepOrder oepOrder) {
		getOepOrders().remove(oepOrder);
		oepOrder.setBulkBatchMaster(null);

		return oepOrder;
	}

	public Timestamp getBatchCreationDate() {
		return batchCreationDate;
	}

	public void setBatchCreationDate(Timestamp batchCreationDate) {
		this.batchCreationDate = batchCreationDate;
	}

	public Timestamp getBatchCompletedDate() {
		return batchCompletedDate;
	}

	public void setBatchCompletedDate(Timestamp batchCompletedDate) {
		this.batchCompletedDate = batchCompletedDate;
	}

	
	
	
}