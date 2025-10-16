package hr.restart.sk;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.borland.dx.dataset.StorageDataSet;

import hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPDVSmetapodaci;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdvs.v1_0.*;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.MetaGenerator;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.repDisk;

public class repPDVSDisk extends repDisk {

  JAXBContext context;
  ObjectFactory factory;
  public repPDVSDisk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("PDV-S_e-porezna.xml");
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
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdvs.v1_0");
    factory = new ObjectFactory();    
  }


  private void marshall() throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
    m.marshal(factory.createObrazacPDVS(sobrazacpdvs), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }
  
  SObrazacPDVS sobrazacpdvs;
  private void generate() throws Exception {
    sobrazacpdvs = factory.createSObrazacPDVS();
    sobrazacpdvs.setMetapodaci(getMeta());
    sobrazacpdvs.setTijelo(getTijelo());
    sobrazacpdvs.setZaglavlje(getZaglavlje());
    sobrazacpdvs.setVerzijaSheme("1.0");
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
    zast.setOIB((String)zastdata.get(Generator.PODNOSITELJ_OIB));
    return zast;
  }
  private STijelo getTijelo() {
    STijelo tijelo = factory.createSTijelo();
    STijelo.Isporuke isporuke = factory.createSTijeloIsporuke();
    SIsporukeUkupno total = factory.createSIsporukeUkupno();
    total.setI1(Aus.zero2);
    total.setI2(Aus.zero2);
    StorageDataSet setPDVS = frmPDV2.getInstance().getSetPDVS();
    for (setPDVS.first(); setPDVS.inBounds(); setPDVS.next()) {
      SIsporuka isporuka = factory.createSIsporuka();
      isporuka.setRedBr(new BigInteger(setPDVS.getInt("RBR")+""));
      isporuka.setKodDrzave(TKodDrzave.valueOf(setPDVS.getString("KODDRZ")));
      isporuka.setPDVID(setPDVS.getString("PDVID"));
      isporuka.setI1(setPDVS.getBigDecimal("I1"));
      isporuka.setI2(setPDVS.getBigDecimal("I2"));
      total.setI1(total.getI1().add(isporuka.getI1()));
      total.setI2(total.getI2().add(isporuka.getI2()));
      isporuke.getIsporuka().add(isporuka);
    }
    tijelo.setIsporuke(isporuke);
    tijelo.setIsporukeUkupno(total);
    return tijelo;
  }


  private SPDVSmetapodaci getMeta() throws Exception {
    return MetaGenerator.generateSPDVSmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Prijava za stjecanje dobara i primljene usluge iz drugih država članica Europske unije", "ObrazacPDVS-v1-0");
  }

}
