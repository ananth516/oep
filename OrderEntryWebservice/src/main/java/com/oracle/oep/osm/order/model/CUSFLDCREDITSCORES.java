//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.08 at 02:50:19 PM IST 
//


package com.oracle.oep.osm.order.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://xmlns.oracle.com/EnterpriseObjects/Core/EBO/SalesOrder/V2}CUS_FLD_CREDIT_SCORE"/>
 *         &lt;element ref="{http://xmlns.oracle.com/EnterpriseObjects/Core/EBO/SalesOrder/V2}CUS_FLD_SC_MAX_VAL"/>
 *         &lt;element ref="{http://xmlns.oracle.com/EnterpriseObjects/Core/EBO/SalesOrder/V2}CUS_FLD_SC_MIN_VAL"/>
 *         &lt;element ref="{http://xmlns.oracle.com/EnterpriseObjects/Core/EBO/SalesOrder/V2}CUS_FLD_SC_ACTUAL_VAL"/>
 *       &lt;/sequence>
 *       &lt;attribute name="elem" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cusfldcreditscore",
    "cusfldscmaxval",
    "cusfldscminval",
    "cusfldscactualval"
})
@XmlRootElement(name = "CUS_FLD_CREDIT_SCORES")
public class CUSFLDCREDITSCORES {

    @XmlElement(name = "CUS_FLD_CREDIT_SCORE", required = true)
    protected BigDecimal cusfldcreditscore;
    @XmlElement(name = "CUS_FLD_SC_MAX_VAL", required = true)
    protected BigDecimal cusfldscmaxval;
    @XmlElement(name = "CUS_FLD_SC_MIN_VAL", required = true)
    protected BigDecimal cusfldscminval;
    @XmlElement(name = "CUS_FLD_SC_ACTUAL_VAL", required = true)
    protected BigDecimal cusfldscactualval;
    @XmlAttribute(name = "elem", required = true)
    protected BigInteger elem;

    /**
     * Gets the value of the cusfldcreditscore property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUSFLDCREDITSCORE() {
        return cusfldcreditscore;
    }

    /**
     * Sets the value of the cusfldcreditscore property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUSFLDCREDITSCORE(BigDecimal value) {
        this.cusfldcreditscore = value;
    }

    /**
     * Gets the value of the cusfldscmaxval property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUSFLDSCMAXVAL() {
        return cusfldscmaxval;
    }

    /**
     * Sets the value of the cusfldscmaxval property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUSFLDSCMAXVAL(BigDecimal value) {
        this.cusfldscmaxval = value;
    }

    /**
     * Gets the value of the cusfldscminval property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUSFLDSCMINVAL() {
        return cusfldscminval;
    }

    /**
     * Sets the value of the cusfldscminval property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUSFLDSCMINVAL(BigDecimal value) {
        this.cusfldscminval = value;
    }

    /**
     * Gets the value of the cusfldscactualval property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUSFLDSCACTUALVAL() {
        return cusfldscactualval;
    }

    /**
     * Sets the value of the cusfldscactualval property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUSFLDSCACTUALVAL(BigDecimal value) {
        this.cusfldscactualval = value;
    }

    /**
     * Gets the value of the elem property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getElem() {
        return elem;
    }

    /**
     * Sets the value of the elem property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setElem(BigInteger value) {
        this.elem = value;
    }

}
