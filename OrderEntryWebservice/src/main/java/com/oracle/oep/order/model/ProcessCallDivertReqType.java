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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcessCallDivertReqType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcessCallDivertReqType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ServiceNo"/>
 *         &lt;element name="ForwardToNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceAction" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}CallDivertActionEnumType"/>
 *         &lt;element name="ChannelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ChannelTrackingId"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessCallDivertReqType", propOrder = {
    "serviceNo",
    "forwardToNumber",
    "serviceAction",
    "channelName",
    "channelTrackingId"
})
public class ProcessCallDivertReqType {

    @XmlElement(name = "ServiceNo", required = true)
    protected String serviceNo;
    @XmlElement(name = "ForwardToNumber")
    protected String forwardToNumber;
    @XmlElement(name = "ServiceAction", required = true)
    @XmlSchemaType(name = "string")
    protected CallDivertActionEnumType serviceAction;
    @XmlElement(name = "ChannelName", required = true)
    protected String channelName;
    @XmlElement(name = "ChannelTrackingId", required = true)
    protected String channelTrackingId;

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
     * Gets the value of the forwardToNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForwardToNumber() {
        return forwardToNumber;
    }

    /**
     * Sets the value of the forwardToNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForwardToNumber(String value) {
        this.forwardToNumber = value;
    }

    /**
     * Gets the value of the serviceAction property.
     * 
     * @return
     *     possible object is
     *     {@link CallDivertActionEnumType }
     *     
     */
    public CallDivertActionEnumType getServiceAction() {
        return serviceAction;
    }

    /**
     * Sets the value of the serviceAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link CallDivertActionEnumType }
     *     
     */
    public void setServiceAction(CallDivertActionEnumType value) {
        this.serviceAction = value;
    }

    /**
     * Gets the value of the channelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Sets the value of the channelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelName(String value) {
        this.channelName = value;
    }

    /**
     * Gets the value of the channelTrackingId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelTrackingId() {
        return channelTrackingId;
    }

    /**
     * Sets the value of the channelTrackingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelTrackingId(String value) {
        this.channelTrackingId = value;
    }

}
