//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.07.26 at 11:37:52 AM IST 
//


package com.oracle.oep.bulkordermanagement.order.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QueryWithBatchIdRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QueryWithBatchIdRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QueryBatches" type="{http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1}QueryWithBatchIdType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryWithBatchIdRequestType", propOrder = {
    "queryBatches"
})
public class QueryWithBatchIdRequestType {

    @XmlElement(name = "QueryBatches", required = true)
    protected QueryWithBatchIdType queryBatches;

    /**
     * Gets the value of the queryBatches property.
     * 
     * @return
     *     possible object is
     *     {@link QueryWithBatchIdType }
     *     
     */
    public QueryWithBatchIdType getQueryBatches() {
        return queryBatches;
    }

    /**
     * Sets the value of the queryBatches property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryWithBatchIdType }
     *     
     */
    public void setQueryBatches(QueryWithBatchIdType value) {
        this.queryBatches = value;
    }

}