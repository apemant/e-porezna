
package hr.apis_it.fin._2012.types.f73;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RacunType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RacunType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Oib" type="{http://www.apis-it.hr/fin/2012/types/f73}OibType"/>
 *         &lt;element name="USustPdv" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="DatVrijeme" type="{http://www.apis-it.hr/fin/2012/types/f73}DatumVrijemeType"/>
 *         &lt;element name="OznSlijed" type="{http://www.apis-it.hr/fin/2012/types/f73}OznakaSlijednostiType"/>
 *         &lt;element name="BrRac" type="{http://www.apis-it.hr/fin/2012/types/f73}BrojRacunaType"/>
 *         &lt;element name="Pdv" type="{http://www.apis-it.hr/fin/2012/types/f73}PdvType" minOccurs="0"/>
 *         &lt;element name="Pnp" type="{http://www.apis-it.hr/fin/2012/types/f73}PorezNaPotrosnjuType" minOccurs="0"/>
 *         &lt;element name="OstaliPor" type="{http://www.apis-it.hr/fin/2012/types/f73}OstaliPoreziType" minOccurs="0"/>
 *         &lt;element name="IznosOslobPdv" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType" minOccurs="0"/>
 *         &lt;element name="IznosMarza" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType" minOccurs="0"/>
 *         &lt;element name="IznosNePodlOpor" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType" minOccurs="0"/>
 *         &lt;element name="Naknade" type="{http://www.apis-it.hr/fin/2012/types/f73}NaknadeType" minOccurs="0"/>
 *         &lt;element name="IznosUkupno" type="{http://www.apis-it.hr/fin/2012/types/f73}IznosType"/>
 *         &lt;element name="NacinPlac" type="{http://www.apis-it.hr/fin/2012/types/f73}NacinPlacanjaType"/>
 *         &lt;element name="OibOper" type="{http://www.apis-it.hr/fin/2012/types/f73}OibType"/>
 *         &lt;element name="ZastKod">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[a-f0-9]{32}"/>
 *               &lt;length value="32"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NakDost" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ParagonBrRac" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
@XmlType(name = "RacunType", propOrder = {
    "oib",
    "uSustPdv",
    "datVrijeme",
    "oznSlijed",
    "brRac",
    "pdv",
    "pnp",
    "ostaliPor",
    "iznosOslobPdv",
    "iznosMarza",
    "iznosNePodlOpor",
    "naknade",
    "iznosUkupno",
    "nacinPlac",
    "oibOper",
    "zastKod",
    "nakDost",
    "paragonBrRac",
    "specNamj"
})
public class RacunType {

    @XmlElement(name = "Oib", required = true)
    protected String oib;
    @XmlElement(name = "USustPdv")
    protected boolean uSustPdv;
    @XmlElement(name = "DatVrijeme", required = true)
    protected String datVrijeme;
    @XmlElement(name = "OznSlijed", required = true)
    protected OznakaSlijednostiType oznSlijed;
    @XmlElement(name = "BrRac", required = true)
    protected BrojRacunaType brRac;
    @XmlElement(name = "Pdv")
    protected PdvType pdv;
    @XmlElement(name = "Pnp")
    protected PorezNaPotrosnjuType pnp;
    @XmlElement(name = "OstaliPor")
    protected OstaliPoreziType ostaliPor;
    @XmlElement(name = "IznosOslobPdv")
    protected BigDecimal iznosOslobPdv;
    @XmlElement(name = "IznosMarza")
    protected BigDecimal iznosMarza;
    @XmlElement(name = "IznosNePodlOpor")
    protected BigDecimal iznosNePodlOpor;
    @XmlElement(name = "Naknade")
    protected NaknadeType naknade;
    @XmlElement(name = "IznosUkupno", required = true)
    protected BigDecimal iznosUkupno;
    @XmlElement(name = "NacinPlac", required = true)
    protected NacinPlacanjaType nacinPlac;
    @XmlElement(name = "OibOper", required = true)
    protected String oibOper;
    @XmlElement(name = "ZastKod", required = true)
    protected String zastKod;
    @XmlElement(name = "NakDost")
    protected boolean nakDost;
    @XmlElement(name = "ParagonBrRac")
    protected String paragonBrRac;
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
     * Gets the value of the uSustPdv property.
     * 
     */
    public boolean isUSustPdv() {
        return uSustPdv;
    }

    /**
     * Sets the value of the uSustPdv property.
     * 
     */
    public void setUSustPdv(boolean value) {
        this.uSustPdv = value;
    }

    /**
     * Gets the value of the datVrijeme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatVrijeme() {
        return datVrijeme;
    }

    /**
     * Sets the value of the datVrijeme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatVrijeme(String value) {
        this.datVrijeme = value;
    }

    /**
     * Gets the value of the oznSlijed property.
     * 
     * @return
     *     possible object is
     *     {@link OznakaSlijednostiType }
     *     
     */
    public OznakaSlijednostiType getOznSlijed() {
        return oznSlijed;
    }

    /**
     * Sets the value of the oznSlijed property.
     * 
     * @param value
     *     allowed object is
     *     {@link OznakaSlijednostiType }
     *     
     */
    public void setOznSlijed(OznakaSlijednostiType value) {
        this.oznSlijed = value;
    }

    /**
     * Gets the value of the brRac property.
     * 
     * @return
     *     possible object is
     *     {@link BrojRacunaType }
     *     
     */
    public BrojRacunaType getBrRac() {
        return brRac;
    }

    /**
     * Sets the value of the brRac property.
     * 
     * @param value
     *     allowed object is
     *     {@link BrojRacunaType }
     *     
     */
    public void setBrRac(BrojRacunaType value) {
        this.brRac = value;
    }

    /**
     * Gets the value of the pdv property.
     * 
     * @return
     *     possible object is
     *     {@link PdvType }
     *     
     */
    public PdvType getPdv() {
        return pdv;
    }

    /**
     * Sets the value of the pdv property.
     * 
     * @param value
     *     allowed object is
     *     {@link PdvType }
     *     
     */
    public void setPdv(PdvType value) {
        this.pdv = value;
    }

    /**
     * Gets the value of the pnp property.
     * 
     * @return
     *     possible object is
     *     {@link PorezNaPotrosnjuType }
     *     
     */
    public PorezNaPotrosnjuType getPnp() {
        return pnp;
    }

    /**
     * Sets the value of the pnp property.
     * 
     * @param value
     *     allowed object is
     *     {@link PorezNaPotrosnjuType }
     *     
     */
    public void setPnp(PorezNaPotrosnjuType value) {
        this.pnp = value;
    }

    /**
     * Gets the value of the ostaliPor property.
     * 
     * @return
     *     possible object is
     *     {@link OstaliPoreziType }
     *     
     */
    public OstaliPoreziType getOstaliPor() {
        return ostaliPor;
    }

    /**
     * Sets the value of the ostaliPor property.
     * 
     * @param value
     *     allowed object is
     *     {@link OstaliPoreziType }
     *     
     */
    public void setOstaliPor(OstaliPoreziType value) {
        this.ostaliPor = value;
    }

    /**
     * Gets the value of the iznosOslobPdv property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIznosOslobPdv() {
        return iznosOslobPdv;
    }

    /**
     * Sets the value of the iznosOslobPdv property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIznosOslobPdv(BigDecimal value) {
        this.iznosOslobPdv = value;
    }

    /**
     * Gets the value of the iznosMarza property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIznosMarza() {
        return iznosMarza;
    }

    /**
     * Sets the value of the iznosMarza property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIznosMarza(BigDecimal value) {
        this.iznosMarza = value;
    }

    /**
     * Gets the value of the iznosNePodlOpor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIznosNePodlOpor() {
        return iznosNePodlOpor;
    }

    /**
     * Sets the value of the iznosNePodlOpor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIznosNePodlOpor(BigDecimal value) {
        this.iznosNePodlOpor = value;
    }

    /**
     * Gets the value of the naknade property.
     * 
     * @return
     *     possible object is
     *     {@link NaknadeType }
     *     
     */
    public NaknadeType getNaknade() {
        return naknade;
    }

    /**
     * Sets the value of the naknade property.
     * 
     * @param value
     *     allowed object is
     *     {@link NaknadeType }
     *     
     */
    public void setNaknade(NaknadeType value) {
        this.naknade = value;
    }

    /**
     * Gets the value of the iznosUkupno property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIznosUkupno() {
        return iznosUkupno;
    }

    /**
     * Sets the value of the iznosUkupno property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIznosUkupno(BigDecimal value) {
        this.iznosUkupno = value;
    }

    /**
     * Gets the value of the nacinPlac property.
     * 
     * @return
     *     possible object is
     *     {@link NacinPlacanjaType }
     *     
     */
    public NacinPlacanjaType getNacinPlac() {
        return nacinPlac;
    }

    /**
     * Sets the value of the nacinPlac property.
     * 
     * @param value
     *     allowed object is
     *     {@link NacinPlacanjaType }
     *     
     */
    public void setNacinPlac(NacinPlacanjaType value) {
        this.nacinPlac = value;
    }

    /**
     * Gets the value of the oibOper property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOibOper() {
        return oibOper;
    }

    /**
     * Sets the value of the oibOper property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOibOper(String value) {
        this.oibOper = value;
    }

    /**
     * Gets the value of the zastKod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZastKod() {
        return zastKod;
    }

    /**
     * Sets the value of the zastKod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZastKod(String value) {
        this.zastKod = value;
    }

    /**
     * Gets the value of the nakDost property.
     * 
     */
    public boolean isNakDost() {
        return nakDost;
    }

    /**
     * Sets the value of the nakDost property.
     * 
     */
    public void setNakDost(boolean value) {
        this.nakDost = value;
    }

    /**
     * Gets the value of the paragonBrRac property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParagonBrRac() {
        return paragonBrRac;
    }

    /**
     * Sets the value of the paragonBrRac property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParagonBrRac(String value) {
        this.paragonBrRac = value;
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
