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
 * <p>Java class for SuspendOrderBulkSMSServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SuspendOrderBulkSMSServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceDetails" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendDisconnectOrderAllServiceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SuspendOrderBulkSMSServiceType", propOrder = {
    "serviceDetails"
})
public class SuspendOrderBulkSMSServiceType {

    @XmlElement(name = "ServiceDetails", required = true)
    protected SuspendDisconnectOrderAllServiceType serviceDetails;

    /**
     * Gets the value of the serviceDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendDisconnectOrderAllServiceType }
     *     
     */
    public SuspendDisconnectOrderAllServiceType getServiceDetails() {
        return serviceDetails;
    }

    /**
     * Sets the value of the serviceDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendDisconnectOrderAllServiceType }
     *     
     */
    public void setServiceDetails(SuspendDisconnectOrderAllServiceType value) {
        this.serviceDetails = value;
    }

}