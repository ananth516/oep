//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.06 at 03:56:34 PM IST 
//



package com.oracle.oep.brm.opcodes;

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
 *         &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="POID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PROGRAM_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="USER_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CUS_FLD_PLAN_TYPE" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
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
    "msisdn",
    "name",
    "poid",
    "programname",
    "username",
    "cusfldplantype"
})
@XmlRootElement(name = "CUS_OP_CUST_INCOMPATIBLE_ADDON_inputFlist")
public class CUSOPCUSTINCOMPATIBLEADDONInputFlist {

    @XmlElement(name = "MSISDN", required = true)
    protected String msisdn;
    @XmlElement(name = "NAME", required = true)
    protected String name;
    @XmlElement(name = "POID", required = true)
    protected String poid;
    @XmlElement(name = "PROGRAM_NAME", required = true)
    protected String programname;
    @XmlElement(name = "USER_NAME", required = true)
    protected String username;
    @XmlElement(name = "CUS_FLD_PLAN_TYPE")
    protected Byte cusfldplantype;

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSISDN() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSISDN(String value) {
        this.msisdn = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAME() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAME(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the poid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOID() {
        return poid;
    }

    /**
     * Sets the value of the poid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOID(String value) {
        this.poid = value;
    }

    /**
     * Gets the value of the programname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROGRAMNAME() {
        return programname;
    }

    /**
     * Sets the value of the programname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROGRAMNAME(String value) {
        this.programname = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERNAME() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERNAME(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the cusfldplantype property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getCUSFLDPLANTYPE() {
        return cusfldplantype;
    }

    /**
     * Sets the value of the cusfldplantype property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setCUSFLDPLANTYPE(Byte value) {
        this.cusfldplantype = value;
    }

}
