//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.05 at 06:34:48 PM IST 
//


package com.oracle.oep.order.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AddOrderM2MType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddOrderM2MType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}MsIsdn"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}SIMDetails" minOccurs="0"/>
 *         &lt;element name="Plan" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}AddActionPlanType" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderName"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderAction"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderStartDate"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderUserId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}OrderServiceType"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ChannelTrackingId" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}AccountNo"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}AccountName"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}BillingAccountId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}CustomerAccountId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}CustomerAccountId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}BillingAccountId"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}BillingAccountName"/>
 *         &lt;element ref="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}CustomerAccountName"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddOrderM2MType", propOrder = {
    "content"
})
public class AddOrderM2MType {

    @XmlElementRefs({
        @XmlElementRef(name = "AccountName", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Plan", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "OrderName", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "MsIsdn", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "OrderAction", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CustomerAccountId", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "OrderStartDate", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "CustomerAccountName", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "OrderServiceType", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ChannelTrackingId", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SIMDetails", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "BillingAccountId", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "OrderUserId", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "BillingAccountName", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AccountNo", namespace = "http://xmlns.oracle.com/OEP/OrderManagement/Order/V1", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "CustomerAccountId" is used by two different parts of a schema. See: 
     * line 694 of file:/D:/DhiraguVM/OrderEntryWebserviceWorksoace_13Nov2017/OrderEntryWebservice/src/main/webapp/WEB-INF/OEP_OrderCapture.xsd
     * line 693 of file:/D:/DhiraguVM/OrderEntryWebserviceWorksoace_13Nov2017/OrderEntryWebservice/src/main/webapp/WEB-INF/OEP_OrderCapture.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link AddActionPlanType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link SIMDetailsType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return this.content;
    }

}