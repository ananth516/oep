//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.23 at 12:02:59 PM IST 
//


package com.oracle.oep.viewusage.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.oep.viewusage.xsd package. 
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

    private final static QName _CUSFLDSERVICEIDENTIFIER_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "CUS_FLD_SERVICE_IDENTIFIER");
    private final static QName _AMOUNT_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "AMOUNT");
    private final static QName _STATUS_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "STATUS");
    private final static QName _CALLEDTO_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "CALLED_TO");
    private final static QName _CUSFLDUNITNAME_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "CUS_FLD_UNIT_NAME");
    private final static QName _POID_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "POID");
    private final static QName _USAGEENDT_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "USAGE_END_T");
    private final static QName _DESCR_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "DESCR");
    private final static QName _EVENTNO_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "EVENT_NO");
    private final static QName _USAGECATEGORY_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "USAGE_CATEGORY");
    private final static QName _USAGESTARTT_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "USAGE_START_T");
    private final static QName _PROGRAMNAME_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "PROGRAM_NAME");
    private final static QName _RESOURCEID_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "RESOURCE_ID");
    private final static QName _QUANTITY_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "QUANTITY");
    private final static QName _OFFERNAME_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "OFFER_NAME");
    private final static QName _USAGETYPE_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "USAGE_TYPE");
    private final static QName _IMPACTCATEGORY_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "IMPACT_CATEGORY");
    private final static QName _STARTT_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "START_T");
    private final static QName _ENDT_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "END_T");
    private final static QName _TERMINATECAUSE_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "TERMINATE_CAUSE");
    private final static QName _USERNAME_QNAME = new QName("http://xmlns.oracle.com/OEP/ViewUsage/V1", "USER_NAME");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.oep.viewusage.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CHARGES }
     * 
     */
    public CHARGES createCHARGES() {
        return new CHARGES();
    }

    /**
     * Create an instance of {@link CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest }
     * 
     */
    public CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest createCUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest() {
        return new CUSOPSUBSCRIPTIONVIEWUSAGEInputFlistRequest();
    }

    /**
     * Create an instance of {@link CUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse }
     * 
     */
    public CUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse createCUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse() {
        return new CUSOPSUBSCRIPTIONVIEWUSAGEOutputFlistResponse();
    }

    /**
     * Create an instance of {@link USAGERECORDS }
     * 
     */
    public USAGERECORDS createUSAGERECORDS() {
        return new USAGERECORDS();
    }

    /**
     * Create an instance of {@link ORIGINSID }
     * 
     */
    public ORIGINSID createORIGINSID() {
        return new ORIGINSID();
    }

    /**
     * Create an instance of {@link LOCAREACODE }
     * 
     */
    public LOCAREACODE createLOCAREACODE() {
        return new LOCAREACODE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "CUS_FLD_SERVICE_IDENTIFIER")
    public JAXBElement<String> createCUSFLDSERVICEIDENTIFIER(String value) {
        return new JAXBElement<String>(_CUSFLDSERVICEIDENTIFIER_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "AMOUNT")
    public JAXBElement<Integer> createAMOUNT(Integer value) {
        return new JAXBElement<Integer>(_AMOUNT_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "STATUS")
    public JAXBElement<Integer> createSTATUS(Integer value) {
        return new JAXBElement<Integer>(_STATUS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "CALLED_TO")
    public JAXBElement<Integer> createCALLEDTO(Integer value) {
        return new JAXBElement<Integer>(_CALLEDTO_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "CUS_FLD_UNIT_NAME")
    public JAXBElement<String> createCUSFLDUNITNAME(String value) {
        return new JAXBElement<String>(_CUSFLDUNITNAME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "POID")
    public JAXBElement<String> createPOID(String value) {
        return new JAXBElement<String>(_POID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "USAGE_END_T")
    public JAXBElement<Long> createUSAGEENDT(Long value) {
        return new JAXBElement<Long>(_USAGEENDT_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "DESCR")
    public JAXBElement<String> createDESCR(String value) {
        return new JAXBElement<String>(_DESCR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "EVENT_NO")
    public JAXBElement<String> createEVENTNO(String value) {
        return new JAXBElement<String>(_EVENTNO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "USAGE_CATEGORY")
    public JAXBElement<Integer> createUSAGECATEGORY(Integer value) {
        return new JAXBElement<Integer>(_USAGECATEGORY_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "USAGE_START_T")
    public JAXBElement<Long> createUSAGESTARTT(Long value) {
        return new JAXBElement<Long>(_USAGESTARTT_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "PROGRAM_NAME")
    public JAXBElement<String> createPROGRAMNAME(String value) {
        return new JAXBElement<String>(_PROGRAMNAME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "RESOURCE_ID")
    public JAXBElement<Integer> createRESOURCEID(Integer value) {
        return new JAXBElement<Integer>(_RESOURCEID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "QUANTITY")
    public JAXBElement<Integer> createQUANTITY(Integer value) {
        return new JAXBElement<Integer>(_QUANTITY_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "OFFER_NAME")
    public JAXBElement<String> createOFFERNAME(String value) {
        return new JAXBElement<String>(_OFFERNAME_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "USAGE_TYPE")
    public JAXBElement<String> createUSAGETYPE(String value) {
        return new JAXBElement<String>(_USAGETYPE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "IMPACT_CATEGORY")
    public JAXBElement<String> createIMPACTCATEGORY(String value) {
        return new JAXBElement<String>(_IMPACTCATEGORY_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "START_T")
    public JAXBElement<Long> createSTARTT(Long value) {
        return new JAXBElement<Long>(_STARTT_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "END_T")
    public JAXBElement<Long> createENDT(Long value) {
        return new JAXBElement<Long>(_ENDT_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "TERMINATE_CAUSE")
    public JAXBElement<Integer> createTERMINATECAUSE(Integer value) {
        return new JAXBElement<Integer>(_TERMINATECAUSE_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/OEP/ViewUsage/V1", name = "USER_NAME")
    public JAXBElement<String> createUSERNAME(String value) {
        return new JAXBElement<String>(_USERNAME_QNAME, String.class, null, value);
    }

}
