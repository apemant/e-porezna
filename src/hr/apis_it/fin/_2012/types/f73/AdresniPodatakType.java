
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdresniPodatakType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdresniPodatakType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Adresa" type="{http://www.apis-it.hr/fin/2012/types/f73}AdresaType"/>
 *         &lt;element name="OstaliTipoviPP" type="{http://www.apis-it.hr/fin/2012/types/f73}String100Type"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdresniPodatakType", propOrder = {
    "adresa",
    "ostaliTipoviPP"
})
public class AdresniPodatakType {

    @XmlElement(name = "Adresa")
    protected AdresaType adresa;
    @XmlElement(name = "OstaliTipoviPP")
    protected String ostaliTipoviPP;

    /**
     * Gets the value of the adresa property.
     * 
     * @return
     *     possible object is
     *     {@link AdresaType }
     *     
     */
    public AdresaType getAdresa() {
        return adresa;
    }

    /**
     * Sets the value of the adresa property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresaType }
     *     
     */
    public void setAdresa(AdresaType value) {
        this.adresa = value;
    }

    /**
     * Gets the value of the ostaliTipoviPP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOstaliTipoviPP() {
        return ostaliTipoviPP;
    }

    /**
     * Sets the value of the ostaliTipoviPP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOstaliTipoviPP(String value) {
        this.ostaliTipoviPP = value;
    }

}
