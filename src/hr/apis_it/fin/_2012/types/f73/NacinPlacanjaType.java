
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NacinPlacanjaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NacinPlacanjaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="K"/>
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="T"/>
 *     &lt;enumeration value="O"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NacinPlacanjaType")
@XmlEnum
public enum NacinPlacanjaType {

    G,
    K,
    C,
    T,
    O;

    public String value() {
        return name();
    }

    public static NacinPlacanjaType fromValue(String v) {
        return valueOf(v);
    }

}
