//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.06 at 04:33:16 PM IST 
//


package com.oracle.oep.troubleticket.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}Action"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}Channel" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CallType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CallArea"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CallCategory"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CallSubCategory"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CallActionType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}ServiceAccount"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}BillingAccount" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CustomerAccount" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}ServiceNo" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}ServiceType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}Priority"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}CallBack" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}Remarks"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}IssueDateTime" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}OrderId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}FailureCode"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/TroubleTicketService/V1}FailureDescription" minOccurs="0"/>
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
    "action",
    "channel",
    "callType",
    "callArea",
    "callCategory",
    "callSubCategory",
    "callActionType",
    "serviceAccount",
    "billingAccount",
    "customerAccount",
    "serviceNo",
    "serviceType",
    "priority",
    "callBack",
    "remarks",
    "issueDateTime",
    "orderId",
    "failureCode",
    "failureDescription"
})
@XmlRootElement(name = "CreateTroubleTicketRequest")
public class CreateTroubleTicketRequest {

    @XmlElement(name = "Action", required = true)
    protected String action;
    @XmlElement(name = "Channel")
    protected String channel;
    @XmlElement(name = "CallType", required = true)
    protected String callType;
    @XmlElement(name = "CallArea", required = true)
    protected String callArea;
    @XmlElement(name = "CallCategory", required = true)
    protected String callCategory;
    @XmlElement(name = "CallSubCategory", required = true)
    protected String callSubCategory;
    @XmlElement(name = "CallActionType", required = true)
    protected String callActionType;
    @XmlElement(name = "ServiceAccount", required = true)
    protected String serviceAccount;
    @XmlElement(name = "BillingAccount")
    protected String billingAccount;
    @XmlElement(name = "CustomerAccount")
    protected String customerAccount;
    @XmlElement(name = "ServiceNo")
    protected String serviceNo;
    @XmlElement(name = "ServiceType", required = true)
    protected String serviceType;
    @XmlElement(name = "Priority", required = true)
    protected String priority;
    @XmlElement(name = "CallBack")
    protected String callBack;
    @XmlElement(name = "Remarks", required = true)
    protected String remarks;
    @XmlElement(name = "IssueDateTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar issueDateTime;
    @XmlElement(name = "OrderId", required = true)
    protected String orderId;
    @XmlElement(name = "FailureCode", required = true)
    protected String failureCode;
    @XmlElement(name = "FailureDescription")
    protected String failureDescription;

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
     * Gets the value of the callType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallType() {
        return callType;
    }

    /**
     * Sets the value of the callType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallType(String value) {
        this.callType = value;
    }

    /**
     * Gets the value of the callArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallArea() {
        return callArea;
    }

    /**
     * Sets the value of the callArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallArea(String value) {
        this.callArea = value;
    }

    /**
     * Gets the value of the callCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallCategory() {
        return callCategory;
    }

    /**
     * Sets the value of the callCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallCategory(String value) {
        this.callCategory = value;
    }

    /**
     * Gets the value of the callSubCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallSubCategory() {
        return callSubCategory;
    }

    /**
     * Sets the value of the callSubCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallSubCategory(String value) {
        this.callSubCategory = value;
    }

    /**
     * Gets the value of the callActionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallActionType() {
        return callActionType;
    }

    /**
     * Sets the value of the callActionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallActionType(String value) {
        this.callActionType = value;
    }

    /**
     * Gets the value of the serviceAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceAccount() {
        return serviceAccount;
    }

    /**
     * Sets the value of the serviceAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceAccount(String value) {
        this.serviceAccount = value;
    }

    /**
     * Gets the value of the billingAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingAccount() {
        return billingAccount;
    }

    /**
     * Sets the value of the billingAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingAccount(String value) {
        this.billingAccount = value;
    }

    /**
     * Gets the value of the customerAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerAccount() {
        return customerAccount;
    }

    /**
     * Sets the value of the customerAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerAccount(String value) {
        this.customerAccount = value;
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

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the callBack property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallBack() {
        return callBack;
    }

    /**
     * Sets the value of the callBack property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallBack(String value) {
        this.callBack = value;
    }

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarks(String value) {
        this.remarks = value;
    }

    /**
     * Gets the value of the issueDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssueDateTime() {
        return issueDateTime;
    }

    /**
     * Sets the value of the issueDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssueDateTime(XMLGregorianCalendar value) {
        this.issueDateTime = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
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
     * Gets the value of the failureDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureDescription() {
        return failureDescription;
    }

    /**
     * Sets the value of the failureDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureDescription(String value) {
        this.failureDescription = value;
    }

}