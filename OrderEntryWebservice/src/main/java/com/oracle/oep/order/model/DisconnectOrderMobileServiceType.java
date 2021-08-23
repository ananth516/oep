//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.08 at 06:10:55 PM IST 
//


package com.oracle.oep.order.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DisconnectOrderMobileServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DisconnectOrderMobileServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceDetails" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SuspendDisconnectOrderAllServiceType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}PortType" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}EarlyTerminationFlag" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}WaveOff" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}WaveOffReason" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}TerminationCharge"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisconnectOrderMobileServiceType", propOrder = {
    "serviceDetails",
    "portType",
    "earlyTerminationFlag",
    "waveOff",
    "waveOffReason",
    "terminationCharge"
})
public class DisconnectOrderMobileServiceType {

    @XmlElement(name = "ServiceDetails", required = true)
    protected SuspendDisconnectOrderAllServiceType serviceDetails;
    @XmlElement(name = "PortType")
    protected String portType;
    @XmlElement(name = "EarlyTerminationFlag")
    protected String earlyTerminationFlag;
    @XmlElement(name = "WaveOff")
    protected String waveOff;
    @XmlElement(name = "WaveOffReason")
    protected String waveOffReason;
    @XmlElement(name = "TerminationCharge", required = true)
    protected String terminationCharge;

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

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortType(String value) {
        this.portType = value;
    }

    /**
     * Gets the value of the earlyTerminationFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEarlyTerminationFlag() {
        return earlyTerminationFlag;
    }

    /**
     * Sets the value of the earlyTerminationFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEarlyTerminationFlag(String value) {
        this.earlyTerminationFlag = value;
    }

    /**
     * Gets the value of the waveOff property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaveOff() {
        return waveOff;
    }

    /**
     * Sets the value of the waveOff property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaveOff(String value) {
        this.waveOff = value;
    }

    /**
     * Gets the value of the waveOffReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaveOffReason() {
        return waveOffReason;
    }

    /**
     * Sets the value of the waveOffReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaveOffReason(String value) {
        this.waveOffReason = value;
    }

    /**
     * Gets the value of the terminationCharge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminationCharge() {
        return terminationCharge;
    }

    /**
     * Sets the value of the terminationCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminationCharge(String value) {
        this.terminationCharge = value;
    }

}
