//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.05 at 11:55:57 AM IST 
//


package com.oracle.oep.notif.xsd;

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
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}AccountNo"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}ServiceNo"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}EventDescr"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}Channel"/>
 *         &lt;element ref="{http://xmlns.oracle.com/communications/schemas/CRMSync}FailureCode"/>
 *       &lt;/sequence>
 *       &lt;attribute name="InstanceId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "accountNo",
    "serviceNo",
    "eventDescr",
    "channel",
    "failureCode"
})
@XmlRootElement(name = "NotifChangeRatePlanFailure")
public class NotifChangeRatePlanFailure {

    @XmlElement(name = "AccountNo", required = true)
    protected String accountNo;
    @XmlElement(name = "ServiceNo")
    protected long serviceNo;
    @XmlElement(name = "EventDescr", required = true)
    protected String eventDescr;
    @XmlElement(name = "Channel", required = true)
    protected String channel;
    @XmlElement(name = "FailureCode", required = true)
    protected String failureCode;
    @XmlAttribute(name = "InstanceId", required = true)
    protected String instanceId;

    /**
     * Gets the value of the accountNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * Sets the value of the accountNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNo(String value) {
        this.accountNo = value;
    }

    /**
     * Gets the value of the serviceNo property.
     * 
     */
    public long getServiceNo() {
        return serviceNo;
    }

    /**
     * Sets the value of the serviceNo property.
     * 
     */
    public void setServiceNo(long value) {
        this.serviceNo = value;
    }

    /**
     * Gets the value of the eventDescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDescr() {
        return eventDescr;
    }

    /**
     * Sets the value of the eventDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDescr(String value) {
        this.eventDescr = value;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannel(String value) {
        this.channel = value;
    }

    /**
     * Gets the value of the failureCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureCode() {
        return failureCode;
    }

    /**
     * Sets the value of the failureCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureCode(String value) {
        this.failureCode = value;
    }

    /**
     * Gets the value of the instanceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Sets the value of the instanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceId(String value) {
        this.instanceId = value;
    }

}
