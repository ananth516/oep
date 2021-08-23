package com.oracle.oep.brm.persistence.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.oep.brm.persistence.model.OEPConfigPlanAttributesT;

@Repository
public interface OEPConfigPlanAttributesRepository extends JpaRepository<OEPConfigPlanAttributesT, BigDecimal> {
	
	public OEPConfigPlanAttributesT findBySystemPlanName(String systemPlanName);

}
