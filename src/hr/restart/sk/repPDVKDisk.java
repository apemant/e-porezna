package hr.restart.sk;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdvk.v6_0.*;
import hr.restart.baza.Condition;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.StIzvjPDV;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.MetaGenerator;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.startFrame;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.dlgGetKnjig;
import hr.restart.zapod.repDisk;

public class repPDVKDisk extends repDisk {
  JAXBContext context;
  ObjectFactory factory;
  
  public repPDVKDisk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("PDVK_e-porezna.xml");
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
    m.marshal(factory.createObrazacPDVK(sobrazacpdvk), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }

  SObrazacPDVK sobrazacpdvk;
  private void generate() throws Exception {
    sobrazacpdvk = factory.createSObrazacPDVK();
    sobrazacpdvk.setMetapodaci(getMeta());
    sobrazacpdvk.setTijelo(getTijelo());
    sobrazacpdvk.setZaglavlje(getZaglavlje());
    sobrazacpdvk.setVerzijaSheme("7.0");
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
    if (data.containsKey(Generator.SASTAVIO_TEL)) sastavio.setTelefon((String)data.get(Generator.SASTAVIO_TEL));
    if (data.containsKey(Generator.SASTAVIO_FAX)) sastavio.setFax((String)data.get(Generator.SASTAVIO_FAX));
    if (data.containsKey(Generator.SASTAVIO_EMAIL)) sastavio.setEmail((String)data.get(Generator.SASTAVIO_EMAIL));
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
    if (zastdata.containsKey(Generator.SASTAVIO_IME)) zast.setIme((String)zastdata.get(Generator.SASTAVIO_IME));
    if (zastdata.containsKey(Generator.SASTAVIO_PREZIME)) zast.setPrezime((String)zastdata.get(Generator.SASTAVIO_PREZIME));
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
    
    tijelo.setPodatak000(data.get("Pod000"));
    tijelo.setPodatak100(data.get("Pod100"));
    tijelo.setPodatak101(data.get("Pod101"));
    tijelo.setPodatak102(data.get("Pod102"));
    tijelo.setPodatak103(data.get("Pod103"));
    tijelo.setPodatak104(data.get("Pod104"));
    tijelo.setPodatak105(data.get("Pod105"));
    tijelo.setPodatak106(data.get("Pod106"));
    tijelo.setPodatak107(data.get("Pod107"));
    tijelo.setPodatak108(data.get("Pod108"));
    tijelo.setPodatak109(data.get("Pod109"));
    tijelo.setPodatak110(data.get("Pod110"));
    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), data.get("Pod200o"), data.get("Pod200p")));
    tijelo.setPodatak201(addSIsporuka(tijelo.getPodatak201(), data.get("Pod201o"), data.get("Pod201p")));
    tijelo.setPodatak202(addSIsporuka(tijelo.getPodatak202(), data.get("Pod202o"), data.get("Pod202p")));
    tijelo.setPodatak203(addSIsporuka(tijelo.getPodatak203(), data.get("Pod203o"), data.get("Pod203p")));
    tijelo.setPodatak204(addSIsporuka(tijelo.getPodatak204(), data.get("Pod204o"), data.get("Pod204p")));
    tijelo.setPodatak205(addSIsporuka(tijelo.getPodatak205(), data.get("Pod205o"), data.get("Pod205p")));
    tijelo.setPodatak206(addSIsporuka(tijelo.getPodatak206(), data.get("Pod206o"), data.get("Pod206p")));
    tijelo.setPodatak207(addSIsporuka(tijelo.getPodatak207(), data.get("Pod207o"), data.get("Pod207p")));
    tijelo.setPodatak208(addSIsporuka(tijelo.getPodatak208(), data.get("Pod208o"), data.get("Pod208p")));
    tijelo.setPodatak209(addSIsporuka(tijelo.getPodatak209(), data.get("Pod209o"), data.get("Pod209p")));
    tijelo.setPodatak210(addSIsporuka(tijelo.getPodatak210(), data.get("Pod210o"), data.get("Pod210p")));
    tijelo.setPodatak211(addSIsporuka(tijelo.getPodatak211(), data.get("Pod211o"), data.get("Pod211p")));
    tijelo.setPodatak212(addSIsporuka(tijelo.getPodatak212(), data.get("Pod212o"), data.get("Pod212p")));
    tijelo.setPodatak213(addSIsporuka(tijelo.getPodatak213(), data.get("Pod213o"), data.get("Pod213p")));
    tijelo.setPodatak214(addSIsporuka(tijelo.getPodatak214(), data.get("Pod214o"), data.get("Pod214p")));

    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), data.get("Pod300o"), data.get("Pod300p")));
    tijelo.setPodatak301(addSIsporuka(tijelo.getPodatak301(), data.get("Pod301o"), data.get("Pod301p")));
    tijelo.setPodatak302(addSIsporuka(tijelo.getPodatak302(), data.get("Pod302o"), data.get("Pod302p")));
    tijelo.setPodatak303(addSIsporuka(tijelo.getPodatak303(), data.get("Pod303o"), data.get("Pod303p")));
    tijelo.setPodatak304(addSIsporuka(tijelo.getPodatak304(), data.get("Pod304o"), data.get("Pod304p")));
    tijelo.setPodatak305(addSIsporuka(tijelo.getPodatak305(), data.get("Pod305o"), data.get("Pod305p")));
    tijelo.setPodatak306(addSIsporuka(tijelo.getPodatak306(), data.get("Pod306o"), data.get("Pod306p")));
    tijelo.setPodatak307(data.get("Pod307p"));
    tijelo.setPodatak400(data.get("Pod400"));
    tijelo.setPodatak500(data.get("Pod500"));
    tijelo.setPodatak600(data.get("Pod600"));
    tijelo.setPodatak700(data.get("Pod700"));
    tijelo.setPodatak810(data.get("Pok810"));
    tijelo.setPodatak811(data.get("Pok811"));
    tijelo.setPodatak812(data.get("Pok812"));
    tijelo.setPodatak813(data.get("Pok813"));
    tijelo.setPodatak814(data.get("Pok814"));
    tijelo.setPodatak815(data.get("Pok815"));
    tijelo.setPodatak816(data.get("Pok816"));
    tijelo.setPodatak820(data.get("Pok820"));
    tijelo.setPodatak830(data.get("Pok830"));
    tijelo.setPodatak840(data.get("Pok840"));
