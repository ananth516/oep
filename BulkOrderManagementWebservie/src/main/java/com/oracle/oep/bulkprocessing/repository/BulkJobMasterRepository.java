package com.oracle.oep.bulkprocessing.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.oep.bulkprocessing.model.BulkJobMaster;

@Repository
public interface BulkJobMasterRepository extends JpaRepository<BulkJobMaster, Long> {
	
	public BulkJobMaster findByFileOrderId(String fileOrderId);

}
