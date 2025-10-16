
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OznakaSlijednostiType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OznakaSlijednostiType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="P"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OznakaSlijednostiType")
@XmlEnum
public enum OznakaSlijednostiType {

    N,
    P;

    public String value() {
        return name();
    }

    public static OznakaSlijednostiType fromValue(String v) {
        return valueOf(v);
    }

}