//    tijelo.setPodatak850();
    
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
//  private STijelo sumTijelo(STijelo tijelo) {
//    tijelo.setPodatak100(tijelo.getPodatak101()
//        .add(tijelo.getPodatak102())
//        .add(tijelo.getPodatak103())
//        .add(tijelo.getPodatak104())
//        .add(tijelo.getPodatak105())
//        .add(tijelo.getPodatak106())
//        .add(tijelo.getPodatak107())
//        .add(tijelo.getPodatak108())
//        .add(tijelo.getPodatak109())
//        .add(tijelo.getPodatak110())
//        );
//    
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak201()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak202()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak203()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak204()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak205()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak206()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak207()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak208()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak209()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak210()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak211()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak212()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak213()));
//    tijelo.setPodatak200(addSIsporuka(tijelo.getPodatak200(), tijelo.getPodatak214()));
//    
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), tijelo.getPodatak301()));
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), tijelo.getPodatak302()));
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), tijelo.getPodatak303()));
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), tijelo.getPodatak304()));
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), tijelo.getPodatak305()));
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), tijelo.getPodatak306()));
//    tijelo.setPodatak300(addSIsporuka(tijelo.getPodatak300(), Aus.zero2, tijelo.getPodatak307()));
//    
//    tijelo.setPodatak400(tijelo.getPodatak200().getPorez().subtract(tijelo.getPodatak300().getPorez()));
//    if (tijelo.getPodatak500() == null) tijelo.setPodatak500(Aus.zero2);
//    tijelo.setPodatak600(tijelo.getPodatak400().subtract(tijelo.getPodatak500()));
//    
//    tijelo.setPodatak000(tijelo.getPodatak100().add(tijelo.getPodatak200().getVrijednost()));
//    return tijelo;
//  }

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
    StorageDataSet setPDV_K = frmPDV2.getInstance().getSetPDV_K();
    for (setPDV_K.first(); setPDV_K.inBounds(); setPDV_K.next()) {
      String poz = setPDV_K.getString("POZ");
      if (poz.toLowerCase().trim().endsWith("o")) {
        data.put(poz, setPDV_K.getBigDecimal("OSN"));
        data.put(new VarStr(poz).replaceLast("o", "p").toString(), setPDV_K.getBigDecimal("PDV"));
      } else {
        data.put(poz, setPDV_K.getBigDecimal("OSN").add(setPDV_K.getBigDecimal("PDV")));
      }
    }
    return data;
  }

  private hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPDVKmetapodaci getMeta() throws Exception {
    return MetaGenerator.generateSPDVKmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Prijava poreza na dodanu vrijednost", "ObrazacPDVK-v6-0");
  }


  private void initialize() throws Exception {
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdvk.v6_0");
    factory = new ObjectFactory();
  }

  //test
  public static void main(String[] args) {
    startFrame.getStartFrame();
    repPDVKDisk a = new repPDVKDisk();
    frmPDV p = frmPDV.getInstance();
    a.tijeloData();

  }
}
