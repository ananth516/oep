//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.05 at 06:34:48 PM IST 
//


package com.oracle.oep.order.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CallDivertActionEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CallDivertActionEnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Activate"/>
 *     &lt;enumeration value="DeActivate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CallDivertActionEnumType")
@XmlEnum
public enum CallDivertActionEnumType {

    @XmlEnumValue("Activate")
    ACTIVATE("Activate"),
    @XmlEnumValue("DeActivate")
    DE_ACTIVATE("DeActivate");
    private final String value;

    CallDivertActionEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CallDivertActionEnumType fromValue(String v) {
        for (CallDivertActionEnumType c: CallDivertActionEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
