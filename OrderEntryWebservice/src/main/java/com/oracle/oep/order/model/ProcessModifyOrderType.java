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
 * <p>Java class for ProcessModifyOrderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcessModifyOrderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ListOfOEPOrder" type="{http://xmlns.oracle.com/OEP/OrderManagement/Order/V1}ListOfOEPOrderModifyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessModifyOrderType", propOrder = {
    "listOfOEPOrder"
})
public class ProcessModifyOrderType {

    @XmlElement(name = "ListOfOEPOrder", required = true)
    protected ListOfOEPOrderModifyType listOfOEPOrder;

    /**
     * Gets the value of the listOfOEPOrder property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfOEPOrderModifyType }
     *     
     */
    public ListOfOEPOrderModifyType getListOfOEPOrder() {
        return listOfOEPOrder;
    }

    /**
     * Sets the value of the listOfOEPOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfOEPOrderModifyType }
     *     
     */
    public void setListOfOEPOrder(ListOfOEPOrderModifyType value) {
        this.listOfOEPOrder = value;
    }

}