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
 *         &lt;element name="ProcessOrderResponse" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ProcessOrderResponseType"/>
 *         &lt;element name="ProcessQueryOrderResponse" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ProcessQueryOrderResponseType"/>
 *         &lt;element name="ProcessQueryOrderDetailsResponse" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ProcessQueryOrderDetailsResponseType"/>
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
    "processOrderResponse",
    "processQueryOrderResponse",
    "processQueryOrderDetailsResponse"
})
@XmlRootElement(name = "OrderEntryResponse")
public class OrderEntryResponse {

    @XmlElement(name = "ProcessOrderResponse", required = true)
    protected ProcessOrderResponseType processOrderResponse;
    @XmlElement(name = "ProcessQueryOrderResponse", required = true)
    protected ProcessQueryOrderResponseType processQueryOrderResponse;
    @XmlElement(name = "ProcessQueryOrderDetailsResponse", required = true)
    protected ProcessQueryOrderDetailsResponseType processQueryOrderDetailsResponse;

    /**
     * Gets the value of the processOrderResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessOrderResponseType }
     *     
     */
    public ProcessOrderResponseType getProcessOrderResponse() {
        return processOrderResponse;
    }

    /**
     * Sets the value of the processOrderResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessOrderResponseType }
     *     
     */
    public void setProcessOrderResponse(ProcessOrderResponseType value) {
        this.processOrderResponse = value;
    }

    /**
     * Gets the value of the processQueryOrderResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessQueryOrderResponseType }
     *     
     */
    public ProcessQueryOrderResponseType getProcessQueryOrderResponse() {
        return processQueryOrderResponse;
    }

    /**
     * Sets the value of the processQueryOrderResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessQueryOrderResponseType }
     *     
     */
    public void setProcessQueryOrderResponse(ProcessQueryOrderResponseType value) {
        this.processQueryOrderResponse = value;
    }

    /**
     * Gets the value of the processQueryOrderDetailsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessQueryOrderDetailsResponseType }
     *     
     */
    public ProcessQueryOrderDetailsResponseType getProcessQueryOrderDetailsResponse() {
        return processQueryOrderDetailsResponse;
    }

    /**
     * Sets the value of the processQueryOrderDetailsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessQueryOrderDetailsResponseType }
     *     
     */
    public void setProcessQueryOrderDetailsResponse(ProcessQueryOrderDetailsResponseType value) {
        this.processQueryOrderDetailsResponse = value;
    }

}
