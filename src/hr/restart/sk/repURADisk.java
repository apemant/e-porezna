package hr.restart.sk;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacura.v1_0.*;
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

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.StorageDataSet;

public class repURADisk extends repDisk {
  JAXBContext context;
  ObjectFactory factory;
  
  public repURADisk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("URA_e-porezna.xml");
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
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacura.v1_0");
    factory = new ObjectFactory();
  }
  
  private void marshall() throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
    m.marshal(factory.createObrazacURA(sobrazacura), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }

  SObrazacURA sobrazacura;
  private void generate() throws Exception {
    sobrazacura = factory.createSObrazacURA();
    sobrazacura.setMetapodaci(getMeta());
    sobrazacura.setTijelo(getTijelo());
    sobrazacura.setZaglavlje(getZaglavlje());
    sobrazacura.setVerzijaSheme("1.0");
  }

  private STijelo getTijelo() throws Exception {
    STijelo tijelo = factory.createSTijelo();
    SRacuniList racs = factory.createSRacuniList();
    
    StorageDataSet ds = frmPDV2.getInstance().getSetURA();
    String[] sumcols = {"IZNOS", "POR", "OSN5", "OSN13", "OSN25", "POR5", "POR5N", "POR13", "POR13N", "POR25", "POR25N"};
    DataRow sums = new DataRow(ds, sumcols);
    
    for (ds.first(); ds.inBounds(); ds.next()) {
      SRacun rac = factory.createSRacun();
      rac.setR1(ds.getInt("RBR"));
      rac.setR2(ds.getString("BROJDOK"));
      rac.setR3(MetaGenerator.createDatum(ds.getTimestamp("DATDOK").getTime()+"", true));
      rac.setR4(ds.getString("NAZPAR"));
      rac.setR5(ds.getString("ADR"));
      rac.setR6(ds.getInt("TIP"));
      rac.setR7(ds.getString("OIB"));
      rac.setR8(ds.getBigDecimal("OSN5"));
      rac.setR9(ds.getBigDecimal("OSN13"));
      rac.setR10(ds.getBigDecimal("OSN25"));
      rac.setR11(ds.getBigDecimal("IZNOS"));
      rac.setR12(ds.getBigDecimal("POR"));
      rac.setR13(ds.getBigDecimal("POR5"));
      rac.setR14(ds.getBigDecimal("POR5N"));
      rac.setR15(ds.getBigDecimal("POR13"));
      rac.setR16(ds.getBigDecimal("POR13N"));
      rac.setR17(ds.getBigDecimal("POR25"));
      rac.setR18(ds.getBigDecimal("POR25N"));
      
      for (int i = 0; i < sumcols.length; i++)
        sums.setBigDecimal(sumcols[i], sums.getBigDecimal(sumcols[i]).add(ds.getBigDecimal(sumcols[i])));
      
      racs.getR().add(rac);
    }
    
    tijelo.setRacuni(racs);
    
    SRacuniUkupno sru = factory.createSRacuniUkupno();
    sru.setU8(sums.getBigDecimal("OSN5"));
    sru.setU9(sums.getBigDecimal("OSN13"));
    sru.setU10(sums.getBigDecimal("OSN25"));
    sru.setU11(sums.getBigDecimal("IZNOS"));
    sru.setU12(sums.getBigDecimal("POR"));
    sru.setU13(sums.getBigDecimal("POR5"));
    sru.setU14(sums.getBigDecimal("POR5N"));
    sru.setU15(sums.getBigDecimal("POR13"));
    sru.setU16(sums.getBigDecimal("POR13N"));
    sru.setU17(sums.getBigDecimal("POR25"));
    sru.setU18(sums.getBigDecimal("POR25N"));
    
    tijelo.setUkupno(sru);
    
    return tijelo;
  }
  
  private SZaglavlje getZaglavlje() throws Exception {
    SZaglavlje zag = factory.createSZaglavlje();
    HashMap<String, Object> data = MetaGenerator.headerData(frmPDV2.getInstance().getDatumOd().getTime(), frmPDV2.getInstance().getDatumDo().getTime());
    
    SRazdoblje razdoblje = factory.createSRazdoblje();
    razdoblje.setDatumOd(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_OD), true));
    razdoblje.setDatumDo(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_DO), true));
    zag.setRazdoblje(razdoblje);
    
    SPorezniObveznik obveznik = factory.createSPorezniObveznik();
    SAdresa adresa = factory.createSAdresa();
    adresa.setUlica((String)data.get(Generator.PODNOSITELJ_ADRESA_ULICA));
    adresa.setMjesto((String)data.get(Generator.PODNOSITELJ_ADRESA_MJESTO));
    String broj = (String)data.get(Generator.PODNOSITELJ_ADRESA_BROJ);
    if (Aus.isDigit(broj))
      adresa.setBroj((String)data.get(Generator.PODNOSITELJ_ADRESA_BROJ));
    else {
      adresa.setBroj(String.valueOf(Aus.getAnyNumber(broj)));
      for (int i = 0; i < broj.length(); i++)
        if (!Character.isDigit(broj.charAt(i))) {
          adresa.setDodatakKucnomBroju(broj.substring(i));
          break;
        }
    }
    obveznik.setAdresa(adresa);
    obveznik.setNaziv((String)data.get(Generator.PODNOSITELJ_NAZIV));
    obveznik.setOIB((String)data.get(Generator.PODNOSITELJ_OIB));
    String sifdjel = (String)data.get(Generator.PODNOSITELJ_SIFDJEL);
    if (sifdjel != null && sifdjel.length() == 4)
      obveznik.setSifraDjelatnosti(sifdjel);
    else if (sifdjel != null && sifdjel.length() == 5) {
      obveznik.setPodrucjeDjelatnosti(sifdjel.substring(0, 1));
      obveznik.setSifraDjelatnosti(sifdjel.substring(1));
    }
    zag.setObveznik(obveznik);
    
    SIspunjavatelj sastavio = factory.createSIspunjavatelj();
    if (data.containsKey(Generator.SASTAVIO_IME)) sastavio.setIme((String)data.get(Generator.SASTAVIO_IME));
    if (data.containsKey(Generator.SASTAVIO_PREZIME)) sastavio.setPrezime((String)data.get(Generator.SASTAVIO_PREZIME));
    
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
    
    return zag;
  }
  

  private hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SURAmetapodaci getMeta() throws Exception {
    return MetaGenerator.generateSURAmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Knjiga primljenih (ulaznih) računa", "ObrazacURA-v1-0");
  }

}
