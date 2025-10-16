
package hr.apis_it.fin._2012.types.f73;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PdvType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PdvType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Porez" type="{http://www.apis-it.hr/fin/2012/types/f73}PorezType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PdvType", propOrder = {
    "porez"
})
public class PdvType {

    @XmlElement(name = "Porez", required = true)
    protected List<PorezType> porez;

    /**
     * Gets the value of the porez property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the porez property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPorez().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PorezType }
     * 
     * 
     */
    public List<PorezType> getPorez() {
        if (porez == null) {
            porez = new ArrayList<PorezType>();
        }
        return this.porez;
    }

}
