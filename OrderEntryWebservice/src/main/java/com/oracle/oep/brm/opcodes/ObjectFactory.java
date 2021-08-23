//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.28 at 03:13:34 PM IST 
//


package com.oracle.oep.brm.opcodes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.oep.brm.opcodes package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ERRORDESCR_QNAME = new QName("http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", "ERROR_DESCR");
    private final static QName _STATUS_QNAME = new QName("http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", "STATUS");
    private final static QName _POID_QNAME = new QName("http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", "POID");
    private final static QName _ERRORCODE_QNAME = new QName("http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", "ERROR_CODE");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.oep.brm.opcodes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CUSOPCUSTCANCELPLANOutputFlist }
     * 
     */
    public CUSOPCUSTCANCELPLANOutputFlist createCUSOPCUSTCANCELPLANOutputFlist() {
        return new CUSOPCUSTCANCELPLANOutputFlist();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", name = "ERROR_DESCR")
    public JAXBElement<String> createERRORDESCR(String value) {
        return new JAXBElement<String>(_ERRORDESCR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", name = "STATUS")
    public JAXBElement<Integer> createSTATUS(Integer value) {
        return new JAXBElement<Integer>(_STATUS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", name = "POID")
    public JAXBElement<String> createPOID(String value) {
        return new JAXBElement<String>(_POID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/BRM/schemas/BusinessOpcodes", name = "ERROR_CODE")
    public JAXBElement<String> createERRORCODE(String value) {
        return new JAXBElement<String>(_ERRORCODE_QNAME, String.class, null, value);
    }

}