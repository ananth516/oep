package com.oracle.oep.bulkprocessing.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@XmlRootElement
//@XmlType


/**
 * The persistent class for the BULK_JOB_MASTER database table.
 * 
 */
@Entity
@Table(name="BULK_JOB_MASTER")

@NamedQueries({
	@NamedQuery(name="BulkJobMaster.findAll", query="SELECT b FROM BulkJobMaster b"),
	@NamedQuery(name="BulkJobMaster.findAllByStatus", query="SELECT b FROM BulkJobMaster b where b.status = 'SCHEDULED' "),
	@NamedQuery(name="getObjectByFileOrderId", query="SELECT b FROM BulkJobMaster b where b.fileOrderId =:fileOrderId")
})
public class BulkJobMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="JOB_ID")
	private Long jobId;

	@Column(name="ACTION")
	private String action;

	@Column(name="FILE_ORDER_ID")
	private String fileOrderId;

	@Column(name="FILE_PATH")
	private String filePath;	

	private String status;
	
	@Column(name="JOB_CREATION_DATE")
	private Timestamp jobCreationDate;
	
	@Column(name="JOB_START_DATE")
	private Timestamp jobStartDate;
	
	@Column(name="JOB_COMPLETED_DATE")
	private Timestamp jobCompletedDate;	
	
	@Column(name="CSR_ID")
	private String csrId;	
		
	//bi-directional many-to-one association to BulkBatchMaster
	@OneToMany(mappedBy="bulkJobMaster")
	private List<BulkBatchMaster> bulkBatchMasters;

	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
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

	public Timestamp getJobCreationDate() {
		return jobCreationDate;
	}

	public void setJobCreationDate(Timestamp jobCreationDate) {
		this.jobCreationDate = jobCreationDate;
	}

	public Timestamp getJobStartDate() {
		return jobStartDate;
	}

	public void setJobStartDate(Timestamp jobStartDate) {
		this.jobStartDate = jobStartDate;
	}

	public Timestamp getJobCompletedDate() {
		return jobCompletedDate;
	}

	public void setJobCompletedDate(Timestamp jobCompletedDate) {
		this.jobCompletedDate = jobCompletedDate;
	}

	public String getCsrId() {
		return csrId;
	}

	public void setCsrId(String csrId) {
		this.csrId = csrId;
	}

	
	
	
}