//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.12.08 at 06:31:32 PM IST 
//


package com.oracle.oep.brm.opcodes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}POID"/>
 *         &lt;element ref="{}PROGRAM_NAME"/>
 *         &lt;element ref="{}USER_NAME"/>
 *         &lt;element ref="{}FLAGS"/>
 *         &lt;element ref="{}PARAM_NAME"/>
 *         &lt;element ref="{}PARAMS" maxOccurs="unbounded"/>
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
    "programname",
    "username",
    "flags",
    "paramname",
    "params"
})
@XmlRootElement(name = "CUS_OP_SEARCH_inputFlist")
public class CUSOPSEARCHInputFlist {

    @XmlElement(name = "POID", required = true)
    protected String poid;
    @XmlElement(name = "PROGRAM_NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String programname;
    @XmlElement(name = "USER_NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String username;
    @XmlElement(name = "FLAGS", required = true)
    protected BigInteger flags;
    @XmlElement(name = "PARAM_NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String paramname;
    @XmlElement(name = "PARAMS", required = true)
    protected List<PARAMS> params;

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
     * Gets the value of the flags property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFLAGS() {
        return flags;
    }

    /**
     * Sets the value of the flags property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFLAGS(BigInteger value) {
        this.flags = value;
    }

    /**
     * Gets the value of the paramname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARAMNAME() {
        return paramname;
    }

    /**
     * Sets the value of the paramname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARAMNAME(String value) {
        this.paramname = value;
    }

    /**
     * Gets the value of the params property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the params property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPARAMS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PARAMS }
     * 
     * 
     */
    public List<PARAMS> getPARAMS() {
        if (params == null) {
            params = new ArrayList<PARAMS>();
        }
        return this.params;
    }

}