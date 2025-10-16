package hr.restart.sk;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdv.v9_0.*;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.MetaGenerator;
import hr.restart.util.VarStr;
import hr.restart.util.startFrame;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.repDisk;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.borland.dx.dataset.StorageDataSet;

public class repPDVDisk extends repDisk {
  JAXBContext context;
  ObjectFactory factory;
  
  public repPDVDisk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("PDV_e-porezna.xml");
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


  private void marshall() throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
    m.marshal(factory.createObrazacPDV(sobrazacpdv), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }

  SObrazacPDV sobrazacpdv;
  private void generate() throws Exception {
    sobrazacpdv = factory.createSObrazacPDV();
    sobrazacpdv.setMetapodaci(getMeta());
    sobrazacpdv.setTijelo(getTijelo());
    sobrazacpdv.setZaglavlje(getZaglavlje());
    sobrazacpdv.setVerzijaSheme("8.0");
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
    if (data.containsKey(Generator.SASTAVIO_IME)) sastavio.setIme((String)data.get(Generator.SASTAVIO_IME));
    if (data.containsKey(Generator.SASTAVIO_PREZIME)) sastavio.setPrezime((String)data.get(Generator.SASTAVIO_PREZIME));
    
    String userOverride = frmParam.getParam("sk", 
        "e-sastavioURA", "", 
        "Tko je sastavio obrazac ura (Ime;Prezime;OIB;Naziv)");
    
    if (userOverride != null && userOverride.trim().length() > 0) {
      SPoslovniSubjekt subjekt = new SPoslovniSubjekt();
      
      String[] parts = new VarStr(userOverride).splitTrimmed(';');
      //if (parts.length > 0) subjekt.setIme(parts[0]);
      //if (parts.length > 1) subjekt.setPrezime(parts[1]);
      if (parts.length > 2) subjekt.setOIB(parts[2]);
      if (parts.length > 3) subjekt.setNaziv(parts[3]);
    
      sastavio.setPoslovniSubjekt(subjekt);
    }
    
    /*if (data.containsKey(Generator.SASTAVIO_TEL)) sastavio.setTelefon((String)data.get(Generator.SASTAVIO_TEL));
    if (data.containsKey(Generator.SASTAVIO_FAX)) sastavio.setFax((String)data.get(Generator.SASTAVIO_FAX));
    if (data.containsKey(Generator.SASTAVIO_EMAIL)) sastavio.setEmail((String)data.get(Generator.SASTAVIO_EMAIL));*/
    zag.setObracunSastavio(sastavio);
    
    SRazdoblje razdoblje = factory.createSRazdoblje();
    razdoblje.setDatumOd(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_OD), true));
    razdoblje.setDatumDo(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_DO), true));
    zag.setRazdoblje(razdoblje);

    SZastupnik zastupnik = getZastupnik();
    if (zastupnik != null) {
      zag.setZastupnik(zastupnik);
    }
    return zag;
  }





  private SZastupnik getZastupnik() {
    String corg_zast = frmParam.getParam("sk", "e-zastupnik", "", "Oznaka logotipa koji sadrži podatke o zastupniku kod e-porezne");
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


  private STijelo getTijelo() {
    STijelo tijelo = factory.createSTijelo();
    HashMap<String, BigDecimal> data = tijeloData();

    tijelo.setPodatak000(data.get("Pdv000"));
    tijelo.setPodatak100(data.get("Pdv100"));
    tijelo.setPodatak101(data.get("Pdv101"));
    tijelo.setPodatak102(data.get("Pdv102"));
    tijelo.setPodatak103(data.get("Pdv103"));
    tijelo.setPodatak104(data.get("Pdv104"));
    tijelo.setPodatak105(data.get("Pdv105"));
    tijelo.setPodatak106(data.get("Pdv106"));
    tijelo.setPodatak107(data.get("Pdv107"));
    tijelo.setPodatak108(data.get("Pdv108"));
    tijelo.setPodatak109(data.get("Pdv109"));
    tijelo.setPodatak110(data.get("Pdv110"));
    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), data.get("Pdv200o"), data.get("Pdv200p")));
    tijelo.setPodatak201(addSIsporuka(tijelo.getPodatak201(), data.get("Pdv201o"), data.get("Pdv201p")));
    tijelo.setPodatak202(addSIsporuka(tijelo.getPodatak202(), data.get("Pdv202o"), data.get("Pdv202p")));
    tijelo.setPodatak203(addSIsporuka(tijelo.getPodatak203(), data.get("Pdv203o"), data.get("Pdv203p")));
    tijelo.setPodatak204(addSIsporuka(tijelo.getPodatak204(), data.get("Pdv204o"), data.get("Pdv204p")));
    tijelo.setPodatak205(addSIsporuka(tijelo.getPodatak205(), data.get("Pdv205o"), data.get("Pdv205p")));
    tijelo.setPodatak206(addSIsporuka(tijelo.getPodatak206(), data.get("Pdv206o"), data.get("Pdv206p")));
    tijelo.setPodatak207(addSIsporuka(tijelo.getPodatak207(), data.get("Pdv207o"), data.get("Pdv207p")));
    tijelo.setPodatak208(addSIsporuka(tijelo.getPodatak208(), data.get("Pdv208o"), data.get("Pdv208p")));
    tijelo.setPodatak209(addSIsporuka(tijelo.getPodatak209(), data.get("Pdv209o"), data.get("Pdv209p")));
    tijelo.setPodatak210(addSIsporuka(tijelo.getPodatak210(), data.get("Pdv210o"), data.get("Pdv210p")));
    tijelo.setPodatak211(addSIsporuka(tijelo.getPodatak211(), data.get("Pdv211o"), data.get("Pdv211p")));
    tijelo.setPodatak212(addSIsporuka(tijelo.getPodatak212(), data.get("Pdv212o"), data.get("Pdv212p")));
    tijelo.setPodatak213(addSIsporuka(tijelo.getPodatak213(), data.get("Pdv213o"), data.get("Pdv213p")));
    tijelo.setPodatak214(addSIsporuka(tijelo.getPodatak214(), data.get("Pdv214o"), data.get("Pdv214p")));
    tijelo.setPodatak215(addSIsporuka(tijelo.getPodatak215(), data.get("Pdv215o"), data.get("Pdv215p")));

    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), data.get("Pdv300o"), data.get("Pdv300p")));
    tijelo.setPodatak301(addSIsporuka(tijelo.getPodatak301(), data.get("Pdv301o"), data.get("Pdv301p")));
    tijelo.setPodatak302(addSIsporuka(tijelo.getPodatak302(), data.get("Pdv302o"), data.get("Pdv302p")));
    tijelo.setPodatak303(addSIsporuka(tijelo.getPodatak303(), data.get("Pdv303o"), data.get("Pdv303p")));
    tijelo.setPodatak304(addSIsporuka(tijelo.getPodatak304(), data.get("Pdv304o"), data.get("Pdv304p")));
    tijelo.setPodatak305(addSIsporuka(tijelo.getPodatak305(), data.get("Pdv305o"), data.get("Pdv305p")));
    tijelo.setPodatak306(addSIsporuka(tijelo.getPodatak306(), data.get("Pdv306o"), data.get("Pdv306p")));
    tijelo.setPodatak307(addSIsporuka(tijelo.getPodatak307(), data.get("Pdv307o"), data.get("Pdv307p")));
    tijelo.setPodatak308(addSIsporuka(tijelo.getPodatak308(), data.get("Pdv308o"), data.get("Pdv308p")));
    tijelo.setPodatak309(addSIsporuka(tijelo.getPodatak309(), data.get("Pdv309o"), data.get("Pdv309p")));
    tijelo.setPodatak310(addSIsporuka(tijelo.getPodatak310(), data.get("Pdv310o"), data.get("Pdv310p")));
    tijelo.setPodatak311(addSIsporuka(tijelo.getPodatak311(), data.get("Pdv311o"), data.get("Pdv311p")));
    tijelo.setPodatak312(addSIsporuka(tijelo.getPodatak312(), data.get("Pdv312o"), data.get("Pdv312p")));
    tijelo.setPodatak313(addSIsporuka(tijelo.getPodatak313(), data.get("Pdv313o"), data.get("Pdv313p")));
    tijelo.setPodatak314(addSIsporuka(tijelo.getPodatak314(), data.get("Pdv314o"), data.get("Pdv314p")));
    tijelo.setPodatak315(data.get("Pdv315p"));
    tijelo.setPodatak400(data.get("Pdv400"));
    tijelo.setPodatak500(data.get("Pdv500"));
    tijelo.setPodatak600(data.get("Pdv600"));
    tijelo.setPodatak700(data.get("Pdv700"));
    
