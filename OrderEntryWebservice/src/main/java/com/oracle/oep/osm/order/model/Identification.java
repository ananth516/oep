//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.08 at 02:50:19 PM IST 
//


package com.oracle.oep.osm.order.model;

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
 *     &lt;extension base="{http://xmlns.oracle.com/EnterpriseObjects/Core/EBO/SalesOrder/V2}ID">
 *       &lt;sequence>
 *         &lt;element ref="{http://xmlns.oracle.com/EnterpriseObjects/Core/EBO/SalesOrder/V2}Revision" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "revision"
})
@XmlRootElement(name = "Identification")
public class Identification
    extends ID
{

    @XmlElement(name = "Revision")
    protected Revision revision;

    /**
     * Gets the value of the revision property.
     * 
     * @return
     *     possible object is
     *     {@link Revision }
     *     
     */
    public Revision getRevision() {
        return revision;
    }

    /**
     * Sets the value of the revision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Revision }
     *     
     */
    public void setRevision(Revision value) {
        this.revision = value;
    }

}
