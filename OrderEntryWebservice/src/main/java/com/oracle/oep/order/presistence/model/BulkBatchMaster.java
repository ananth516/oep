package com.oracle.oep.order.presistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
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
	private String batchId;

	@Column(name="BATCH_NAME")
	private String batchName;

	//bi-directional many-to-one association to BulkJobMaster
	@ManyToOne
	@JoinColumn(name="JOB_ID")
	private BulkJobMaster bulkJobMaster;

	//bi-directional many-to-one association to OepOrder
	@OneToMany(mappedBy="bulkBatchMaster")
	private List<OepOrder> oepOrders;

	public BulkBatchMaster() {
	}

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
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

}