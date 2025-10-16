
package hr.apis_it.fin._2012.types.f73;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NaknadeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NaknadeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Naknada" type="{http://www.apis-it.hr/fin/2012/types/f73}NaknadaType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NaknadeType", propOrder = {
    "naknada"
})
public class NaknadeType {

    @XmlElement(name = "Naknada", required = true)
    protected List<NaknadaType> naknada;

    /**
     * Gets the value of the naknada property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the naknada property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNaknada().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NaknadaType }
     * 
     * 
     */
    public List<NaknadaType> getNaknada() {
        if (naknada == null) {
            naknada = new ArrayList<NaknadaType>();
        }
        return this.naknada;
    }

}
