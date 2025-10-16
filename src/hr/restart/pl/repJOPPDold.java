package hr.restart.pl;

import hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SJOPPDmetapodaci;
import hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SZPmetapodaci;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacjoppd.v1_0.*;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.sk.JOPPDhndlr;
import hr.restart.sk.frmPDV2;
import hr.restart.util.Aus;
import hr.restart.util.MetaGenerator;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.repDisk;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.borland.dx.dataset.StorageDataSet;

public class repJOPPDold extends repDisk {

  JAXBContext context;
  ObjectFactory factory;
  public repJOPPDold() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("JOPPD_e-porezna_1_0.xml");
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
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacjoppd.v1_0");
    factory = new ObjectFactory();    
  }


  private void marshall() throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
    m.marshal(factory.createObrazacJOPPD(sobrazacjoppd), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }
  
  SObrazacJOPPD sobrazacjoppd;
  private void generate() throws Exception {
    sobrazacjoppd = factory.createSObrazacJOPPD();
    sobrazacjoppd.setMetapodaci(getMeta());
    sobrazacjoppd.setStranaA(getStranaA());
    sobrazacjoppd.setStranaB(getStranaB());
    sobrazacjoppd.setVerzijaSheme("1.0");
  }

  private SStranaA getStranaA() throws Exception {
    frmPDV2 p1 = frmPDV2.getInstance();
    JOPPDhndlr p2 = p1.getJOPPD();
    SStranaA strA = factory.createSStranaA();
    HashMap<String, Object> data = MetaGenerator.headerData(p1.getDatumOd().getTime(), p1.getDatumDo().getTime());
    StorageDataSet strAset = p2.getStrAset();
    strA.setDatumIzvjesca(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_OD), true));
    strA.setVrstaIzvjesca(BigInteger.valueOf(strAset.getInt("VRSTAIZV")));
    strA.setOznakaIzvjesca(strAset.getString("OZNIZV"));
    //podnositelj
    SPodnositeljIzvjesca podnositelj = factory.createSPodnositeljIzvjesca();
    SAdresa adresa = factory.createSAdresa();
    adresa.setUlica((String)data.get(Generator.PODNOSITELJ_ADRESA_ULICA));
    adresa.setMjesto((String)data.get(Generator.PODNOSITELJ_ADRESA_MJESTO));
    adresa.setBroj((String)data.get(Generator.PODNOSITELJ_ADRESA_BROJ));
    podnositelj.setAdresa(adresa);
    podnositelj.setNaziv((String)data.get(Generator.PODNOSITELJ_NAZIV));
    podnositelj.setOIB((String)data.get(Generator.PODNOSITELJ_OIB));
    podnositelj.setOznaka(strAset.getString("OZNPOD"));
    podnositelj.setEmail((String)data.get(Generator.SASTAVIO_EMAIL));
    strA.setPodnositeljIzvjesca(podnositelj);
    //obveznik u strAset = prioritet 2
    
    //
    strA.setBrojOsoba(p2.getBrojOsoba());
    strA.setBrojRedaka(p2.getBrojRedaka());
    //porez
    SPredujamPoreza porez = factory.createSPredujamPoreza();
    porez.setP1(p2.getSumPorPrirUk());
    porez.setP11(p2.getSumPorPrirPl());
    porez.setP12(p2.getSumPorPrirMir());
    porez.setP2(p2.getSumPorPrirKap()); //
    porez.setP3(p2.getSumPorPrirImo()); //
    porez.setP4(p2.getSumPorPrirOs()); //
    porez.setP5(p2.getSumPorPrirDD()); //
    strA.setPredujamPoreza(porez);
    //doprinosi
    SDoprinosi doprinosi = factory.createSDoprinosi();
    SGeneracijskaSolidarnost gen = factory.createSGeneracijskaSolidarnost();
    gen.setP1(p2.getSumMIO1Pl());
    gen.setP2(p2.getSumMIO1DD());
    gen.setP3(p2.getSumMIO1Pod());
    gen.setP4(p2.getSumMIO1PP());
    gen.setP5(p2.getSumMIO1OO());
    gen.setP6(p2.getSumMIO1Staz());
    doprinosi.setGeneracijskaSolidarnost(gen);
    SKapitaliziranaStednja kap = factory.createSKapitaliziranaStednja();
    kap.setP1(p2.getSumMIO2Pl());
    kap.setP2(p2.getSumMIO2DD());
    kap.setP3(p2.getSumMIO2Pod());
    kap.setP4(p2.getSumMIO2PP());
    kap.setP5(p2.getSumMIO2Staz());
    doprinosi.setKapitaliziranaStednja(kap);
    SZdravstvenoOsiguranje zos = factory.createSZdravstvenoOsiguranje();
    zos.setP1(p2.getSumZdrPl());
    zos.setP2(p2.getSumZasNRPl());
    zos.setP3(p2.getSumZdrPod());
    zos.setP4(p2.getSumZasNRPod());
    zos.setP5(p2.getSumZdrDD());
    zos.setP6(p2.getSumZdrINO());
    zos.setP7(p2.getSumZdrPenzici());
    zos.setP8(p2.getSumZdrPP());
    zos.setP9(p2.getSumZasNRPP());;
    zos.setP10(p2.getSumZasNROO());
    doprinosi.setZdravstvenoOsiguranje(zos);
    SZaposljavanje zap = factory.createSZaposljavanje();
    zap.setP1(p2.getSumZap());
    zap.setP2(p2.getSumZapOsInv());
    zap.setP3(p2.getSumZapPoduz());
    doprinosi.setZaposljavanje(zap);
    
    strA.setDoprinosi(doprinosi);
    strA.setIsplaceniNeoporeziviPrimici(p2.getSumNeoporeziviPrimici());
    strA.setKamataMO2(p2.getKamataMO2());
    
    SIzvjesceSastavio sastavio = factory.createSIzvjesceSastavio();
    sastavio.setIme((String)data.get(Generator.SASTAVIO_IME));
    sastavio.setPrezime((String)data.get(Generator.SASTAVIO_PREZIME));
    strA.setIzvjesceSastavio(sastavio);
    return strA;
  }

  private SStranaB getStranaB() throws Exception {
    JOPPDhndlr p2 = frmPDV2.getInstance().getJOPPD();
    SStranaB strB = factory.createSStranaB();
    SPrimatelji primatelji = factory.createSPrimatelji();
    StorageDataSet strBset = p2.getStrBset();
    for (p2.getStrBset().first(); p2.getStrBset().inBounds(); p2.getStrBset().next()) {
      SPrimatelji.P p = factory.createSPrimateljiP();
      p.setP1(strBset.getInt("RBR"));
      p.setP2(strBset.getString("COPCINE"));
      p.setP3(strBset.getString("COPRADA"));
      p.setP4(strBset.getString("OIB"));
      p.setP5(strBset.getString("IMEPREZ"));
      p.setP61(strBset.getString("JOS"));
      p.setP62(strBset.getString("JOP"));
      p.setP71(strBset.getString("JOB"));
      p.setP72(strBset.getString("JOZ"));
      p.setP8(strBset.getString("JOM"));
      p.setP9(strBset.getString("JRV"));
      p.setP10(strBset.getInt("SATI"));
      p.setP101(MetaGenerator.createDatum(strBset.getTimestamp("ODJ").getTime()+"",true));
      p.setP102(MetaGenerator.createDatum(strBset.getTimestamp("DOJ").getTime()+"",true));
      p.setP11(strBset.getBigDecimal("BRUTO"));
      p.setP12(strBset.getBigDecimal("OSNDOP"));
      p.setP121(strBset.getBigDecimal("MIO1"));
      p.setP122(strBset.getBigDecimal("MIO2"));
      p.setP123(strBset.getBigDecimal("ZDR"));
      p.setP124(strBset.getBigDecimal("ZASNR"));
      p.setP125(strBset.getBigDecimal("ZAP"));
      p.setP126(strBset.getBigDecimal("MIO1STAZ"));
      p.setP127(strBset.getBigDecimal("MIO2STAZ"));
      p.setP128(strBset.getBigDecimal("ZDRINO"));
      p.setP129(strBset.getBigDecimal("ZAPOSINV"));
      p.setP131(strBset.getBigDecimal("IZDATAK"));
      p.setP132(strBset.getBigDecimal("IZDATAKMIO"));
      p.setP133(strBset.getBigDecimal("DOHODAK"));
      p.setP134(strBset.getBigDecimal("ISKNEOP"));
      p.setP135(strBset.getBigDecimal("POROSN"));
      p.setP141(strBset.getBigDecimal("POR"));
      p.setP142(strBset.getBigDecimal("PRIR"));
      p.setP151(strBset.getString("JNP"));
      p.setP152(strBset.getBigDecimal("NEOP"));
      p.setP161(strBset.getString("JNI"));
      p.setP162(strBset.getBigDecimal("NETOPK"));
      p.setP17(strBset.getBigDecimal("BRUTOOBR"));
      
      primatelji.getP().add(p);
    }
 
    strB.getPrimatelji().add(primatelji);
    return strB;
  }


  private SJOPPDmetapodaci getMeta() throws Exception {
    SJOPPDmetapodaci meta = MetaGenerator.generateSJOPPDmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Izvješće o primicima, porezu na dohodak i prirezu te doprinosima za obvezna osiguranja", "ObrazacJOPPD-v1-0");
    return meta;
  }

}
