package com.oracle.oep.order.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for OptionsEnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OptionsEnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="1"/>
 *     &lt;enumeration value="2"/>
 *     &lt;enumeration value="3"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OptionsEnumType")
@XmlEnum
public enum OptionsEnumType {

    @XmlEnumValue("1")
    WAIVE_OFF("1"),
    @XmlEnumValue("2")
    ADD_TO_BILL("2"),
    @XmlEnumValue("3")
    TAKE_UPFRONT("3");
    private final String value;

    OptionsEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OptionsEnumType fromValue(String v) {
        for (OptionsEnumType c: OptionsEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}


