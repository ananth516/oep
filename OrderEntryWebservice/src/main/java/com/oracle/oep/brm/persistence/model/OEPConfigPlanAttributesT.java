package com.oracle.oep.brm.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the CONFIG_PLAN_ATTRIBUTES_T database table.
 * 
 */
@Entity
@Table(name="CONFIG_PLAN_ATTRIBUTES_T")
@NamedQuery(name="OEPConfigPlanAttributesT.findAll", query="SELECT o FROM OEPConfigPlanAttributesT o")
public class OEPConfigPlanAttributesT implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ADDON_EXPIRY_DAYS")
	private BigDecimal addonExpiryDays;

	@Column(name="ADDON_PKG_SUB_TYPE")
	private String addonPkgSubType;

	@Column(name="ADDON_PKG_TYPE")
	private String addonPkgType;

	@Column(name="CHANGE_PLAN_ALLOWED")
	private BigDecimal changePlanAllowed;

	@Column(name="COMMERCIAL_PLAN_NAME")
	private String commercialPlanName;

	@Column(name="CONTRACT_DURATION")
	private BigDecimal contractDuration;

	@Column(name="\"COS\"")
	private String cos;

	@Column(name="DEPOSIT_AMOUNT")
	private BigDecimal depositAmount;

	@Column(name="DEVICE_MODEL")
	private String deviceModel;

	@Column(name="DISCOUNTED_IDD")
	private BigDecimal discountedIdd;

	@Column(name="FAVORITE_COUNTRY")
	private BigDecimal favoriteCountry;

	@Column(name="FREE_NUM_PLAN_NAME")
	private String freeNumPlanName;

	private BigDecimal grants;

	@Column(name="INVOICE_DESCR")
	private String invoiceDescr;

	@Column(name="LC_EXPIRY_DAYS")
	private BigDecimal lcExpiryDays;

	@Column(name="NUM_DISC_FNF")
	private BigDecimal numDiscFnf;

	@Column(name="NUM_OF_FREE_NUMBER")
	private BigDecimal numOfFreeNumber;

	@Column(name="OBJ_ID0")
	private BigDecimal objId0;

	private BigDecimal overridable;

	@Column(name="OVERRIDE_ADD")
	private BigDecimal overrideAdd;

	@Column(name="PACKAGE_PRICE")
	private BigDecimal packagePrice;

	@Column(name="PACKAGE_TYPE")
	private String packageType;

	@Column(name="PENALTY_AMOUNT")
	private BigDecimal penaltyAmount;

	private String ps;

	@Column(name="SERVICE_SUB_TYPE")
	private String serviceSubType;

	@Column(name="SERVICE_TYPE")
	private String serviceType;

	@Column(name="SYSTEM_PLAN_NAME")
	private String systemPlanName;

	@Column(name="TOURIST_KIT")
	private BigDecimal touristKit;

	@Column(name="TP_EXPIRY_DAYS")
	private BigDecimal tpExpiryDays;
	
	@Id
	@Column(name="REC_ID")
	private BigDecimal recId;

	public BigDecimal getRecId() {
		return recId;
	}

	public void setRecId(BigDecimal recId) {
		this.recId = recId;
	}

	//bi-directional many-to-one association to OEPConfigFeaturesT
	@OneToMany(mappedBy="configPlanAttributesT")
	private List<OEPConfigFeaturesT> configFeaturesTs;

	public OEPConfigPlanAttributesT() {
	}

	public BigDecimal getAddonExpiryDays() {
		return this.addonExpiryDays;
	}

	public void setAddonExpiryDays(BigDecimal addonExpiryDays) {
		this.addonExpiryDays = addonExpiryDays;
	}

	public String getAddonPkgSubType() {
		return this.addonPkgSubType;
	}

	public void setAddonPkgSubType(String addonPkgSubType) {
		this.addonPkgSubType = addonPkgSubType;
	}

	public String getAddonPkgType() {
		return this.addonPkgType;
	}

	public void setAddonPkgType(String addonPkgType) {
		this.addonPkgType = addonPkgType;
	}

	public BigDecimal getChangePlanAllowed() {
		return this.changePlanAllowed;
	}

	public void setChangePlanAllowed(BigDecimal changePlanAllowed) {
		this.changePlanAllowed = changePlanAllowed;
	}

	public String getCommercialPlanName() {
		return this.commercialPlanName;
	}

	public void setCommercialPlanName(String commercialPlanName) {
		this.commercialPlanName = commercialPlanName;
	}

	public BigDecimal getContractDuration() {
		return this.contractDuration;
	}

	public void setContractDuration(BigDecimal contractDuration) {
		this.contractDuration = contractDuration;
	}

	public String getCos() {
		return this.cos;
	}

	public void setCos(String cos) {
		this.cos = cos;
	}

	public BigDecimal getDepositAmount() {
		return this.depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getDeviceModel() {
		return this.deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public BigDecimal getDiscountedIdd() {
		return this.discountedIdd;
	}

	public void setDiscountedIdd(BigDecimal discountedIdd) {
		this.discountedIdd = discountedIdd;
	}

	public BigDecimal getFavoriteCountry() {
		return this.favoriteCountry;
	}

	public void setFavoriteCountry(BigDecimal favoriteCountry) {
		this.favoriteCountry = favoriteCountry;
	}

	public String getFreeNumPlanName() {
		return this.freeNumPlanName;
	}

	public void setFreeNumPlanName(String freeNumPlanName) {
		this.freeNumPlanName = freeNumPlanName;
	}

	public BigDecimal getGrants() {
		return this.grants;
	}

	public void setGrants(BigDecimal grants) {
		this.grants = grants;
	}

	public String getInvoiceDescr() {
		return this.invoiceDescr;
	}

	public void setInvoiceDescr(String invoiceDescr) {
		this.invoiceDescr = invoiceDescr;
	}

	public BigDecimal getLcExpiryDays() {
		return this.lcExpiryDays;
	}

	public void setLcExpiryDays(BigDecimal lcExpiryDays) {
		this.lcExpiryDays = lcExpiryDays;
	}

	public BigDecimal getNumDiscFnf() {
		return this.numDiscFnf;
	}

	public void setNumDiscFnf(BigDecimal numDiscFnf) {
		this.numDiscFnf = numDiscFnf;
	}

	public BigDecimal getNumOfFreeNumber() {
		return this.numOfFreeNumber;
	}

	public void setNumOfFreeNumber(BigDecimal numOfFreeNumber) {
		this.numOfFreeNumber = numOfFreeNumber;
	}

	public BigDecimal getObjId0() {
		return this.objId0;
	}

	public void setObjId0(BigDecimal objId0) {
		this.objId0 = objId0;
	}

	public BigDecimal getOverridable() {
		return this.overridable;
	}

	public void setOverridable(BigDecimal overridable) {
		this.overridable = overridable;
	}

	public BigDecimal getOverrideAdd() {
		return this.overrideAdd;
	}

	public void setOverrideAdd(BigDecimal overrideAdd) {
		this.overrideAdd = overrideAdd;
	}

	public BigDecimal getPackagePrice() {
		return this.packagePrice;
	}

	public void setPackagePrice(BigDecimal packagePrice) {
		this.packagePrice = packagePrice;
	}

	public String getPackageType() {
		return this.packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public BigDecimal getPenaltyAmount() {
		return this.penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public String getPs() {
		return this.ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getServiceSubType() {
		return this.serviceSubType;
	}

	public void setServiceSubType(String serviceSubType) {
		this.serviceSubType = serviceSubType;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSystemPlanName() {
		return this.systemPlanName;
	}

	public void setSystemPlanName(String systemPlanName) {
		this.systemPlanName = systemPlanName;
	}

	public BigDecimal getTouristKit() {
		return this.touristKit;
	}

	public void setTouristKit(BigDecimal touristKit) {
		this.touristKit = touristKit;
	}

	public BigDecimal getTpExpiryDays() {
		return this.tpExpiryDays;
	}

	public void setTpExpiryDays(BigDecimal tpExpiryDays) {
		this.tpExpiryDays = tpExpiryDays;
	}

	public List<OEPConfigFeaturesT> getConfigFeaturesTs() {
		return this.configFeaturesTs;
	}

	public void setConfigFeaturesTs(List<OEPConfigFeaturesT> configFeaturesTs) {
		this.configFeaturesTs = configFeaturesTs;
	}

	public OEPConfigFeaturesT addConfigFeaturesT(OEPConfigFeaturesT configFeaturesT) {
		getConfigFeaturesTs().add(configFeaturesT);
		configFeaturesT.setConfigPlanAttributesT(this);

		return configFeaturesT;
	}

	public OEPConfigFeaturesT removeConfigFeaturesT(OEPConfigFeaturesT configFeaturesT) {
		getConfigFeaturesTs().remove(configFeaturesT);
		configFeaturesT.setConfigPlanAttributesT(null);

		return configFeaturesT;
	}

}