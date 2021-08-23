//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.05 at 06:34:48 PM IST 
//


package com.oracle.oep.order.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BarringServicesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BarringServicesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}PlanName" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}BarringType" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ServiceIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ServiceNo" minOccurs="0"/>
 *         &lt;element name="Action" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ServiceLevelBarringActionType" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ServiceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BarringServicesType", propOrder = {
    "planName",
    "barringType",
    "serviceIdentifier",
    "serviceNo",
    "action",
    "serviceType"
})
public class BarringServicesType {

    @XmlElement(name = "PlanName")
    protected String planName;
    @XmlElement(name = "BarringType")
    protected String barringType;
    @XmlElement(name = "ServiceIdentifier")
    protected String serviceIdentifier;
    @XmlElement(name = "ServiceNo")
    protected String serviceNo;
    @XmlElement(name = "Action")
    protected String action;
    @XmlElement(name = "ServiceType")
    protected String serviceType;

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
     * Gets the value of the serviceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceNo() {
        return serviceNo;
    }

    /**
     * Sets the value of the serviceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceNo(String value) {
        this.serviceNo = value;
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
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

}
