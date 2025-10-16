
package hr.apis_it.fin._2012.types.f73;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NaknadaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NaknadaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NazivN">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IznosN" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NaknadaType", propOrder = {
    "nazivN",
    "iznosN"
})
public class NaknadaType {

    @XmlElement(name = "NazivN", required = true)
    protected String nazivN;
    @XmlElement(name = "IznosN", required = true)
    protected BigDecimal iznosN;

    /**
     * Gets the value of the nazivN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazivN() {
        return nazivN;
    }

    /**
     * Sets the value of the nazivN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazivN(String value) {
        this.nazivN = value;
    }

    /**
     * Gets the value of the iznosN property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIznosN() {
        return iznosN;
    }

    /**
     * Sets the value of the iznosN property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIznosN(BigDecimal value) {
        this.iznosN = value;
    }

}
