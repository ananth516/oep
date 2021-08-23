package com.oracle.oep.bulkprocessing.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.oep.bulkprocessing.model.OepOrder;

@Repository
public interface OepOrderRepository extends JpaRepository<OepOrder, Long> {
	
	public List<OepOrder> findByBatchId(Integer batchId);

}
