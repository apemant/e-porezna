
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OznakaZatvaranjaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OznakaZatvaranjaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Z"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OznakaZatvaranjaType")
@XmlEnum
public enum OznakaZatvaranjaType {

    Z;

    public String value() {
        return name();
    }

    public static OznakaZatvaranjaType fromValue(String v) {
        return valueOf(v);
    }

}
