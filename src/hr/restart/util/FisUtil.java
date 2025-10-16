package hr.restart.util;

import hr.apis_it.fin._2012.types.f73.AdresaType;
import hr.apis_it.fin._2012.types.f73.AdresniPodatakType;
import hr.apis_it.fin._2012.types.f73.BrojRacunaType;
import hr.apis_it.fin._2012.types.f73.GreskaType;
import hr.apis_it.fin._2012.types.f73.GreskeType;
import hr.apis_it.fin._2012.types.f73.NacinPlacanjaType;
import hr.apis_it.fin._2012.types.f73.NaknadaType;
import hr.apis_it.fin._2012.types.f73.NaknadeType;
import hr.apis_it.fin._2012.types.f73.ObjectFactory;
import hr.apis_it.fin._2012.types.f73.OznakaSlijednostiType;
import hr.apis_it.fin._2012.types.f73.OznakaZatvaranjaType;
import hr.apis_it.fin._2012.types.f73.PdvType;
import hr.apis_it.fin._2012.types.f73.PorezNaPotrosnjuType;
import hr.apis_it.fin._2012.types.f73.PorezType;
import hr.apis_it.fin._2012.types.f73.PoslovniProstorOdgovor;
import hr.apis_it.fin._2012.types.f73.PoslovniProstorType;
import hr.apis_it.fin._2012.types.f73.PoslovniProstorZahtjev;
import hr.apis_it.fin._2012.types.f73.RacunOdgovor;
import hr.apis_it.fin._2012.types.f73.RacunType;
import hr.apis_it.fin._2012.types.f73.RacunZahtjev;
import hr.apis_it.fin._2012.types.f73.ZaglavljeType;
import hr.fina.eracun.b2g.pki.echo.v0.EchoAckMsg;
import hr.fina.eracun.b2g.pki.echo.v0.EchoMsg;
//import hr.apis_it.www.fin._2012.services.fiskalizacijaservice.FiskalizacijaServiceStub;


import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;

