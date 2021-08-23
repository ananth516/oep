//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.03 at 07:57:08 PM IST 
//


package com.oracle.oep.brm.opcodes;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}POID"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}ACCOUNT_NO"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}PROGRAM_NAME"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}USER_NAME"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}SERVICE_ID"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}NAME"/>
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
    "poid",
    "accountno",
    "programname",
    "username",
    "serviceid",
    "name"
})
@XmlRootElement(name = "CUS_OP_CUST_VALIDATE_CHANGE_PLAN_inputFlist")
public class CUSOPCUSTVALIDATECHANGEPLANInputFlist {

    @XmlElement(name = "POID", required = true)
    protected String poid;
    @XmlElement(name = "ACCOUNT_NO", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String accountno;
    @XmlElement(name = "PROGRAM_NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String programname;
    @XmlElement(name = "USER_NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String username;
    @XmlElement(name = "SERVICE_ID", required = true)
    protected String serviceid;
    @XmlElement(name = "NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

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
     * Gets the value of the accountno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUNTNO() {
        return accountno;
    }

    /**
     * Sets the value of the accountno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUNTNO(String value) {
        this.accountno = value;
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
     * Gets the value of the serviceid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public String getSERVICEID() {
        return serviceid;
    }

    /**
     * Sets the value of the serviceid property.
     * 
     * @param string
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSERVICEID(String serviceid) {
        this.serviceid = serviceid;
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

}
