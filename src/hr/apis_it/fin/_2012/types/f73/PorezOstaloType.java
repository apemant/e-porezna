
package hr.apis_it.fin._2012.types.f73;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PorezOstaloType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PorezOstaloType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Naziv">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Stopa" type="{http://www.apis-it.hr/fin/2012/types/f73}StopaType"/>
 *         &lt;element name="Osnovica" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType"/>
 *         &lt;element name="Iznos" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PorezOstaloType", propOrder = {
    "naziv",
    "stopa",
    "osnovica",
    "iznos"
})
public class PorezOstaloType {

    @XmlElement(name = "Naziv", required = true)
    protected String naziv;
    @XmlElement(name = "Stopa", required = true)
    protected BigDecimal stopa;
    @XmlElement(name = "Osnovica", required = true)
    protected BigDecimal osnovica;
    @XmlElement(name = "Iznos", required = true)
    protected BigDecimal iznos;

    /**
     * Gets the value of the naziv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Sets the value of the naziv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaziv(String value) {
        this.naziv = value;
    }

    /**
     * Gets the value of the stopa property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStopa() {
        return stopa;
    }

    /**
     * Sets the value of the stopa property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStopa(BigDecimal value) {
        this.stopa = value;
    }

    /**
     * Gets the value of the osnovica property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOsnovica() {
        return osnovica;
    }

    /**
     * Sets the value of the osnovica property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOsnovica(BigDecimal value) {
        this.osnovica = value;
    }

    /**
     * Gets the value of the iznos property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIznos() {
        return iznos;
    }

    /**
     * Sets the value of the iznos property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIznos(BigDecimal value) {
        this.iznos = value;
    }

}
