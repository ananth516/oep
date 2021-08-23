package com.oracle.oep.order.presistence.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oracle.oep.order.presistence.model.OepOrder;

@Repository
public interface OepOrderRepository extends JpaRepository<OepOrder, String>{
	
	public List<OepOrder> findByServiceNo(String serviceNo);
	
	public OepOrder findByOrderId(String orderId);
	
	public List<OepOrder> findByAccountNoAndOrderId(String accountNo,String orderId);
	
	public List<OepOrder> findByAccountNo(String accountNo);
	
	public List<OepOrder> findByAccountNoAndServiceNo(String accountNo,String serviceNo);	

	@Query("select o from OepOrder o where o.accountNo = :accountNo and o.orderCreationDate between :orderCreationDateMin and :orderCreationDateMax")
	public List<OepOrder> findByAccountNoAndDateRange(@Param("accountNo") String accountNo,@Param("orderCreationDateMin")  Timestamp orderCreationDateMin,@Param("orderCreationDateMax")  Timestamp orderCreationDateMax);
	
	@Query("select o from OepOrder o where o.serviceNo = :serviceNo and o.orderCreationDate between :orderCreationDateMin and :orderCreationDateMax")
	public List<OepOrder> findByServiceNoAndDateRange(@Param("serviceNo")  String serviceNo,@Param("orderCreationDateMin")  Timestamp orderCreationDateMin,@Param("orderCreationDateMax")  Timestamp orderCreationDateMax);
	
	@Query(value="select OEPUSER.OEP_ORDER_SEQ.nextval from dual", nativeQuery = true)
	public BigDecimal getNextValOepOrderSeq();
}
