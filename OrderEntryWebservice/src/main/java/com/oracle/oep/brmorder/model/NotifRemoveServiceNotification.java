//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.21 at 11:53:00 AM IST 
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
 *         &lt;element name="AccountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AccountObj" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ServiceNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Login" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EventDescr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TerminationIndicator" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="RemoveServiceEvents">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BasePackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="QTQTFLAG" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="MMFLAG" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ElementId" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="InstanceId" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "accountObj",
    "serviceNo",
	"login",
    "eventDescr",
    "terminationIndicator",
    "removeServiceEvents"
})
@XmlRootElement(name = "NotifRemoveServiceNotification", namespace = "http://xmlns.oracle.com/communications/schemas/CRMSync")
public class NotifRemoveServiceNotification {

    @XmlElement(name = "AccountNo", required = true)
    protected String accountNo;
    @XmlElement(name = "AccountObj", required = true)
    protected String accountObj;
    @XmlElement(name = "ServiceNo")
    protected String serviceNo;
	@XmlElement(name = "Login")
    protected String login;
    @XmlElement(name = "EventDescr", required = true)
    protected String eventDescr;
    @XmlElement(name = "TerminationIndicator")
    protected byte terminationIndicator;
    @XmlElement(name = "RemoveServiceEvents", required = true)
    protected NotifRemoveServiceNotification.RemoveServiceEvents removeServiceEvents;
    @XmlAttribute(name = "InstanceId")
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
     * Gets the value of the accountObj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountObj() {
        return accountObj;
    }

    /**
     * Sets the value of the accountObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountObj(String value) {
        this.accountObj = value;
    }

    /**
     * Gets the value of the serviceNo property.
     * 
     */
    public String getServiceNo() {
        return serviceNo;
    }

    /**
     * Sets the value of the serviceNo property.
     * 
     */
    public void setServiceNo(String value) {
        this.serviceNo = value;
    }
	
	/**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
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
    public void setLogin(String value) {
        this.login = value;
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
     * Gets the value of the terminationIndicator property.
     * 
     */
    public byte getTerminationIndicator() {
        return terminationIndicator;
    }

    /**
     * Sets the value of the terminationIndicator property.
     * 
     */
    public void setTerminationIndicator(byte value) {
        this.terminationIndicator = value;
    }

    /**
     * Gets the value of the removeServiceEvents property.
     * 
     * @return
     *     possible object is
     *     {@link NotifRemoveServiceNotification.RemoveServiceEvents }
     *     
     */
    public NotifRemoveServiceNotification.RemoveServiceEvents getRemoveServiceEvents() {
        return removeServiceEvents;
    }

    /**
     * Sets the value of the removeServiceEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotifRemoveServiceNotification.RemoveServiceEvents }
     *     
     */
    public void setRemoveServiceEvents(NotifRemoveServiceNotification.RemoveServiceEvents value) {
        this.removeServiceEvents = value;
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
     *         &lt;element name="BasePackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="QTQTFLAG" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="MMFLAG" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ElementId" type="{http://www.w3.org/2001/XMLSchema}byte" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "basePackage",
        "qtqtflag",
        "mmflag"
    })
    public static class RemoveServiceEvents {

        @XmlElement(name = "BasePackage", required = true)
        protected String basePackage;
        @XmlElement(name = "QTQTFLAG")
        protected byte qtqtflag;
        @XmlElement(name = "MMFLAG")
        protected byte mmflag;
        @XmlAttribute(name = "ElementId")
        protected Byte elementId;

        /**
         * Gets the value of the basePackage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBasePackage() {
            return basePackage;
        }

        /**
         * Sets the value of the basePackage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBasePackage(String value) {
            this.basePackage = value;
        }

        /**
         * Gets the value of the qtqtflag property.
         * 
         */
        public byte getQTQTFLAG() {
            return qtqtflag;
        }

        /**
         * Sets the value of the qtqtflag property.
         * 
         */
        public void setQTQTFLAG(byte value) {
            this.qtqtflag = value;
        }

        /**
         * Gets the value of the mmflag property.
         * 
         */
        public byte getMMFLAG() {
            return mmflag;
        }

        /**
         * Sets the value of the mmflag property.
         * 
         */
        public void setMMFLAG(byte value) {
            this.mmflag = value;
        }

        /**
         * Gets the value of the elementId property.
         * 
         * @return
         *     possible object is
         *     {@link Byte }
         *     
         */
        public Byte getElementId() {
            return elementId;
        }

        /**
         * Sets the value of the elementId property.
         * 
         * @param value
         *     allowed object is
         *     {@link Byte }
         *     
         */
        public void setElementId(Byte value) {
            this.elementId = value;
        }

    }

}
