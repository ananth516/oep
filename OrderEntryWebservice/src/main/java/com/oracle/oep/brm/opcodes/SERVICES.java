//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.07.14 at 10:08:20 AM IST 
//


package com.oracle.oep.brm.opcodes;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}SERVICE_TYPE"/>
 *         &lt;element ref="{}LOGIN"/>
 *         &lt;element ref="{}MSISDN"/>
 *         &lt;element ref="{}ACTION"/>
 *         &lt;element ref="{}CUS_FLD_SUSPENSE_DURATION"/>
 *         &lt;element ref="{}ORDER_ID"/>
 *         &lt;element ref="{}DEAL_NAME"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "servicetype",
    "login",
    "msisdn",
    "action",
    "cusfldsuspenseduration",
    "orderid",
    "dealname"
})
@XmlRootElement(name = "SERVICES")
public class SERVICES {

    @XmlElement(name = "SERVICE_TYPE", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String servicetype;
    @XmlElement(name = "LOGIN", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String login;
    @XmlElement(name = "MSISDN", required = true)
    protected String msisdn;
    @XmlElement(name = "ACTION", required = true)
    protected BigInteger action;
    @XmlElement(name = "CUS_FLD_SUSPENSE_DURATION", required = true)
    protected BigInteger cusfldsuspenseduration;
    @XmlElement(name = "ORDER_ID", required = true)
    protected String orderid;
    @XmlElement(name = "DEAL_NAME", required = true)
    protected String dealname;

    /**
     * Gets the value of the servicetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERVICETYPE() {
        return servicetype;
    }

    /**
     * Sets the value of the servicetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERVICETYPE(String value) {
        this.servicetype = value;
    }

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGIN() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGIN(String value) {
        this.login = value;
    }

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSISDN() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSISDN(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getACTION() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setACTION(BigInteger value) {
        this.action = value;
    }

    /**
     * Gets the value of the cusfldsuspenseduration property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSFLDSUSPENSEDURATION() {
        return cusfldsuspenseduration;
    }

    /**
     * Sets the value of the cusfldsuspenseduration property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSFLDSUSPENSEDURATION(BigInteger value) {
        this.cusfldsuspenseduration = value;
    }

    /**
     * Gets the value of the orderid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERID() {
        return orderid;
    }

    /**
     * Sets the value of the orderid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERID(String value) {
        this.orderid = value;
    }

    /**
     * Gets the value of the dealname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEALNAME() {
        return dealname;
    }

    /**
     * Sets the value of the dealname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEALNAME(String value) {
        this.dealname = value;
    }

}