//    for (Iterator iterator = data.keySet().iterator(); iterator.hasNext();) {
//      String ciz = (String) iterator.next();
//      System.out.println("** "+ciz+" = "+data.get(ciz));
//    }
//    return sumTijelo(tijelo);
    return tijelo;
  }
  /*
Pod100  I. TRANSAKCIJE KOJE NE PODLIJEŽU OPOREZIVANJU I OSLOBOÐENE - UKUPNO (1.+2.+3.+4.+5.+6.+7.+8.+9.+10.)
Pod200  II. OPOREZIVE TRANSAKCIJE UKUPNO (1.+2.+3.+4.+5.+6.+7.+8.+9.+10.+11.+12.+13.+14.)
Pod300  III. OBRAČUNANI PRETPOREZ – UKUPNO (1.+2.+3.+4.+5.+6.+7.)
Pod400  IV. OBVEZA PDV-a U OBRAČUNSKOM RAZDOBLJU:ZA UPLATU (II. - III.) ILI ZA POVRAT (III. - II.)
Pod600  VI. UKUPNO RAZLIKA: ZA UPLATU/ZA POVRAT
   */

  private SIsporuka addSIsporuka(SIsporuka isp, SIsporuka augend) {
    if (augend == null) augend = addSIsporuka(augend, Aus.zero2, Aus.zero2);
    return addSIsporuka(isp, augend.getVrijednost(), augend.getPorez());
  }
  private SIsporuka addSIsporuka(SIsporuka isp, BigDecimal vri, BigDecimal por) {
    if (isp == null) {
      isp = factory.createSIsporuka();
      isp.setVrijednost(Aus.zero2);
      isp.setPorez(Aus.zero2);
    }
    vri = (vri == null)?Aus.zero2:vri;
    por = (por == null)?Aus.zero2:por;
    isp.setVrijednost(isp.getVrijednost().add(vri));
    isp.setPorez(isp.getPorez().add(por));
    return isp;
  }


  public HashMap<String, BigDecimal> tijeloData() {
    HashMap<String, BigDecimal> data = new HashMap<String, BigDecimal>();
    //populate empty data from dataset (kvercina prebacena u frmPDV2)
    StorageDataSet setPDV = frmPDV2.getInstance().getSetPDV();
    for (setPDV.first(); setPDV.inBounds(); setPDV.next()) {
      String poz = setPDV.getString("POZ");
      if (poz.toLowerCase().trim().endsWith("o")) {
        data.put(poz, setPDV.getBigDecimal("OSN"));
        data.put(new VarStr(poz).replaceLast("o", "p").toString(), setPDV.getBigDecimal("PDV"));
      } else {
        data.put(poz, setPDV.getBigDecimal("OSN").add(setPDV.getBigDecimal("PDV")));
      }
    }
    return data;
  }

  private hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPDVmetapodaci getMeta() throws Exception {
    return MetaGenerator.generateSPDVmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Prijava poreza na dodanu vrijednost", "ObrazacPDV-v8-0");
  }


  private void initialize() throws Exception {
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdv.v8_0");
    factory = new ObjectFactory();
  }

  //test
  public static void main(String[] args) {
    startFrame.getStartFrame();
    repPDVDisk a = new repPDVDisk();
    frmPDV p = frmPDV.getInstance();
    a.tijeloData();

  }
}
