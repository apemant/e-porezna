package hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0;

import hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SIDmetapodaci;
import hr.porezna_uprava.e_porezna.sheme.temeljnitipovi.v2_1.TFormat;
import hr.porezna_uprava.e_porezna.sheme.temeljnitipovi.v2_1.TJezik;
import hr.porezna_uprava.e_porezna.sheme.temeljnitipovi.v2_1.TTip;
import hr.restart.util.MetaGenerator;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Generator {
  public static final String AUTOR = "id.autor";
  public static final String DATUM = "id.datum";
  public static final String IDENTIFIKATOR = "id.identifikator";
  public static final String PODRUCNI_URED = "id.podrucniured";
  public static final String ISPOSTAVA = "id.ispostava";
  public static final String PODNOSITELJ_NAZIV = "id.podnositelj.naziv";
  public static final String PODNOSITELJ_OIB = "id.podnositelj.oib";
  public static final String PODNOSITELJ_SIFDJEL = "id.podnositelj.sifdjel";
  public static final String PODNOSITELJ_EMAIL = "id.podnositelj.email";
  public static final String PODNOSITELJ_ADRESA_BROJ = "id.podnositelj.adresa.broj";
  public static final String PODNOSITELJ_ADRESA_MJESTO = "id.podnositelj.adresa.mjesto";
  public static final String PODNOSITELJ_ADRESA_ULICA = "id.podnositelj.adresa.ulica";
  public static final String DU_P110 = "id.du.p110";
  public static final String DU_P120 = "id.du.p120";
  public static final String DU_P210 = "id.du.p210";
  public static final String DU_P220 = "id.du.p220";
  public static final String DU_P310 = "id.du.p310";
  public static final String DU_P320 = "id.du.p320";
  public static final String DU_P330 = "id.du.p330";
  public static final String DU_P410 = "id.du.p410";
  public static final String DU_P420 = "id.du.p420";
  /**
   * Biginteger!!!
   */
  public static final String DU_P500 = "id.du.p500";

  public static final String OP_P100 = "id.op.p100";
  public static final String OP_P200 = "id.op.p200";
  public static final String OP_P210 = "id.op.p210";
  public static final String OP_P220 = "id.op.p220";
  public static final String OP_P230 = "id.op.p230";
  public static final String OP_P300 = "id.op.p300";
  public static final String OP_P400 = "id.op.p400";
  public static final String OP_P500 = "id.op.p500";
  public static final String OP_P600 = "id.op.p600";
  public static final String OP_P610 = "id.op.p610";
  public static final String OP_P620 = "id.op.p620";
  public static final String OP_P700 = "id.op.p700";
  /**
   * Biginteger!!!
   */
  public static final String OP_P800 = "id.op.p800";
  
  /**
   * TreeMap<String, BigDecimal[]> - copcine, [porez,prirez,ukupno]
   */
  public static final String OBRACUNATI_POREZI = "id.obracunati.porezi";
  
  public static final String UKUPNO_POREZA = "id.ukupno.poreza";
  public static final String UKUPNO_PRIREZA = "id.ukupno.prireza";
  public static final String UKUPNO_UKUPNO = "id.ukupno.ukupno";
  public static final String PERIOD_OD = "id.period.od";
  public static final String PERIOD_DO = "id.period.do";
  public static final String SASTAVIO_IME = "sastavio.ime";
  public static final String SASTAVIO_PREZIME = "sastavio.prezime";
  public static final String SASTAVIO_TEL = "sastavio.tel";
  public static final String SASTAVIO_FAX = "sastavio.fax";
  public static final String SASTAVIO_EMAIL = "sastavio.email";
  
  
  JAXBContext context;
  ObjectFactory factory;
  SObrazacID sid;
  HashMap<String, Object> data;
  public Generator(HashMap<String, Object> _data, FileOutputStream out) throws Exception {
    data = _data;
    initialize();
    generate();
    marshal(out);
  }
  

  private void initialize() throws JAXBException {
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0");
    factory = new ObjectFactory();
  }
  
  public void generate() throws Exception {
    sid = factory.createSObrazacID();
//    sid.setMetapodaci(value)
//    sid.setTijelo(value)
//    sid.setVerzijaSheme(value)
//    sid.setZaglavlje(value)
     sid.setVerzijaSheme("3.0");
     sid.setMetapodaci(createSIDmeta());
     sid.setZaglavlje(createZaglavlje());
     sid.setTijelo(createTijelo());
  }

  @SuppressWarnings("unchecked")
  private STijelo createTijelo() {
    STijelo stj = factory.createSTijelo();
    stj.setDoprinosiUkupno(factory.createSDoprinosiUkupno());
    stj.getDoprinosiUkupno().setPodatak110((BigDecimal)data.get(DU_P110));
    stj.getDoprinosiUkupno().setPodatak120((BigDecimal)data.get(DU_P120));
    stj.getDoprinosiUkupno().setPodatak210((BigDecimal)data.get(DU_P210));
    stj.getDoprinosiUkupno().setPodatak220((BigDecimal)data.get(DU_P220));
    stj.getDoprinosiUkupno().setPodatak310((BigDecimal)data.get(DU_P310));
    stj.getDoprinosiUkupno().setPodatak320((BigDecimal)data.get(DU_P320));
    stj.getDoprinosiUkupno().setPodatak330((BigDecimal)data.get(DU_P330));
    stj.getDoprinosiUkupno().setPodatak410((BigDecimal)data.get(DU_P410));
    stj.getDoprinosiUkupno().setPodatak420((BigDecimal)data.get(DU_P420));
    stj.getDoprinosiUkupno().setPodatak500((BigInteger)data.get(DU_P500));
    
    stj.setIsplaceniPrimiciIObracunPoreza(factory.createSIsplaceniPrimiciIObracunPoreza());
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak100((BigDecimal)data.get(OP_P100));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak200((BigDecimal)data.get(OP_P200));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak210((BigDecimal)data.get(OP_P210));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak220((BigDecimal)data.get(OP_P220));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak230((BigDecimal)data.get(OP_P230));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak300((BigDecimal)data.get(OP_P300));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak400((BigDecimal)data.get(OP_P400));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak500((BigDecimal)data.get(OP_P500));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak600((BigDecimal)data.get(OP_P600));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak610((BigDecimal)data.get(OP_P610));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak620((BigDecimal)data.get(OP_P620));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak700((BigDecimal)data.get(OP_P700));
    stj.getIsplaceniPrimiciIObracunPoreza().setPodatak800((BigInteger)data.get(OP_P800));
    
    stj.setObracunatiPorezi(factory.createSPorezIprirezPremaOpciniGradu());
    ArrayList<SPorezIprirezPremaOpciniGradu.ObracunatiPorez> oporezi = new ArrayList<SPorezIprirezPremaOpciniGradu.ObracunatiPorez>();
    TreeMap<String, BigDecimal[]> data_opz = (TreeMap<String, BigDecimal[]>)data.get(OBRACUNATI_POREZI); 
    Set<String> copcs = data_opz.keySet();
    for (String sifra : copcs) {
      SPorezIprirezPremaOpciniGradu.ObracunatiPorez opz = factory.createSPorezIprirezPremaOpciniGraduObracunatiPorez();
      BigDecimal[] v = data_opz.get(sifra);
      opz.setSifra(sifra);
      opz.setPoreza(v[0]);
      opz.setPrireza(v[1]);
      opz.setUkupno(v[2]);
      oporezi.add(opz);
    }
    stj.getObracunatiPorezi().obracunatiPorez = oporezi;
    
    stj.setUkupno(factory.createSUkupno());
    stj.getUkupno().setPoreza((BigDecimal)data.get(UKUPNO_POREZA));
    stj.getUkupno().setPrireza((BigDecimal)data.get(UKUPNO_PRIREZA));
    stj.getUkupno().setUkupno((BigDecimal)data.get(UKUPNO_UKUPNO));
    
    return stj;
  }


  private SZaglavlje createZaglavlje() throws Exception {
    SZaglavlje zag = factory.createSZaglavlje();
    zag.setIdentifikator((String)data.get(IDENTIFIKATOR));
    zag.setPodrucniUred((String)data.get(PODRUCNI_URED));
    zag.setIspostava((String)data.get(ISPOSTAVA));
    zag.setPodnositeljZahtjeva(factory.createSZaglavljePodnositeljZahtjeva());
    zag.getPodnositeljZahtjeva().setNaziv((String)data.get(PODNOSITELJ_NAZIV));
    zag.getPodnositeljZahtjeva().setOIB((String)data.get(PODNOSITELJ_OIB));
    zag.getPodnositeljZahtjeva().setAdresa(factory.createSAdresa());
    zag.getPodnositeljZahtjeva().getAdresa().setBroj((String)data.get(PODNOSITELJ_ADRESA_BROJ));
    zag.getPodnositeljZahtjeva().getAdresa().setMjesto((String)data.get(PODNOSITELJ_ADRESA_MJESTO));
    zag.getPodnositeljZahtjeva().getAdresa().setUlica((String)data.get(PODNOSITELJ_ADRESA_ULICA));
    
    zag.setRazdoblje(factory.createSRazdoblje());
    zag.getRazdoblje().setDatumOd(createDatum(PERIOD_OD, false));
    zag.getRazdoblje().setDatumDo(createDatum(PERIOD_DO, false));
    return zag;
  }

  private SIDmetapodaci createSIDmeta() throws Exception {
    return MetaGenerator.generateSIDmeta((String)data.get(AUTOR), data.get(DATUM).toString(), "ID Obrazac", "ObrazacID-v3-0");
//    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.ObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.ObjectFactory();
//    SIDmetapodaci mpd = mf.createSIDmetapodaci();
//    mpd.setAdresant(mf.createSAdresantTemeljni());
//    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
//    mpd.setAutor(mf.createSAutorTemeljni());
//    mpd.getAutor().setValue((String)data.get(AUTOR));
//    mpd.getAutor().setDc(mpd.getAutor().getDc());
//    mpd.setDatum(mf.createSDatumTemeljni());
//    mpd.getDatum().setValue(createDatum(DATUM, true));
//    mpd.getDatum().setDc(mpd.getDatum().getDc());
//    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
//    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
//    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
//    mpd.setNaslov(mf.createSNaslovTemeljni());
//    mpd.getNaslov().setValue("ID Obrazac");
//    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
//    mpd.setUskladjenost(mf.createSUskladjenost());
//    mpd.getUskladjenost().setValue("ObrazacID-v3-0");
//    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
//    //temljni tipovi
//    mpd.setFormat(mf.createSFormatTemeljni());
//    mpd.getFormat().setValue(TFormat.TEXT_XML);
//    mpd.getFormat().setDc(mpd.getFormat().getDc());
//    mpd.setJezik(mf.createSJezikTemeljni());
//    mpd.getJezik().setValue(TJezik.HR_HR);
//    mpd.getJezik().setDc(mpd.getJezik().getDc());
//    mpd.setTip(mf.createSTipTemeljni());
//    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
//    mpd.getTip().setDc(mpd.getTip().getDc());
//    return mpd;
  }


  private XMLGregorianCalendar createDatum(String key, boolean time) throws Exception {
    return MetaGenerator.createDatum(data.get(key).toString(), time);
//    GregorianCalendar c = (GregorianCalendar)Calendar.getInstance();
//    long datum; 
//    try {
//      datum = Long.parseLong(sdat);
//    } catch (Exception e) {
//      datum = System.currentTimeMillis();
//    }
//    c.setTimeInMillis(datum);
//    XMLGregorianCalendar gd = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE), DatatypeConstants.FIELD_UNDEFINED);
//    if (time) {
//      gd.setHour(12);
//      gd.setMinute(0);
//      gd.setSecond(0);
//    }
//    return gd;
  }


  private void marshal(FileOutputStream out) throws JAXBException {
    Marshaller m = context.createMarshaller();
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
    m.marshal(factory.createObrazacID(sid), out);
  }
}
