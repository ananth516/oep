package com.oracle.oep.order.presistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//@XmlRootElement
//@XmlType


/**
 * The persistent class for the BULK_JOB_MASTER database table.
 * 
 */
@Entity
@Table(name="BULK_JOB_MASTER")
@NamedQuery(name="BulkJobMaster.findAll", query="SELECT b FROM BulkJobMaster b")
public class BulkJobMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="JOB_ID")
	private String jobId;

	@Column(name="\"ACTION\"")
	private String action;

	@Column(name="FILE_ORDER_ID")
	private String fileOrderId;

	@Column(name="FILE_PATH")
	private String filePath;

	private String status;

	//bi-directional many-to-one association to BulkBatchMaster
	@OneToMany(mappedBy="bulkJobMaster")
	private List<BulkBatchMaster> bulkBatchMasters;

	public BulkJobMaster() {
	}

	public String getJobId() {
		return this.jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFileOrderId() {
		return this.fileOrderId;
	}

	public void setFileOrderId(String fileOrderId) {
		this.fileOrderId = fileOrderId;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<BulkBatchMaster> getBulkBatchMasters() {
		return this.bulkBatchMasters;
	}

	public void setBulkBatchMasters(List<BulkBatchMaster> bulkBatchMasters) {
		this.bulkBatchMasters = bulkBatchMasters;
	}

	public BulkBatchMaster addBulkBatchMaster(BulkBatchMaster bulkBatchMaster) {
		getBulkBatchMasters().add(bulkBatchMaster);
		bulkBatchMaster.setBulkJobMaster(this);

		return bulkBatchMaster;
	}

	public BulkBatchMaster removeBulkBatchMaster(BulkBatchMaster bulkBatchMaster) {
		getBulkBatchMasters().remove(bulkBatchMaster);
		bulkBatchMaster.setBulkJobMaster(null);

		return bulkBatchMaster;
	}

}