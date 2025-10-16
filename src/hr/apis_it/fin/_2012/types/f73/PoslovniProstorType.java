
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoslovniProstorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoslovniProstorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Oib" type="{http://www.apis-it.hr/fin/2012/types/f73}OibType"/>
 *         &lt;element name="OznPoslProstora" type="{http://www.apis-it.hr/fin/2012/types/f73}OznPoslProstoraType"/>
 *         &lt;element name="AdresniPodatak" type="{http://www.apis-it.hr/fin/2012/types/f73}AdresniPodatakType"/>
 *         &lt;element name="RadnoVrijeme">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumPocetkaPrimjene" type="{http://www.apis-it.hr/fin/2012/types/f73}DatumType"/>
 *         &lt;element name="OznakaZatvaranja" type="{http://www.apis-it.hr/fin/2012/types/f73}OznakaZatvaranjaType" minOccurs="0"/>
 *         &lt;element name="SpecNamj" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoslovniProstorType", propOrder = {
    "oib",
    "oznPoslProstora",
    "adresniPodatak",
    "radnoVrijeme",
    "datumPocetkaPrimjene",
    "oznakaZatvaranja",
    "specNamj"
})
public class PoslovniProstorType {

    @XmlElement(name = "Oib", required = true)
    protected String oib;
    @XmlElement(name = "OznPoslProstora", required = true)
    protected String oznPoslProstora;
    @XmlElement(name = "AdresniPodatak", required = true)
    protected AdresniPodatakType adresniPodatak;
    @XmlElement(name = "RadnoVrijeme", required = true)
    protected String radnoVrijeme;
    @XmlElement(name = "DatumPocetkaPrimjene", required = true)
    protected String datumPocetkaPrimjene;
    @XmlElement(name = "OznakaZatvaranja")
    protected OznakaZatvaranjaType oznakaZatvaranja;
    @XmlElement(name = "SpecNamj")
    protected String specNamj;

    /**
     * Gets the value of the oib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOib() {
        return oib;
    }

    /**
     * Sets the value of the oib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOib(String value) {
        this.oib = value;
    }

    /**
     * Gets the value of the oznPoslProstora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOznPoslProstora() {
        return oznPoslProstora;
    }

    /**
     * Sets the value of the oznPoslProstora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOznPoslProstora(String value) {
        this.oznPoslProstora = value;
    }

    /**
     * Gets the value of the adresniPodatak property.
     * 
     * @return
     *     possible object is
     *     {@link AdresniPodatakType }
     *     
     */
    public AdresniPodatakType getAdresniPodatak() {
        return adresniPodatak;
    }

    /**
     * Sets the value of the adresniPodatak property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresniPodatakType }
     *     
     */
    public void setAdresniPodatak(AdresniPodatakType value) {
        this.adresniPodatak = value;
    }

    /**
     * Gets the value of the radnoVrijeme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRadnoVrijeme() {
        return radnoVrijeme;
    }

    /**
     * Sets the value of the radnoVrijeme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRadnoVrijeme(String value) {
        this.radnoVrijeme = value;
    }

    /**
     * Gets the value of the datumPocetkaPrimjene property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumPocetkaPrimjene() {
        return datumPocetkaPrimjene;
    }

    /**
     * Sets the value of the datumPocetkaPrimjene property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumPocetkaPrimjene(String value) {
        this.datumPocetkaPrimjene = value;
    }

    /**
     * Gets the value of the oznakaZatvaranja property.
     * 
     * @return
     *     possible object is
     *     {@link OznakaZatvaranjaType }
     *     
     */
    public OznakaZatvaranjaType getOznakaZatvaranja() {
        return oznakaZatvaranja;
    }

    /**
     * Sets the value of the oznakaZatvaranja property.
     * 
     * @param value
     *     allowed object is
     *     {@link OznakaZatvaranjaType }
     *     
     */
    public void setOznakaZatvaranja(OznakaZatvaranjaType value) {
        this.oznakaZatvaranja = value;
    }

    /**
     * Gets the value of the specNamj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecNamj() {
        return specNamj;
    }

    /**
     * Sets the value of the specNamj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecNamj(String value) {
        this.specNamj = value;
    }

}
