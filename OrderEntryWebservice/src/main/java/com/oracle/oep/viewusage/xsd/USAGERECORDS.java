//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.23 at 12:02:59 PM IST 
//


package com.oracle.oep.viewusage.xsd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.oracle.oep.viewusage.processor.UsageCharges;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}EVENT_NO"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}DESCR"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}USAGE_TYPE"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}CUS_FLD_UNIT_NAME"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}START_T"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}END_T"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}ORIGIN_SID"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}LOC_AREA_CODE"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}TERMINATE_CAUSE"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}CALLED_TO"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}QUANTITY"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}AMOUNT"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}IMPACT_CATEGORY"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}USAGE_CLASS"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/ViewUsage/V1}CHARGES"/>
 *       &lt;/sequence>
 *       &lt;attribute name="elem" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eventno",
    "descr",
    "usagetype",
    "cusfldunitname",
    "startt",
    "endt",
    "originsid",
    "locareacode",
    "terminatecause",
    "calledto",
    "quantity",
    "amount",
    "impactcategory",
    "usageClass",
    "charges"
})
@XmlRootElement(name = "USAGE_RECORDS")
public class USAGERECORDS {

    @XmlElement(name = "EVENT_NO", required = true)
    protected String eventno;
    @XmlElement(name = "DESCR", required = true)
    protected String descr;
    @XmlElement(name = "USAGE_TYPE", required = true)
    protected String usagetype;
    @XmlElement(name = "CUS_FLD_UNIT_NAME", required = true)
    protected String cusfldunitname;
    @XmlElement(name = "START_T")
    protected long startt;
    @XmlElement(name = "END_T")
    protected long endt;
    @XmlElement(name = "ORIGIN_SID", required = true)
    protected ORIGINSID originsid;
    @XmlElement(name = "LOC_AREA_CODE", required = true)
    protected LOCAREACODE locareacode;
    @XmlElement(name = "TERMINATE_CAUSE")
    protected String terminatecause;
    @XmlElement(name = "CALLED_TO")
    protected String calledto;
    @XmlElement(name = "QUANTITY")
    protected Long quantity;
    @XmlElement(name = "AMOUNT")
    protected BigDecimal amount;
    @XmlElement(name = "IMPACT_CATEGORY", required = true)
    protected String impactcategory;
    @XmlElement(name = "USAGE_CLASS", required = true)
    protected String usageClass;
    @XmlElement(name = "CHARGES", required = true)
    protected List<CHARGES> charges;
    @XmlAttribute(name = "elem", required = true)
    protected int elem;

    /**
     * Gets the value of the eventno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEVENTNO() {
        return eventno;
    }

    /**
     * Sets the value of the eventno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEVENTNO(String value) {
        this.eventno = value;
    }

    /**
     * Gets the value of the descr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCR() {
        return descr;
    }

    /**
     * Sets the value of the descr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCR(String value) {
        this.descr = value;
    }

    /**
     * Gets the value of the usagetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSAGETYPE() {
        return usagetype;
    }

    /**
     * Sets the value of the usagetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSAGETYPE(String value) {
        this.usagetype = value;
    }

    /**
     * Gets the value of the cusfldunitname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSFLDUNITNAME() {
        return cusfldunitname;
    }

    /**
     * Sets the value of the cusfldunitname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSFLDUNITNAME(String value) {
        this.cusfldunitname = value;
    }

    /**
     * Gets the value of the startt property.
     * 
     */
    public long getSTARTT() {
        return startt;
    }

    /**
     * Sets the value of the startt property.
     * 
     */
    public void setSTARTT(long value) {
        this.startt = value;
    }

    /**
     * Gets the value of the endt property.
     * 
     */
    public long getENDT() {
        return endt;
    }

    /**
     * Sets the value of the endt property.
     * 
     */
    public void setENDT(long value) {
        this.endt = value;
    }

    /**
     * Gets the value of the originsid property.
     * 
     * @return
     *     possible object is
     *     {@link ORIGINSID }
     *     
     */
    public ORIGINSID getORIGINSID() {
        return originsid;
    }

    /**
     * Sets the value of the originsid property.
     * 
     * @param value
     *     allowed object is
     *     {@link ORIGINSID }
     *     
     */
    public void setORIGINSID(ORIGINSID value) {
        this.originsid = value;
    }

    /**
     * Gets the value of the locareacode property.
     * 
     * @return
     *     possible object is
     *     {@link LOCAREACODE }
     *     
     */
    public LOCAREACODE getLOCAREACODE() {
        return locareacode;
    }

    /**
     * Sets the value of the locareacode property.
     * 
     * @param value
     *     allowed object is
     *     {@link LOCAREACODE }
     *     
     */
    public void setLOCAREACODE(LOCAREACODE value) {
        this.locareacode = value;
    }

    /**
     * Gets the value of the terminatecause property.
     * 
     */
    public String getTERMINATECAUSE() {
        return terminatecause;
    }

    /**
     * Sets the value of the terminatecause property.
     * 
     */
    public void setTERMINATECAUSE(String value) {
        this.terminatecause = value;
    }

    /**
     * Gets the value of the calledto property.
     * 
     */
    public String getCALLEDTO() {
        return calledto;
    }

    /**
     * Sets the value of the calledto property.
     * 
     */
    public void setCALLEDTO(String value) {
        this.calledto = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public Long getQUANTITY() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQUANTITY(Long value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     */
    public BigDecimal getAMOUNT() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     */
    public void setAMOUNT(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the impactcategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIMPACTCATEGORY() {
        return impactcategory;
    }

    /**
     * Sets the value of the impactcategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIMPACTCATEGORY(String value) {
        this.impactcategory = value;
    }
    
    

    public String getUsageClass() {
		return usageClass;
	}

	public void setUsageClass(String usageClass) {
		this.usageClass = usageClass;
	}

	/**
     * Gets the value of the charges property.
     * 
     * @return
     *     possible object is
     *     {@link CHARGES }
     *     
     */
/*    public CHARGES getCHARGES() {
        return charges;
    }

    *//**
     * Sets the value of the charges property.
     * 
     * @param value
     *     allowed object is
     *     {@link CHARGES }
     *     
     *//*
    public void setCHARGES(CHARGES value) {
        this.charges = value;
    }*/
    
    public List<CHARGES> getCharges() {
        if (charges == null) {
        	charges = new ArrayList<CHARGES>();
        }
        return this.charges;
    }

    /**
     * Gets the value of the elem property.
     * 
     */
    public int getElem() {
        return elem;
    }

    /**
     * Sets the value of the elem property.
     * 
     */
    public void setElem(int value) {
        this.elem = value;
    }

}
