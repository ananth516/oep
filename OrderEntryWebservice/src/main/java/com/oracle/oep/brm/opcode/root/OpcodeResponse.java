//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.14 at 06:35:24 PM IST 
//


package com.oracle.oep.brm.opcode.root;

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
 *       &lt;sequence>
 *         &lt;element name="opcodeResponse" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
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
    "opcodeResponse"
})
@XmlRootElement(name = "opcodeResponse")
public class OpcodeResponse {

    @XmlElement(required = true)
    protected String opcodeResponse;

    /**
     * Gets the value of the opcodeResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpcodeResponse() {
        return opcodeResponse;
    }

    /**
     * Sets the value of the opcodeResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpcodeResponse(String value) {
        this.opcodeResponse = value;
    }

}
