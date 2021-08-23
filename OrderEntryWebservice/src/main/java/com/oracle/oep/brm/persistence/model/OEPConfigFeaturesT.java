package com.oracle.oep.brm.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the CONFIG_FEATURES_T database table.
 * 
 */
@Entity
@Table(name="CONFIG_FEATURES_T")
@NamedQuery(name="OEPConfigFeaturesT.findAll", query="SELECT o FROM OEPConfigFeaturesT o")
public class OEPConfigFeaturesT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="OBJ_ID0")
	private BigDecimal objId0;

	@Column(name="PARAM_NAME")
	private String paramName;

	@Column(name="PARAM_VALUE")
	private String paramValue;

 
	
	@EmbeddedId
	private OEPConfigFeaturesKey featuresKey;

	

	public OEPConfigFeaturesKey getFeaturesKey() {
		return featuresKey;
	}

	public void setFeaturesKey(OEPConfigFeaturesKey featuresKey) {
		this.featuresKey = featuresKey;
	}

	//bi-directional many-to-one association to OEPConfigPlanAttributesT
	@ManyToOne
	@JoinColumn(name="REC_ID2", referencedColumnName="REC_ID", insertable=false, updatable=false)
	private OEPConfigPlanAttributesT configPlanAttributesT;

	public OEPConfigFeaturesT() {
	}

	public BigDecimal getObjId0() {
		return this.objId0;
	}

	public void setObjId0(BigDecimal objId0) {
		this.objId0 = objId0;
	}

	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	
	public OEPConfigPlanAttributesT getConfigPlanAttributesT() {
		return this.configPlanAttributesT;
	}

	public void setConfigPlanAttributesT(OEPConfigPlanAttributesT configPlanAttributesT) {
		this.configPlanAttributesT = configPlanAttributesT;
	}

}