import sun.security.action.GetBooleanAction;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class FisUtil {
  
  private static FisUtil _instance;
  private String srvURL = System.getProperty("fisutil.SRVURL","https://cis.porezna-uprava.hr:8449/FiskalizacijaService"); 
  private String testSrvURL = "https://cistest.apis-it.hr:8449/FiskalizacijaServiceTest"; 
  JAXBContext fiscontext;
  ObjectFactory fisfactory;
  private FisSigner fissigner;
  private String keyPath = null;
  private String keyStorePasword = null;
  private String keyStoreType = "PKCS12";
  private String contextClassName = "hr.apis_it.fin._2012.types.f73";
  public static String externalFisCmnd;
 
  public String getSrvURL() {
    return srvURL;
  }

  public void setSrvURL(String srvURL) {
    this.srvURL = srvURL;
  }

  public String getTestSrvURL() {
    return testSrvURL;
  }

  public void setTestSrvURL(String testSrvURL) {
    this.testSrvURL = testSrvURL;
  }
  
  public String getKeyPath() {
    if (keyPath == null) /*throw new RuntimeException*/System.err.println("Potrebno je navesti path do KeyStorea (*.pfx ili *.jks) ");
    return keyPath;
  }

  public void setKeyPath(String keypath) {
    this.keyPath = keypath;
    if (keypath!=null) {
      System.setProperty("javax.net.ssl.trustStore", keypath);
      System.setProperty("javax.net.ssl.trustStoreType", getKeyStoreType());
      System.setProperty("javax.net.ssl.keyStore", keypath);
      System.setProperty("javax.net.ssl.keyStoreType", getKeyStoreType());
    }
  }

  public String getKeyStorePasword() {
    return keyStorePasword;
  }

  public void setKeyStorePasword(String keyStorePasword) {
    this.keyStorePasword = keyStorePasword;
    if (keyStorePasword!=null) {
      System.setProperty("javax.net.ssl.keyStorePassword", getKeyStorePasword());
      System.setProperty("javax.net.ssl.trustStorePassword", getKeyStorePasword());      
    }
  }
  
  public String getExternalFisCmnd() {
    if (externalFisCmnd == null) {
      externalFisCmnd = System.getProperty("fisutil.EXTCMD");
    }
    System.out.println("externalFisCmnd = "+externalFisCmnd);
    return externalFisCmnd;
  }
  /**
   * Samo externa komanda za fiskalizaciju (Raverus.FiskalizacijaDEV.EXE.exe) u formatu "c:\fis\fis.exe. 
   * U istom direktoriju trebali bi se nalaziti i potrebni dll-ovi
   * Program ce sam dodati parametre ovisno o vrsti zahtjeva
   * @param _externalFisCmnd
   */
  public void setExternalFisCmnd(String _externalFisCmnd) {
    externalFisCmnd = _externalFisCmnd;
  }

  public static void main(String[] args) {
    FisUtil fu = null;
    try {
//      fu = new FisUtil("fiskal.jks", "1restart2", "JKS");
      if (args.length == 0) {
        //ovo se izvrsava po defaultu
//      fu.echo();  
        System.out.println("USAGE: ");
        System.out.println(" <prg> [TESTRN|TESTPP|TESTZKI] [keystore password] [req.xml(za ZKI)]");
        return;
        
      }
      if (args.length==1 && args[0].toUpperCase().startsWith("SS:")) {
        fu = FisUtil.getInstance();
        Object o = fu.getUnmarshaller().unmarshal(fu.stripSoap(new File(args[0].substring(3))));
        System.out.println(o.getClass().getName());
        System.out.println(o);
        System.out.println(((RacunOdgovor)o).getJir());
      }
      if ( args.length>2 ) {
        fu = new FisUtil(args[1], args[2], null);
      }
      if (args[0].equalsIgnoreCase("TESTRN")) {
        if (fu == null) fu = getTestFisUtil();
        long t1 = System.currentTimeMillis();
        System.out.println(
            fu.fiskaliziraj(fu.generateTestRacunZahtjev())
            );
        System.out.println("Fiskalizirano za cca "+(System.currentTimeMillis()-t1)+" milisekundi");
      } else if (args[0].equalsIgnoreCase("TESTPP")) {
        if (fu == null) fu = getTestFisUtil();
        long t1 = System.currentTimeMillis();
        System.out.println(
            fu.fiskaliziraj(fu.generateTestPPZahtjev())
            );
        System.out.println("Poslovni prostor poslan za cca "+(System.currentTimeMillis()-t1)+" milisekundi");
      } else if (args[0].equalsIgnoreCase("TESTZKI")) {
        RacunZahtjev zah = null;
        if (args.length>3) {
          try {
            zah = (RacunZahtjev)fu.getUnmarshaller().unmarshal(new File(args[3]));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        System.out.println(
            fu.generateZKI(zah==null?fu.generateTestRacunZahtjev().getRacun():zah.getRacun())
            );
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
  }
  
  
  /**
   * Metoda koja salje racun na server porezne uprave
   * @param zahtjev
   * @return JIR
   */
  public String fiskaliziraj(RacunZahtjev zahtjev) {
    String ZKI = "ZKI:"+generateZKI(zahtjev.getRacun());
    String idPoruke = zahtjev.getZaglavlje().getIdPoruke().trim();
    try {
      RacunOdgovor odg;
      if (getExternalFisCmnd() != null) {
        File response = sendExternally(zahtjev);
        //skip soap envelope - ili procitaj iz JIR.txt
        XMLStreamReader xsr = stripSoap(response);
        //unmarshal
        odg = (RacunOdgovor)getUnmarshaller().unmarshal(xsr);
      } else {
        Document doc = fissigner.signJAXBDocument(zahtjev);
//        System.out.println("****** P O T P I S A N I *****");
//        fissigner.printDOM(new DOMSource(doc),new StreamResult(new FileOutputStream("signed.xml")));
//        fissigner.printDOM(doc, System.out);
//        System.out.println("****** E N D ");
        SOAPMessage resp = fissigner.sendSOAP(doc);
        Document respdoc = resp.getSOAPBody().extractContentAsDocument();
//        fissigner.printDOM(respdoc, System.out);
        odg = (RacunOdgovor)getUnmarshaller().unmarshal(respdoc);
      }
      if (checkResponse(idPoruke, odg)) {
        System.out.println("no errors - returning Jir");
        return odg.getJir();
      }
      //return "6b7749c6-56c1-4cf5-b7f7-9f29cebc9f7f";//iz primjera
      //        e4d909c2-90d0-fb1c-a068-ffaddf22cbd0
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ZKI;
  }
  
  public boolean echo(EchoMsg msg) {
    try {
        Document doc = fissigner.signJAXBDocument(msg);
        SOAPMessage resp = fissigner.sendSOAP(doc);
        Document respdoc = resp.getSOAPBody().extractContentAsDocument();
        EchoAckMsg odg = (EchoAckMsg) getUnmarshaller().unmarshal(respdoc);
        
        System.out.println("odgovor: " + odg);
        if (odg != null) {
          System.out.println(odg.getEchoData().getEcho());
          System.out.println(odg.getMessageAck().getAckStatusText());
          System.out.println(odg.getMessageAck().getAckStatusCode());
        }
        return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  
  public boolean fiskaliziraj(PoslovniProstorZahtjev PPZahtjev) {
    String idPoruke = PPZahtjev.getZaglavlje().getIdPoruke();
    try {
      if (getExternalFisCmnd() != null) {
        File response = sendExternally(PPZahtjev);
        //skip soap envelope - ili procitaj iz JIR.txt
        XMLStreamReader xsr = stripSoap(response);
        //unmarshal
        PoslovniProstorOdgovor odg = (PoslovniProstorOdgovor)getUnmarshaller().unmarshal(xsr);
        return checkResponse(idPoruke, odg);
      } else {
        Document doc = fissigner.signJAXBDocument(PPZahtjev);
        SOAPMessage resp = fissigner.sendSOAP(doc);
        Document respdoc = resp.getSOAPBody().extractContentAsDocument();
        PoslovniProstorOdgovor odg = (PoslovniProstorOdgovor)getUnmarshaller().unmarshal(respdoc);
        return checkResponse(idPoruke, odg);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  public boolean checkResponse(String idPoruke, Object odg) {
    String idOdg = "";
    GreskeType greska = null;
    List<GreskaType> greske = null;
    if (odg instanceof RacunOdgovor) {
      idOdg = ((RacunOdgovor)odg).getZaglavlje().getIdPoruke().trim();
      greska = ((RacunOdgovor)odg).getGreske();
    } else if (odg instanceof PoslovniProstorOdgovor) {
      idOdg = ((PoslovniProstorOdgovor)odg).getZaglavlje().getIdPoruke().trim();
      greska = ((PoslovniProstorOdgovor)odg).getGreske();      
    } else {
      System.out.println("********* FISKALIZACIJA: GREŠKA: Nepoznat tip odgovora");
      return false;
    }
    if (idOdg.equals(idPoruke)) {
      System.out.println("idPoruke matches - checking errors");
      if (greska == null) {
        //no errors
        return true;
      } else {
        greske = greska.getGreska();
      }
      if (!greske.isEmpty()) {
        System.out.println("********* FISKALIZACIJA: GREŠKE PRI SLANJU PORUKE U PU *********");
        System.out.println("* TIP PORUKE: "+odg.getClass().getName());
        System.out.println("* ID PORUKE: "+idPoruke);
        System.out.println("* Greške:: \n*");
        for (Iterator iterator = greske.iterator(); iterator.hasNext();) {
          GreskaType greskaType = (GreskaType) iterator.next();
          System.out.println("*    "+greskaType.getSifraGreske()+"   "+greskaType.getPorukaGreske());
        }
        System.out.println("*****************************************************************");
        return false;
      }
    } else {
      System.out.println("********* FISKALIZACIJA: GREŠKA: ID poruke i ID odgovora ne odgovaraju!!! ");
      return false;
    }
    return true;
  }

  public XMLStreamReader stripSoap(File response) throws XMLStreamException,
      FactoryConfigurationError, FileNotFoundException {
    FileReader fr = new FileReader(response);
    try {//skip garbage
      int pos = 0;
      while (true) {
        int r = fr.read();
        if (r < 0) break;
        if (r == '<') break;
        pos++;
      }
      System.out.println("< found at "+pos);
      if (pos>0) {
        System.out.println("skipping stream");
        fr = new FileReader(response);
        fr.skip(pos);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(fr);
    System.out.println(xsr.getClass());
    xsr.nextTag();
    xsr.nextTag();
    xsr.nextTag();
    return xsr;
  }

  private File sendExternally(Object zahtjev) throws JAXBException, IOException {
    //koristeci vanjski exe posalje zahtjev, procita odgovor i vrati jir
    File req = new File("req.xml");
    File resp = new File("resp.xml");
    String command = null;
    if (zahtjev instanceof RacunZahtjev) {
      command = "RacunZahtjev";
    } else if (zahtjev instanceof PoslovniProstorZahtjev) {
      command = "PoslovniProstorZahtjev";
    } else {
      throw new RuntimeException("Greška:: Nepoznat tip zahtjeva "+zahtjev);
    }
    getMarshaller().marshal(zahtjev, req);
    String cmd = getExternalFisCmnd()+" "+command+" "+getSrvURL()
        +" \""+req.getAbsolutePath()+"\" "
        +" \""+resp.getAbsolutePath()+"\" "
        +"true true "
        +getKeyPath()+" "+getKeyStorePasword();
    System.out.println("*** executing "+cmd);
    Process proc = Runtime.getRuntime().exec(cmd);
    int ch;
    while ((ch = proc.getErrorStream().read()) > -1) System.out.write(ch);
    while ((ch = proc.getInputStream().read()) > -1) System.out.write(ch);
    try {
      System.out.println(getExternalFisCmnd()+" exit code: "+proc.waitFor());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return resp;
  }
  private Marshaller fismarshaller;
  public Marshaller getMarshaller() {
    if (fismarshaller == null) {
      try {
        fismarshaller = fiscontext.createMarshaller();
        fismarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        fismarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
          
          @Override
          public String getPreferredPrefix(String namespaceUri, String suggestion,
              boolean requirePrefix) {
            if (namespaceUri.equals("http://www.apis-it.hr/fin/2012/types/f73")) return "tns";
            if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance")) return "xsi";
            return suggestion;
          }
        });
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        fismarshaller = null;
      }
    }
    return fismarshaller;
  }
  
  private Unmarshaller fisunmarshaller;
  public Unmarshaller getUnmarshaller() {
    if (fisunmarshaller == null) {
      try {
        fisunmarshaller = fiscontext.createUnmarshaller();
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
    return fisunmarshaller;
  }
  /**
   * Primjer kreiranje zahtjeva sa racunom:
   * <pre>
      fu = new FisUtil("fiskal.pfx", "Pa5sw0rd", null);
      Timestamp datvri = new Timestamp(System.currentTimeMillis());
      RacunZahtjev zahtj = fu.createRacunZahtjev(
          fu.createZaglavlje(datvri, null), 
          fu.createRacun(
              "53204444499", //oib firme (Rest Art) NE PREPISUJ!!
              true, //da li je obveznik pdv-a 
              datvri, // datum i vrijeme kreiranja racuna
              "N", // oznaka slijednosti
              666, // broj racuna 
              "P1", // oznaka poslovne jedinice
              1, // oznaka naplatnog mjesta
              new BigDecimal(25), //stopa pdv-a 
              new BigDecimal(1000), //osnovica za pdv
              new BigDecimal(250), //iznos pdv-a
              new BigDecimal(5), //stopa pnp-a
              new BigDecimal(10), //osnovica za pnp
              new BigDecimal(0.5), //iznos pnp 
              null, //naziv naknade - defaults to 'Povradna naknada' 
              new BigDecimal(0.5), //iznos naknade
              new BigDecimal(1260.5), //ukupan iznos racuna
              "G",//nacin placanja
              "23261401589",//oib prodavatelja (Ja) NE PREPISUJ!!
              false //da li je naknadna dostava
           ));

      fu.getMarshaller().marshal(zahtj, System.out);
   * </pre>
   * 
   * @param _keyStorePath gdje je keystore
   * @param _keyStorePasword koji je password
   * @param _keyStoreType koji je tip keystorea - ako je null ima nekakvu autodetekciju.
   * @throws JAXBException
   */
  public FisUtil(String _keyStorePath, String _keyStorePasword, String _keyStoreType) throws Exception {
    this(_keyStorePath, _keyStorePasword, _keyStoreType, "hr.apis_it.fin._2012.types.f73");
  }
  
  public FisUtil(String _keyStorePath, String _keyStorePasword, String _keyStoreType, String _context) throws Exception {
    setKeyPath(_keyStorePath);
    setKeyStorePasword(_keyStorePasword);
    setKeyStoreType(_keyStoreType);
    contextClassName = _context;
    initialize();
    _instance = this;
  }

  public static FisUtil getInstance() {
    if (_instance == null) {
      try {
        _instance = new FisUtil(null, null, null);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
      }
    }
    return _instance;
  }
  
  private void initialize() throws Exception {
    
    fiscontext = JAXBContext.newInstance(contextClassName);
    fisfactory = new ObjectFactory();
    fissigner = new FisSigner(this);
    if (getKeyPath() != null) {//redundanca, ali ako je netko instancirao sa keystoreType null, onda ... 
      System.setProperty("javax.net.ssl.trustStore", getKeyPath());
      System.setProperty("javax.net.ssl.trustStoreType", getKeyStoreType());
      System.setProperty("javax.net.ssl.keyStore", getKeyPath());
      System.setProperty("javax.net.ssl.keyStoreType", getKeyStoreType());
      System.setProperty("javax.net.ssl.keyStorePassword", getKeyStorePasword());
      System.setProperty("javax.net.ssl.trustStorePassword", getKeyStorePasword());
    }
  }
  
  public ObjectFactory getFisFactory() {
    return fisfactory;
  }
  
  public RacunZahtjev createRacunZahtjev(ZaglavljeType zaglavljeType, RacunType racunType) {
    RacunZahtjev racunzahtjev = fisfactory.createRacunZahtjev();
    racunzahtjev.setId("signXmlId");
    if (zaglavljeType == null) zaglavljeType = createZaglavlje(null, null);
    racunzahtjev.setZaglavlje(zaglavljeType);
    if (racunType == null) racunType = createRacun(null,true,null,null,567,"PP1",1, null, null, null, null, null, null, null, null, null, "G", null, false);
    racunzahtjev.setRacun(racunType);
 //....    
    return racunzahtjev;
  }
  
  public PoslovniProstorZahtjev createPoslovniProstorZahtjev(ZaglavljeType zaglavljeType, PoslovniProstorType poslovniProstorType) {
    PoslovniProstorZahtjev prostorzahtjev = fisfactory.createPoslovniProstorZahtjev();
    prostorzahtjev.setId("signXmlId");
    if (zaglavljeType == null) zaglavljeType = createZaglavlje(null, null);
    prostorzahtjev.setZaglavlje(zaglavljeType);
    if (poslovniProstorType == null) poslovniProstorType = createPoslovniProstor("", "", "", "", "", "", "", "", "", "", new Timestamp(System.currentTimeMillis()), false);
    prostorzahtjev.setPoslovniProstor(poslovniProstorType);
    
    return prostorzahtjev;
  }
  
  public PoslovniProstorType createPoslovniProstor(String oib, String oznPoslProstora, String ulica, String kucnibroj, String kucnibrojdodatak, String brojposte, String naselje, String opcina, String ostaliTipPP, String radnoVrijeme, Timestamp datumPocetkaPrimjene, boolean zatvaranje) {
    PoslovniProstorType pp = fisfactory.createPoslovniProstorType();
    pp.setOib(oib);
    pp.setOznPoslProstora(oznPoslProstora);
    AdresniPodatakType adresniPodatak = fisfactory.createAdresniPodatakType();
    if (ulica != null && !"".equals(ulica.trim())) {
      AdresaType adresa = fisfactory.createAdresaType();
      adresa.setUlica(ulica);
      if (kucnibroj != null) {
        adresa.setKucniBroj(kucnibroj);
        if (kucnibrojdodatak != null) adresa.setKucniBrojDodatak(kucnibrojdodatak);
      }
      if (brojposte != null) adresa.setBrojPoste(brojposte);
      if (naselje != null) adresa.setNaselje(naselje);
      if (opcina != null) adresa.setOpcina(opcina);
      adresniPodatak.setAdresa(adresa);
    }
    if (ostaliTipPP!=null) adresniPodatak.setOstaliTipoviPP(ostaliTipPP);
    pp.setAdresniPodatak(adresniPodatak);
    pp.setRadnoVrijeme(radnoVrijeme);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    pp.setDatumPocetkaPrimjene(sdf.format(datumPocetkaPrimjene));
    if (zatvaranje) pp.setOznakaZatvaranja(OznakaZatvaranjaType.Z);
    pp.setSpecNamj("53204444499"); //OIB od Rest Arta :(
    return pp;
  }

  public static String formatDatumVrijeme(Timestamp dv) {
    if (dv == null) dv = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy'T'HH:mm:ss");
    return df.format(dv);
  }
  /**
   * Kreira zaglavlje zahtjeva sa zadanim vremenom kreiranja i uuid-om
   * @param datumVrijemeKreiranja ako je null onda System.currentTimeMillis()
   * @param uuidPoruke ako je null onda UUID.randomUUID()
   * @return
   */
  public ZaglavljeType createZaglavlje(Timestamp datumVrijemeKreiranja, String uuidPoruke) {
    ZaglavljeType zaglavlje = fisfactory.createZaglavljeType();
    zaglavlje.setDatumVrijeme(formatDatumVrijeme(datumVrijemeKreiranja));
    if (uuidPoruke == null) uuidPoruke = UUID.randomUUID().toString();
    zaglavlje.setIdPoruke(uuidPoruke);
    return zaglavlje;
  }
  /**
   * Uobičajena metoda za kreiranje računa sa jednim PDV-om i jednim PNP, te eventualno jednom (povratnom) naknadom. 
   * Ako treba proširiti racun npr. sa jos jednom stopom PDV-a od 10% to se radi ovako:
   * <pre>
   * RacunType rn = createRacun(....);
   * PorezType p10p = getFisFactory().createPorezType();
   * porez.setStopa(new BigDecimal("10.00"));
   * porez.setOsnovica(...);
   * porez.setIznos(...);
   * rn.getPdv().getPorez().add(p10p); 
   * </pre>
   * RacunType  
   * @param oib OIB obveznika fiskalizacije.
   * @param uSustavuPDV Oznaka je li obveznik u sustavu PDV-a ili nije. True ako je obveznik u sustavu PDV-a, usuprotnom false.
   * @param datVrijemeIzdavanja Datum i vrijeme izdavanja koji se ispisuju na računu. Ako je null daje System.currentTimeMillis()
   * @param oznakaSlijednosti Oznaka slijednosti brojeva računa. Oznaka koja govori gdje se određuje dodjela broja računa tj. dodjeljuje li se broj računa centralno na razini poslovnog prostora ili pojedinačno na svakom naplatnom uređaju. P - na nivou poslovnog prostora; N - na nivou naplatnog uređaja - default je N 
   * @param brojrac Može sadržavati samo znamenke 0-9. Nisu dozvoljene vodeće nule.
   * @param oznPoslProstora Može sadržavati samo znamenke i slova 0-9, a-z, A-Z. Mora biti jedinstvena na razini OIB-a obveznika.
   * @param oznNapUredjaja Može sadržavati samo znamenke 0-9. Nisu dozvoljene vodeće nule. Mora biti jedinstvena na razini jednog poslovnog prostora obveznika.
   * @param PDV_PP1 Stopa prvog PDV-a
   * @param PDV_OSN1 Iznos prve osnovice PDV-a. Ako je null ili 0 ne kreira se slog poreza (PDV_PP1 i PDV_IZN1 se ignoriraju) 
   * @param PDV_IZN1 Iznos prvog PDV-a
   * @param PNP_PP1 Stopa PNP-a
   * @param PNP_OSN1 Iznos osnovice PNP-a. Ako je null ili 0 ne kreira se slog poreza (PNP_PP1 i PNP_IZN1 se ignoriraju) 
   * @param PNP_IZN1 Iznos PNP-a
   * @param nazivNaknade Naziv naknade. Default 'Povratna naknada'
   * @param NAK_IZN Iznos naknade. Ako je null ili 0 ne kreira se slog naknade (nazivNaknade se ignorira)
   * @param UKUPNO Ukupan iznos iskazan na računu.
   * @param nacinPlacanja Način plaćanja može biti: <li>G – gotovina</li><li>K – kartice</li><li>C – ček</li><li>T – transakcijski račun</li><li>O – ostalo</li>
   * U slučaju više načina plaćanja po jednom računu, isto je potrebno prijaviti pod 'Ostalo'. 
   * Za sve načine plaćanja koji nisu prije navedeni koristiti će se oznaka ‘Ostalo’.
   * @param oibOperatera OIB operatera na naplatnom uređaju koji izdaje račun. U slučaju samouslužnih naplatnih uređaja i automata potrebno je dostaviti OIB izdavatelja (OIB s računa).
   * @param naknadnaDostava Pod naknadnom dostavom računa Poreznoj upravi smatra se situacija kad je isti prethodno izdan kupcu bez JIR-a (npr. prekid Internet veze ili potpuni prestanak rada naplatnog uređaja). True ako je riječ o naknadnoj dostavi računa, u suprotnom false.
   * @return kreirani racun 
   */
  public RacunType createRacun(String oib, boolean uSustavuPDV, Timestamp datVrijemeIzdavanja, String oznakaSlijednosti,
      int brojrac, String oznPoslProstora, int oznNapUredjaja,
      BigDecimal PDV_PP1, BigDecimal PDV_OSN1, BigDecimal PDV_IZN1,
      BigDecimal PNP_PP1, BigDecimal PNP_OSN1, BigDecimal PNP_IZN1,
      String nazivNaknade, BigDecimal NAK_IZN, 
      BigDecimal UKUPNO, String nacinPlacanja, String oibOperatera, boolean naknadnaDostava
      ) {
    RacunType racun = fisfactory.createRacunType();
    
    racun.setOib(oib);
    
    racun.setUSustPdv(uSustavuPDV);
    
    racun.setDatVrijeme(formatDatumVrijeme(datVrijemeIzdavanja));
    
    racun.setOznSlijed("P".equalsIgnoreCase(oznakaSlijednosti)?OznakaSlijednostiType.P:OznakaSlijednostiType.N);
    
    BrojRacunaType brojracuna = fisfactory.createBrojRacunaType();
    brojracuna.setBrOznRac(Integer.toString(brojrac));
    brojracuna.setOznPosPr(oznPoslProstora);
    brojracuna.setOznNapUr(Integer.toString(oznNapUredjaja));
    racun.setBrRac(brojracuna);
    
    if (PDV_OSN1!=null && PDV_OSN1.signum() != 0) {
      PdvType pdv = fisfactory.createPdvType();
      PorezType porez = fisfactory.createPorezType();
      porez.setStopa(sc2(PDV_PP1));
      porez.setOsnovica(sc2(PDV_OSN1));
      porez.setIznos(sc2(PDV_IZN1));
      pdv.getPorez().add(porez);
      racun.setPdv(pdv);
    }
    
    if (PNP_OSN1!=null && PNP_OSN1.signum() != 0) {
      PorezNaPotrosnjuType pnp = fisfactory.createPorezNaPotrosnjuType();
      PorezType porez = fisfactory.createPorezType();
      porez.setStopa(sc2(PNP_PP1));
      porez.setOsnovica(sc2(PNP_OSN1));
      porez.setIznos(sc2(PNP_IZN1));
      pnp.getPorez().add(porez);
      racun.setPnp(pnp);
    }
    
    if (NAK_IZN!=null && NAK_IZN.signum() != 0) {
      NaknadeType naks = fisfactory.createNaknadeType();
      NaknadaType nak = fisfactory.createNaknadaType();
      nak.setNazivN(nazivNaknade==null?"Povratna naknada":nazivNaknade);
      nak.setIznosN(sc2(NAK_IZN));
      naks.getNaknada().add(nak);
      racun.setNaknade(naks);
    }
    
    racun.setIznosUkupno(sc2(UKUPNO));
    
    nacinPlacanja=nacinPlacanja==null?"O":nacinPlacanja;
    racun.setNacinPlac(NacinPlacanjaType.fromValue(nacinPlacanja));
    
    racun.setOibOper(oibOperatera);
    
    racun.setZastKod(generateZKI(racun));
    
    racun.setNakDost(naknadnaDostava);
    
    return racun;
  }
  private BigDecimal sc2(BigDecimal b) {
    return b.setScale(2, BigDecimal.ROUND_HALF_UP);
  }
  public String generateZKI(RacunType racun) {
    String oib = racun.getOib();
    String dv = racun.getDatVrijeme();
    String bor = racun.getBrRac().getBrOznRac();
    String opp = racun.getBrRac().getOznPosPr();
    String onu = racun.getBrRac().getOznNapUr();
    String uir = racun.getIznosUkupno().toPlainString();
    
    return generateZKI(oib, dv, bor, opp, onu, uir);
  }

  public String getKeyStoreType() {
    if (keyStoreType == null) {
      if (getKeyPath().endsWith("pfx")) {
        System.out.println("Autodetecting keyStoreType PKCS12");
        keyStoreType = "PKCS12";
      } else if (getKeyPath().endsWith("jks")) {
        System.out.println("Autodetecting keyStoreType JKS");
        keyStoreType = "JKS";
      } else {
        System.out.println("Unknown keyStoreType. Assuming PKCS12");
        keyStoreType = "PKCS12";
      }
    }
    return keyStoreType.toLowerCase();
  }

  public void setKeyStoreType(String keyStoreType) {
    this.keyStoreType = keyStoreType;
    if (keyStoreType!=null) {
      System.setProperty("javax.net.ssl.trustStoreType", keyStoreType);
      System.setProperty("javax.net.ssl.keyStoreType", keyStoreType);
    }
  }
  
  private KeyStore keyStore;
  public KeyStore getKeyStore() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
    if (keyStore == null) {
      FileInputStream fis = new FileInputStream(getKeyPath());
      keyStore = KeyStore.getInstance(getKeyStoreType());
      keyStore.load(fis, getKeyStorePasword().toCharArray());
    }
    return keyStore;
  }
  
  private String generateZKI(String oib, String dv, String bor, String opp,
      String onu, String uir) {
    
    byte[] pkb = null;
    
    String mr = oib + dv + bor + opp + onu + uir;
    
    try {
      KeyStore kys = getKeyStore();
      Enumeration als = kys.aliases();
      String alias = (String) als.nextElement();
      System.err.println("alias " + alias);
      while (alias.equalsIgnoreCase(getMyKey()) || alias.equalsIgnoreCase("fiskalcis")) {
        alias = (String) als.nextElement();
        System.err.println("alias " + alias);
      }
      System.err.println("ALIAS::: "+alias);
      Key key = kys.getKey(alias, getKeyStorePasword().toCharArray());
      Signature signer = Signature.getInstance("SHA256withRSA");
      signer.initSign((PrivateKey)key);
      signer.update(mr.getBytes());
      pkb = signer.sign();
//      pk = Base64.encode(key.getEncoded());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return DigestUtils.md5Hex(pkb);
  }

  public void echo() {
//    try {
//      FiskalizacijaServiceStub stub = new FiskalizacijaServiceStub(testSrvURL);
//      JAXBElement<String> echomsg = fisfactory.createEchoRequest("probica");
//      CharArrayWriter wrtr = new CharArrayWriter();
//      getMarshaller().marshal(echomsg, wrtr);
//      System.out.println(wrtr.toString());
////      String resp = stub.echo(wrtr.toString());
//      String resp = stub.echo("probica");
//      System.out.println("Echo response :: "+resp);
//    } catch (Exception e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
  }

  //test
  public RacunZahtjev generateTestRacunZahtjev() {
    Timestamp datvri = new Timestamp(System.currentTimeMillis());
    RacunZahtjev zahtj = createRacunZahtjev(
        createZaglavlje(datvri, null), 
        createRacun(
            "53204444499", //oib firme (Rest Art) NE PREPISUJ!!
            true, //da li je obveznik pdv-a 
            datvri, // datum i vrijeme kreiranja racuna
            "N", // oznaka slijednosti
            666, // broj racuna 
            "P1", // oznaka poslovne jedinice
            1, // oznaka naplatnog mjesta
            new BigDecimal(25), //stopa pdv-a 
            new BigDecimal(1), //osnovica za pdv
            new BigDecimal(0.25), //iznos pdv-a
            new BigDecimal(3), //stopa pnp-a
            new BigDecimal(1), //osnovica za pnp
            new BigDecimal(0.03), //iznos pnp 
            null, //naziv naknade - defaults to 'Povradna naknada' 
            null,//new BigDecimal(0.5), //iznos naknade
            new BigDecimal(1.28), //ukupan iznos racuna
            "G",//nacin placanja
            "23261401589",//oib prodavatelja (Ja) NE PREPISUJ!!
            false //da li je naknadna dostava
         ));
    return zahtj;
  }
  private PoslovniProstorZahtjev generateTestPPZahtjev() {
    Timestamp datvri = new Timestamp(System.currentTimeMillis());
    PoslovniProstorZahtjev ppz = createPoslovniProstorZahtjev(
        createZaglavlje(datvri, null), 
        createPoslovniProstor("53204444499", "PP1", "Medjimurska", "21", null, "10000", "Zagreb", null, null, "Pon-Pet 09:00-17:00", new Timestamp(System.currentTimeMillis()),false)
        );
    return ppz;
  }
  private static FisUtil getTestFisUtil() throws Exception {
    return new FisUtil("fiskal.jks", "1restart2", null);
  }

  public static String getMyKey() {
    String mykey = IntParam.getTag("fisutil.keyalias");
    if (mykey == null || "".equals(mykey)) mykey = "mykey";
    return mykey;
  }

}
