package com.oracle.oep.order.presistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oracle.oep.order.presistence.model.OepOrderLine;

public interface OepOrderLineRepository extends JpaRepository<OepOrderLine, String> {
	
	public List<OepOrderLine> findByOrderLineId(String orderLineId);

}
