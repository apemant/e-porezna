
package hr.apis_it.fin._2012.types.f73;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hr.apis_it.fin._2012.types.f73 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EchoRequest_QNAME = new QName("http://www.apis-it.hr/fin/2012/types/f73", "EchoRequest");
    private final static QName _EchoResponse_QNAME = new QName("http://www.apis-it.hr/fin/2012/types/f73", "EchoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hr.apis_it.fin._2012.types.f73
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RacunZahtjev }
     * 
     */
    public RacunZahtjev createRacunZahtjev() {
        return new RacunZahtjev();
    }

    /**
     * Create an instance of {@link PorezOstaloType }
     * 
     */
    public PorezOstaloType createPorezOstaloType() {
        return new PorezOstaloType();
    }

    /**
     * Create an instance of {@link BrojRacunaType }
     * 
     */
    public BrojRacunaType createBrojRacunaType() {
        return new BrojRacunaType();
    }

    /**
     * Create an instance of {@link PoslovniProstorOdgovor }
     * 
     */
    public PoslovniProstorOdgovor createPoslovniProstorOdgovor() {
        return new PoslovniProstorOdgovor();
    }

    /**
     * Create an instance of {@link PdvType }
     * 
     */
    public PdvType createPdvType() {
        return new PdvType();
    }

    /**
     * Create an instance of {@link PorezType }
     * 
     */
    public PorezType createPorezType() {
        return new PorezType();
    }

    /**
     * Create an instance of {@link ZaglavljeOdgovorType }
     * 
     */
    public ZaglavljeOdgovorType createZaglavljeOdgovorType() {
        return new ZaglavljeOdgovorType();
    }

    /**
     * Create an instance of {@link GreskeType }
     * 
     */
    public GreskeType createGreskeType() {
        return new GreskeType();
    }

    /**
     * Create an instance of {@link GreskaType }
     * 
     */
    public GreskaType createGreskaType() {
        return new GreskaType();
    }

    /**
     * Create an instance of {@link ZaglavljeType }
     * 
     */
    public ZaglavljeType createZaglavljeType() {
        return new ZaglavljeType();
    }

    /**
     * Create an instance of {@link RacunOdgovor }
     * 
     */
    public RacunOdgovor createRacunOdgovor() {
        return new RacunOdgovor();
    }

    /**
     * Create an instance of {@link AdresaType }
     * 
     */
    public AdresaType createAdresaType() {
        return new AdresaType();
    }

    /**
     * Create an instance of {@link PoslovniProstorZahtjev }
     * 
     */
    public PoslovniProstorZahtjev createPoslovniProstorZahtjev() {
        return new PoslovniProstorZahtjev();
    }

    /**
     * Create an instance of {@link PorezNaPotrosnjuType }
     * 
     */
    public PorezNaPotrosnjuType createPorezNaPotrosnjuType() {
        return new PorezNaPotrosnjuType();
    }

    /**
     * Create an instance of {@link NaknadaType }
     * 
     */
    public NaknadaType createNaknadaType() {
        return new NaknadaType();
    }

    /**
     * Create an instance of {@link NaknadeType }
     * 
     */
    public NaknadeType createNaknadeType() {
        return new NaknadeType();
    }

    /**
     * Create an instance of {@link RacunType }
     * 
     */
    public RacunType createRacunType() {
        return new RacunType();
    }

    /**
     * Create an instance of {@link PoslovniProstorType }
     * 
     */
    public PoslovniProstorType createPoslovniProstorType() {
        return new PoslovniProstorType();
    }

    /**
     * Create an instance of {@link OstaliPoreziType }
     * 
     */
    public OstaliPoreziType createOstaliPoreziType() {
        return new OstaliPoreziType();
    }

    /**
     * Create an instance of {@link AdresniPodatakType }
     * 
     */
    public AdresniPodatakType createAdresniPodatakType() {
        return new AdresniPodatakType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.apis-it.hr/fin/2012/types/f73", name = "EchoRequest")
    public JAXBElement<String> createEchoRequest(String value) {
        return new JAXBElement<String>(_EchoRequest_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.apis-it.hr/fin/2012/types/f73", name = "EchoResponse")
    public JAXBElement<String> createEchoResponse(String value) {
        return new JAXBElement<String>(_EchoResponse_QNAME, String.class, null, value);
    }

}
