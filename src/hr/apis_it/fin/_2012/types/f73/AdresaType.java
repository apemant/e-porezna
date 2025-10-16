
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdresaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdresaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Ulica" type="{http://www.apis-it.hr/fin/2012/types/f73}String100Type" minOccurs="0"/>
 *         &lt;element name="KucniBroj" type="{http://www.apis-it.hr/fin/2012/types/f73}String4BrojType" minOccurs="0"/>
 *         &lt;element name="KucniBrojDodatak" type="{http://www.apis-it.hr/fin/2012/types/f73}String4Type" minOccurs="0"/>
 *         &lt;element name="BrojPoste" type="{http://www.apis-it.hr/fin/2012/types/f73}String12BrojType" minOccurs="0"/>
 *         &lt;element name="Naselje" type="{http://www.apis-it.hr/fin/2012/types/f73}String35Type" minOccurs="0"/>
 *         &lt;element name="Opcina" type="{http://www.apis-it.hr/fin/2012/types/f73}String35Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdresaType", propOrder = {
    "ulica",
    "kucniBroj",
    "kucniBrojDodatak",
    "brojPoste",
    "naselje",
    "opcina"
})
public class AdresaType {

    @XmlElement(name = "Ulica")
    protected String ulica;
    @XmlElement(name = "KucniBroj")
    protected String kucniBroj;
    @XmlElement(name = "KucniBrojDodatak")
    protected String kucniBrojDodatak;
    @XmlElement(name = "BrojPoste")
    protected String brojPoste;
    @XmlElement(name = "Naselje")
    protected String naselje;
    @XmlElement(name = "Opcina")
    protected String opcina;

    /**
     * Gets the value of the ulica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUlica() {
        return ulica;
    }

    /**
     * Sets the value of the ulica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUlica(String value) {
        this.ulica = value;
    }

    /**
     * Gets the value of the kucniBroj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKucniBroj() {
        return kucniBroj;
    }

    /**
     * Sets the value of the kucniBroj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKucniBroj(String value) {
        this.kucniBroj = value;
    }

    /**
     * Gets the value of the kucniBrojDodatak property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKucniBrojDodatak() {
        return kucniBrojDodatak;
    }

    /**
     * Sets the value of the kucniBrojDodatak property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKucniBrojDodatak(String value) {
        this.kucniBrojDodatak = value;
    }

    /**
     * Gets the value of the brojPoste property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrojPoste() {
        return brojPoste;
    }

    /**
     * Sets the value of the brojPoste property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrojPoste(String value) {
        this.brojPoste = value;
    }

    /**
     * Gets the value of the naselje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaselje() {
        return naselje;
    }

    /**
     * Sets the value of the naselje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaselje(String value) {
        this.naselje = value;
    }

    /**
     * Gets the value of the opcina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpcina() {
        return opcina;
    }

    /**
     * Sets the value of the opcina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpcina(String value) {
        this.opcina = value;
    }

}
