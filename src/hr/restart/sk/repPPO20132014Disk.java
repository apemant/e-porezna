package hr.restart.sk;

import hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPPOmetapodaci;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacppo.v1_0.*;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.MetaGenerator;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.repDisk;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;

public class repPPO20132014Disk extends repDisk {

  JAXBContext context;
  ObjectFactory factory;
  public repPPO20132014Disk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("PPO_e-porezna.xml");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void makeReport() {
    try {
      initialize();
      generate();
      marshall();
      //new Generator(makeData(), new FileOutputStream(mxReport.TMPPRINTFILE));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void initialize() throws Exception {
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacppo.v1_0");
    factory = new ObjectFactory();    
  }


  private void marshall() throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
    m.marshal(factory.createObrazacPPO(sobrazacppo), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }
  
  SObrazacPPO sobrazacppo;
  private void generate() throws Exception {
    sobrazacppo = factory.createSObrazacPPO();
    sobrazacppo.setMetapodaci(getMeta());
    sobrazacppo.setTijelo(getTijelo());
    sobrazacppo.setZaglavlje(getZaglavlje());
    sobrazacppo.setVerzijaSheme("1.0");
  }


  private SZaglavlje getZaglavlje() throws Exception {
    SZaglavlje zag = factory.createSZaglavlje();
    HashMap<String, Object> data = MetaGenerator.headerData(frmPDV2.getInstance().getDatumOd().getTime(), frmPDV2.getInstance().getDatumDo().getTime());
    zag.setIspostava((String)data.get(Generator.ISPOSTAVA));

    SPoslovniSubjektProsireno obveznik = factory.createSPoslovniSubjektProsireno();
    SAdresa adresa = factory.createSAdresa();
    adresa.setUlica((String)data.get(Generator.PODNOSITELJ_ADRESA_ULICA));
    adresa.setMjesto((String)data.get(Generator.PODNOSITELJ_ADRESA_MJESTO));
    adresa.setBroj((String)data.get(Generator.PODNOSITELJ_ADRESA_BROJ));
    obveznik.setAdresa(adresa);
    obveznik.setNaziv((String)data.get(Generator.PODNOSITELJ_NAZIV));
    obveznik.setOIB((String)data.get(Generator.PODNOSITELJ_OIB));
    zag.setObveznik(obveznik);
    
    SObracunSastavio sastavio = factory.createSObracunSastavio();
    sastavio.setIme((String)data.get(Generator.SASTAVIO_IME));
    sastavio.setPrezime((String)data.get(Generator.SASTAVIO_PREZIME));
    sastavio.setTelefon((String)data.get(Generator.SASTAVIO_TEL));
    sastavio.setFax((String)data.get(Generator.SASTAVIO_FAX));
    sastavio.setEmail((String)data.get(Generator.SASTAVIO_EMAIL));
    String userOverride = frmParam.getParam("sk", 
        "e-sastavioURA", "", 
        "Tko je sastavio obrazac ura (Ime;Prezime;OIB;Naziv)");
    
    if (userOverride != null && userOverride.trim().length() > 0) {
      SPoslovniSubjekt subjekt = new SPoslovniSubjekt();
      
      String[] parts = new VarStr(userOverride).splitTrimmed(';');
      if (parts.length > 0 && parts.length <= 3) subjekt.setIme(parts[0]);
      if (parts.length > 1 && parts.length <= 3) subjekt.setPrezime(parts[1]);
      if (parts.length > 2) subjekt.setOIB(parts[2]);
      if (parts.length > 3) subjekt.setNaziv(parts[3]);
    
      sastavio.setPoslovniSubjekt(subjekt);
    }
    
    zag.setObracunSastavio(sastavio);
    
    SRazdoblje razdoblje = factory.createSRazdoblje();
    razdoblje.setDatumOd(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_OD), true));
    razdoblje.setDatumDo(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_DO), true));
    System.err.println(razdoblje.getDatumOd().getYear());
    System.err.println(razdoblje.getDatumOd().getMonth());
    
    String god = frmPDV2.getInstance().getDatumDo().toString().substring(0, 4);
    String mj = frmPDV2.getInstance().getDatumDo().toString().substring(5, 7);
    
    razdoblje.setGodina(new BigInteger(god));
    razdoblje.setTromjesecje(new BigInteger(Integer.toString((Integer.parseInt(mj) - 1) / 3 + 1)));
    
    //razdoblje.setOznaka(new BigInteger(razdoblje.getDatumOd().getYear()==2013?"1":razdoblje.getDatumOd().getMonth()==1?"2":"3"));
    zag.setRazdoblje(razdoblje);

    SZastupnik zastupnik = getZastupnik();
    if (zastupnik != null) {
      zag.setZastupnik(zastupnik);
    }
    return zag;
  }

  private SZastupnik getZastupnik() {
    String corg_zast = frmParam.getParam("sk", "e-zastupnik", "", "Oznaka logotipa koji sadrži podatke o zastupniku kod e-porezne");
    //HashMap<String, Object> zastdata = MetaGenerator.headerData(frmPDV2.getInstance().getDatumOd().getTime(),
    //    frmPDV2.getInstance().getDatumDo().getTime(), corg_zast);
    //if (zastdata == null) return null;
    //SZastupnik zast = factory.createSZastupnik();
    // bit će ovo u novijim verzijama 100%
//    zast.setNaziv((String)zastdata.get(Generator.PODNOSITELJ_NAZIV));
//    if (zastdata.containsKey(Generator.SASTAVIO_IME)) zast.setIme((String)zastdata.get(Generator.SASTAVIO_IME));
//    if (zastdata.containsKey(Generator.SASTAVIO_PREZIME)) zast.setPrezime((String)zastdata.get(Generator.SASTAVIO_PREZIME));
//    SAdresa adresa = factory.createSAdresa();
//    
//    adresa.setUlica((String)zastdata.get(Generator.PODNOSITELJ_ADRESA_ULICA));
//    adresa.setMjesto((String)zastdata.get(Generator.PODNOSITELJ_ADRESA_MJESTO));
//    adresa.setBroj((String)zastdata.get(Generator.PODNOSITELJ_ADRESA_BROJ));
//    zast.setAdresa(adresa);
    //zast.setOIB((String)zastdata.get(Generator.PODNOSITELJ_OIB));
    //return zast;
    
    
    HashMap<String, Object> zastdata = MetaGenerator.headerData(frmPDV2.getInstance().getDatumOd().getTime(),
        frmPDV2.getInstance().getDatumDo().getTime(), corg_zast);
    if (zastdata == null) return null;
    SZastupnik zast = factory.createSZastupnik();
    zast.setNaziv((String)zastdata.get(Generator.PODNOSITELJ_NAZIV));
    if (zast.getNaziv() == null) {
      if (zastdata.containsKey(Generator.SASTAVIO_IME)) zast.setIme((String)zastdata.get(Generator.SASTAVIO_IME));
      if (zastdata.containsKey(Generator.SASTAVIO_PREZIME)) zast.setPrezime((String)zastdata.get(Generator.SASTAVIO_PREZIME));
    }
    SAdresa adresa = factory.createSAdresa();
    
    adresa.setUlica((String)zastdata.get(Generator.PODNOSITELJ_ADRESA_ULICA));
    adresa.setMjesto((String)zastdata.get(Generator.PODNOSITELJ_ADRESA_MJESTO));
    adresa.setBroj((String)zastdata.get(Generator.PODNOSITELJ_ADRESA_BROJ));
    zast.setAdresa(adresa);
    zast.setOIB((String)zastdata.get(Generator.PODNOSITELJ_OIB));
    return zast;
  }
  private STijelo getTijelo() throws Exception {
    STijelo tijelo = factory.createSTijelo();
    SIsporuke isporuke = factory.createSIsporuke();
    BigDecimal ukupno = Aus.zero2;
    BigDecimal sveukupno = Aus.zero2;
    StorageDataSet setPPO = frmPDV2.getInstance().getSetPPO();
    setPPO.setSort(new SortDescriptor(new String[] {"DATUMOD"}));
    Timestamp datumod = frmPDV2.getInstance().getDatumOd();
    Timestamp datumdo = frmPDV2.getInstance().getDatumDo();
    Timestamp dod = null;
    SIsporukaPodatak isporukapodatak = null;
    SPodaci podaci = null;
    int rbr = 0;
    //for (setPPO.first(); setPPO.inBounds(); setPPO.next()) {
    Calendar c = Calendar.getInstance();
    c.setTime(datumod);
    int month = c.get(Calendar.MONTH);
    int year = c.get(Calendar.YEAR);
    c.setTime(datumdo);
    int lastmonth = c.get(Calendar.MONTH);
    while (month <= lastmonth) {
      isporukapodatak = factory.createSIsporukaPodatak();
      ukupno = Aus.zero2;
      podaci = null;
      rbr = 0;
      Calendar c2 = Calendar.getInstance();
      c2.clear();
      c2.set(Calendar.YEAR, year);
      c2.set(Calendar.MONTH, month);
      c2.set(Calendar.DATE, 1);
      isporukapodatak.setDatumOd(MetaGenerator.createDatum(c2.getTimeInMillis()+"", false));
      dod = new Timestamp(c2.getTimeInMillis());
      c2.set(Calendar.DATE, c2.getActualMaximum(Calendar.DATE));
      isporukapodatak.setDatumDo(MetaGenerator.createDatum(c2.getTimeInMillis()+"", false));
      for (setPPO.first();setPPO.inBounds();setPPO.next()) {
        if (Util.getUtil().sameDay(dod, setPPO.getTimestamp("DATUMOD"))) {
        //if (dod.compareTo(setPPO.getTimestamp("DATUMOD")) == 0) {
          if (podaci == null) podaci = factory.createSPodaci();
          //dod = new Timestamp(setPPO.getTimestamp("DATUMOD").getTime());
          SPodatak podatak = factory.createSPodatak();
          podatak.setIznos(setPPO.getBigDecimal("VRI"));
          ukupno = ukupno.add(setPPO.getBigDecimal("VRI"));
          sveukupno = sveukupno.add(setPPO.getBigDecimal("VRI"));
          rbr++;
          podatak.setRedniBroj(new BigInteger(rbr+""));
          podatak.setOIB(setPPO.getString("OIB"));
          podaci.getPodatak().add(podatak);
        }      
      }
      if (podaci != null) {
        isporukapodatak.setPodaci(podaci);
      } else {
        isporukapodatak.setPodaci(podaci = factory.createSPodaci());
      }
      isporukapodatak.setIznos(ukupno);
      isporuke.getIsporuka().add(isporukapodatak);
      month++;
    }
    tijelo.setIsporuke(isporuke);
    tijelo.setUkupno(sveukupno);
    return tijelo;
  }


  private SPPOmetapodaci getMeta() throws Exception {
    return MetaGenerator.generateSPPOmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Prijava prijenosa porezne obveze", "ObrazacPPO-v1-0");
  }

}
