//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.11 at 11:07:29 PM IST 
//


package com.oracle.oep.brmorder.model;

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
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}PlanName" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}ServiceIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}Msisdn" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}BarringType" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}Action" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ElementId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "planName",
    "serviceIdentifier",
    "msisdn",
    "barringType",
    "action"
})
@XmlRootElement(name = "Collservices")
public class Collservices {

    @XmlElement(name = "PlanName")
    protected String planName;
    @XmlElement(name = "ServiceIdentifier")
    protected String serviceIdentifier;
    @XmlElement(name = "Msisdn")
    protected String msisdn;
    @XmlElement(name = "BarringType")
    protected String barringType;
    @XmlElement(name = "Action")
    protected String action;
    @XmlAttribute(name = "ElementId", required = true)
    protected String elementId;

    /**
     * Gets the value of the planName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * Sets the value of the planName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanName(String value) {
        this.planName = value;
    }

    /**
     * Gets the value of the serviceIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    /**
     * Sets the value of the serviceIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceIdentifier(String value) {
        this.serviceIdentifier = value;
    }

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsisdn() {
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
    public void setMsisdn(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the barringType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBarringType() {
        return barringType;
    }

    /**
     * Sets the value of the barringType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBarringType(String value) {
        this.barringType = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the elementId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * Sets the value of the elementId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElementId(String value) {
        this.elementId = value;
    }

}