//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.28 at 09:36:50 AM IST 
//


package com.oracle.oep.order.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AddOrderShortCodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddOrderShortCodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ShortCodeNumber"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ShortCodeType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ShortCodeCategory" minOccurs="0"/>
 *         &lt;element name="Plan" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}AddActionPlanType" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderName"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderAction"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderStartDate"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderUserId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderServiceType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ChannelTrackingId" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}AccountNo"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}EmailId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}AccountName"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}CustomerAccountId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}BillingAccountId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}BillingAccountName" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}CustomerAccountName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddOrderShortCodeType", propOrder = {
    "shortCodeNumber",
    "shortCodeType",
    "shortCodeCategory",
    "plan",
    "orderName",
    "orderAction",
    "orderStartDate",
    "orderUserId",
    "orderServiceType",
    "channelTrackingId",
    "accountNo",
    "emailId",
    "accountName",
    "customerAccountId",
    "billingAccountId",
    "billingAccountName",
    "customerAccountName"
})
public class AddOrderShortCodeType {

    @XmlElement(name = "ShortCodeNumber", required = true)
    protected String shortCodeNumber;
    @XmlElement(name = "ShortCodeType", required = true)
    protected String shortCodeType;
    @XmlElement(name = "ShortCodeCategory")
    protected String shortCodeCategory;
    @XmlElement(name = "Plan", required = true)
    protected List<AddActionPlanType> plan;
    @XmlElement(name = "OrderName", required = true)
    protected String orderName;
    @XmlElement(name = "OrderAction", required = true)
    protected String orderAction;
    @XmlElement(name = "OrderStartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar orderStartDate;
    @XmlElement(name = "OrderUserId", required = true)
    protected String orderUserId;
    @XmlElement(name = "OrderServiceType", required = true)
    protected String orderServiceType;
    @XmlElement(name = "ChannelTrackingId")
    protected String channelTrackingId;
    @XmlElement(name = "AccountNo", required = true)
    protected String accountNo;
    @XmlElement(name = "EmailId", required = true)
    protected String emailId;
    @XmlElement(name = "AccountName", required = true)
    protected String accountName;
    @XmlElement(name = "CustomerAccountId", required = true)
    protected String customerAccountId;
    @XmlElement(name = "BillingAccountId", required = true)
    protected String billingAccountId;
    @XmlElement(name = "BillingAccountName")
    protected String billingAccountName;
    @XmlElement(name = "CustomerAccountName")
    protected String customerAccountName;

    /**
     * Gets the value of the shortCodeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortCodeNumber() {
        return shortCodeNumber;
    }

    /**
     * Sets the value of the shortCodeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortCodeNumber(String value) {
        this.shortCodeNumber = value;
    }

    /**
     * Gets the value of the shortCodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortCodeType() {
        return shortCodeType;
    }

    /**
     * Sets the value of the shortCodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortCodeType(String value) {
        this.shortCodeType = value;
    }

    /**
     * Gets the value of the shortCodeCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortCodeCategory() {
        return shortCodeCategory;
    }

    /**
     * Sets the value of the shortCodeCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortCodeCategory(String value) {
        this.shortCodeCategory = value;
    }

    /**
     * Gets the value of the plan property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the plan property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlan().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AddActionPlanType }
     * 
     * 
     */
    public List<AddActionPlanType> getPlan() {
        if (plan == null) {
            plan = new ArrayList<AddActionPlanType>();
        }
        return this.plan;
    }

    /**
     * Gets the value of the orderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderName() {
        return orderName;
    }

    /**
     * Sets the value of the orderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderName(String value) {
        this.orderName = value;
    }

    /**
     * Gets the value of the orderAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderAction() {
        return orderAction;
    }

    /**
     * Sets the value of the orderAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderAction(String value) {
        this.orderAction = value;
    }

    /**
     * Gets the value of the orderStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderStartDate() {
        return orderStartDate;
    }

    /**
     * Sets the value of the orderStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderStartDate(XMLGregorianCalendar value) {
        this.orderStartDate = value;
    }

    /**
     * Gets the value of the orderUserId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderUserId() {
        return orderUserId;
    }

    /**
     * Sets the value of the orderUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderUserId(String value) {
        this.orderUserId = value;
    }

    /**
     * Gets the value of the orderServiceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderServiceType() {
        return orderServiceType;
    }

    /**
     * Sets the value of the orderServiceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderServiceType(String value) {
        this.orderServiceType = value;
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
     * Gets the value of the emailId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Sets the value of the emailId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailId(String value) {
        this.emailId = value;
    }

    /**
     * Gets the value of the accountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the value of the accountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * Gets the value of the customerAccountId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerAccountId() {
        return customerAccountId;
    }

    /**
     * Sets the value of the customerAccountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerAccountId(String value) {
        this.customerAccountId = value;
    }

    /**
     * Gets the value of the billingAccountId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingAccountId() {
        return billingAccountId;
    }

    /**
     * Sets the value of the billingAccountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingAccountId(String value) {
        this.billingAccountId = value;
    }

    /**
     * Gets the value of the billingAccountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingAccountName() {
        return billingAccountName;
    }

    /**
     * Sets the value of the billingAccountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingAccountName(String value) {
        this.billingAccountName = value;
    }

    /**
     * Gets the value of the customerAccountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerAccountName() {
        return customerAccountName;
    }

    /**
     * Sets the value of the customerAccountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerAccountName(String value) {
        this.customerAccountName = value;
    }

}
