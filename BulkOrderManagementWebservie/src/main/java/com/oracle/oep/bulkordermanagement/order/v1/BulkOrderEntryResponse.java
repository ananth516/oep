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
 *       &lt;choice>
 *         &lt;element name="ProcessBulkOrderResponse" type="{http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1}ProcessBulkOrderResponseType"/>
 *         &lt;element name="QueryWithFileOrderIdResponse" type="{http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1}QueryWithFileOrderIdResponseType"/>
 *         &lt;element name="QueryWithBatchIdResponse" type="{http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1}QueryWithBatchIdResponseType"/>
 *         &lt;element name="QueryFailedRecordsResponse" type="{http://xmlns.oracle.com.mv/OEP/BulkOrderManagement/Order/V1}QueryFailedRecordsResponseType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "processBulkOrderResponse",
    "queryWithFileOrderIdResponse",
    "queryWithBatchIdResponse",
    "queryFailedRecordsResponse"
})
@XmlRootElement(name = "BulkOrderEntryResponse")
public class BulkOrderEntryResponse {

    @XmlElement(name = "ProcessBulkOrderResponse")
    protected ProcessBulkOrderResponseType processBulkOrderResponse;
    @XmlElement(name = "QueryWithFileOrderIdResponse")
    protected QueryWithFileOrderIdResponseType queryWithFileOrderIdResponse;
    @XmlElement(name = "QueryWithBatchIdResponse")
    protected QueryWithBatchIdResponseType queryWithBatchIdResponse;
    @XmlElement(name = "QueryFailedRecordsResponse")
    protected QueryFailedRecordsResponseType queryFailedRecordsResponse;

    /**
     * Gets the value of the processBulkOrderResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessBulkOrderResponseType }
     *     
     */
    public ProcessBulkOrderResponseType getProcessBulkOrderResponse() {
        return processBulkOrderResponse;
    }

    /**
     * Sets the value of the processBulkOrderResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessBulkOrderResponseType }
     *     
     */
    public void setProcessBulkOrderResponse(ProcessBulkOrderResponseType value) {
        this.processBulkOrderResponse = value;
    }

    /**
     * Gets the value of the queryWithFileOrderIdResponse property.
     * 
     * @return
     *     possible object is
     *     {@link QueryWithFileOrderIdResponseType }
     *     
     */
    public QueryWithFileOrderIdResponseType getQueryWithFileOrderIdResponse() {
        return queryWithFileOrderIdResponse;
    }

    /**
     * Sets the value of the queryWithFileOrderIdResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryWithFileOrderIdResponseType }
     *     
     */
    public void setQueryWithFileOrderIdResponse(QueryWithFileOrderIdResponseType value) {
        this.queryWithFileOrderIdResponse = value;
    }

    /**
     * Gets the value of the queryWithBatchIdResponse property.
     * 
     * @return
     *     possible object is
     *     {@link QueryWithBatchIdResponseType }
     *     
     */
    public QueryWithBatchIdResponseType getQueryWithBatchIdResponse() {
        return queryWithBatchIdResponse;
    }

    /**
     * Sets the value of the queryWithBatchIdResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryWithBatchIdResponseType }
     *     
     */
    public void setQueryWithBatchIdResponse(QueryWithBatchIdResponseType value) {
        this.queryWithBatchIdResponse = value;
    }

    /**
     * Gets the value of the queryFailedRecordsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link QueryFailedRecordsResponseType }
     *     
     */
    public QueryFailedRecordsResponseType getQueryFailedRecordsResponse() {
        return queryFailedRecordsResponse;
    }

    /**
     * Sets the value of the queryFailedRecordsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryFailedRecordsResponseType }
     *     
     */
    public void setQueryFailedRecordsResponse(QueryFailedRecordsResponseType value) {
        this.queryFailedRecordsResponse = value;
    }

}