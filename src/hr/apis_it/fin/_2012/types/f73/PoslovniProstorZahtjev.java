
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig.SignatureType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Zaglavlje" type="{http://www.apis-it.hr/fin/2012/types/f73}ZaglavljeType"/>
 *         &lt;element name="PoslovniProstor" type="{http://www.apis-it.hr/fin/2012/types/f73}PoslovniProstorType"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "zaglavlje",
    "poslovniProstor",
    "signature"
})
@XmlRootElement(name = "PoslovniProstorZahtjev")
public class PoslovniProstorZahtjev {

    @XmlElement(name = "Zaglavlje", required = true)
    protected ZaglavljeType zaglavlje;
    @XmlElement(name = "PoslovniProstor", required = true)
    protected PoslovniProstorType poslovniProstor;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    protected SignatureType signature;
    @XmlAttribute(name = "Id")
    protected String id;

    /**
     * Gets the value of the zaglavlje property.
     * 
     * @return
     *     possible object is
     *     {@link ZaglavljeType }
     *     
     */
    public ZaglavljeType getZaglavlje() {
        return zaglavlje;
    }

    /**
     * Sets the value of the zaglavlje property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZaglavljeType }
     *     
     */
    public void setZaglavlje(ZaglavljeType value) {
        this.zaglavlje = value;
    }

    /**
     * Gets the value of the poslovniProstor property.
     * 
     * @return
     *     possible object is
     *     {@link PoslovniProstorType }
     *     
     */
    public PoslovniProstorType getPoslovniProstor() {
        return poslovniProstor;
    }

    /**
     * Sets the value of the poslovniProstor property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoslovniProstorType }
     *     
     */
    public void setPoslovniProstor(PoslovniProstorType value) {
        this.poslovniProstor = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
