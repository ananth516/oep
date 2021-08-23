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
 * <p>Java class for SuspendOrderServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SuspendOrderServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MobileService" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendOrderMobileServiceType" minOccurs="0"/>
 *         &lt;element name="MobileBBService" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendOrderMobileBBServiceType" minOccurs="0"/>
 *         &lt;element name="MobileShortCodeService" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendOrderMobileShortCodeServiceType" minOccurs="0"/>
 *         &lt;element name="M2MService" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendOrderM2MServiceType" minOccurs="0"/>
 *         &lt;element name="BulkSMSService" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendOrderBulkSMSServiceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SuspendOrderServiceType", propOrder = {
    "mobileService",
    "mobileBBService",
    "mobileShortCodeService",
    "m2MService",
    "bulkSMSService"
})
public class SuspendOrderServiceType {

    @XmlElement(name = "MobileService")
    protected SuspendOrderMobileServiceType mobileService;
    @XmlElement(name = "MobileBBService")
    protected SuspendOrderMobileBBServiceType mobileBBService;
    @XmlElement(name = "MobileShortCodeService")
    protected SuspendOrderMobileShortCodeServiceType mobileShortCodeService;
    @XmlElement(name = "M2MService")
    protected SuspendOrderM2MServiceType m2MService;
    @XmlElement(name = "BulkSMSService")
    protected SuspendOrderBulkSMSServiceType bulkSMSService;

    /**
     * Gets the value of the mobileService property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendOrderMobileServiceType }
     *     
     */
    public SuspendOrderMobileServiceType getMobileService() {
        return mobileService;
    }

    /**
     * Sets the value of the mobileService property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendOrderMobileServiceType }
     *     
     */
    public void setMobileService(SuspendOrderMobileServiceType value) {
        this.mobileService = value;
    }

    /**
     * Gets the value of the mobileBBService property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendOrderMobileBBServiceType }
     *     
     */
    public SuspendOrderMobileBBServiceType getMobileBBService() {
        return mobileBBService;
    }

    /**
     * Sets the value of the mobileBBService property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendOrderMobileBBServiceType }
     *     
     */
    public void setMobileBBService(SuspendOrderMobileBBServiceType value) {
        this.mobileBBService = value;
    }

    /**
     * Gets the value of the mobileShortCodeService property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendOrderMobileShortCodeServiceType }
     *     
     */
    public SuspendOrderMobileShortCodeServiceType getMobileShortCodeService() {
        return mobileShortCodeService;
    }

    /**
     * Sets the value of the mobileShortCodeService property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendOrderMobileShortCodeServiceType }
     *     
     */
    public void setMobileShortCodeService(SuspendOrderMobileShortCodeServiceType value) {
        this.mobileShortCodeService = value;
    }

    /**
     * Gets the value of the m2MService property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendOrderM2MServiceType }
     *     
     */
    public SuspendOrderM2MServiceType getM2MService() {
        return m2MService;
    }

    /**
     * Sets the value of the m2MService property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendOrderM2MServiceType }
     *     
     */
    public void setM2MService(SuspendOrderM2MServiceType value) {
        this.m2MService = value;
    }

    /**
     * Gets the value of the bulkSMSService property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendOrderBulkSMSServiceType }
     *     
     */
    public SuspendOrderBulkSMSServiceType getBulkSMSService() {
        return bulkSMSService;
    }

    /**
     * Sets the value of the bulkSMSService property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendOrderBulkSMSServiceType }
     *     
     */
    public void setBulkSMSService(SuspendOrderBulkSMSServiceType value) {
        this.bulkSMSService = value;
    }

}
