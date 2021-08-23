//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.28 at 02:44:56 PM IST 
//


package com.oracle.oep.brm.opcodes;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}POID"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}ACCOUNT_NO"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}PROGRAM_NAME"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}USER_NAME"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}SERVICE_ID"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}MSISDN"/>
 *         &lt;element ref="{http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes}PLAN" maxOccurs="unbounded"/>
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
    "msisdn",
    "plan"
})
@XmlRootElement(name = "CUS_OP_CUST_CANCEL_PLAN_inputFlist")
public class CUSOPCUSTCANCELPLANInputFlist {

    @XmlElement(name = "POID", required = true)
    protected String poid;
    @XmlElement(name = "ACCOUNT_NO", required = true)
    protected String accountno;
    @XmlElement(name = "PROGRAM_NAME", required = true)
    protected String programname;
    @XmlElement(name = "USER_NAME", required = true)
    protected String username;
    @XmlElement(name = "SERVICE_ID", required = true)
    protected String serviceid;
    @XmlElement(name = "MSISDN", required = true)
    protected String msisdn;
    @XmlElement(name = "PLAN", required = true)
    protected List<PLAN> plan;

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
     *     {@link String }
     *     
     */
    public String getSERVICEID() {
        return serviceid;
    }

    /**
     * Sets the value of the serviceid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERVICEID(String value) {
        this.serviceid = value;
    }

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
     *    getPLAN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PLAN }
     * 
     * 
     */
    public List<PLAN> getPLAN() {
        if (plan == null) {
            plan = new ArrayList<PLAN>();
        }
        return this.plan;
    }

}
