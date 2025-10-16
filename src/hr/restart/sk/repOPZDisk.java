package hr.restart.sk;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacopz.v1_0.*;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.raUser;
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

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;

public class repOPZDisk extends repDisk {
  JAXBContext context;
  ObjectFactory factory;
  
  public repOPZDisk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("OPZ_e-porezna.xml");
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
    m.marshal(factory.createObrazacOPZ(sobrazacopz), new FileOutputStream(mxReport.TMPPRINTFILE)); 
  }

  SObrazacOPZ sobrazacopz;
  private void generate() throws Exception {
    sobrazacopz = factory.createSObrazacOPZ();
    sobrazacopz.setMetapodaci(getMeta());
    sobrazacopz.setTijelo(getTijelo());
    sobrazacopz.setZaglavlje(getZaglavlje());
    sobrazacopz.setVerzijaSheme("1.0");
  }


  private SZaglavlje getZaglavlje() throws Exception {
    SZaglavlje zag = factory.createSZaglavlje();
    HashMap<String, Object> data = MetaGenerator.headerData(frmPDV2.getInstance().getDatumOd().getTime(), frmPDV2.getInstance().getDatumDo().getTime());
    
    SRazdoblje razdoblje = factory.createSRazdoblje();
    razdoblje.setDatumOd(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_OD), true));
    razdoblje.setDatumDo(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_DO), true));
    zag.setRazdoblje(razdoblje);
    
    zag.setNaDan(MetaGenerator.createDatum((String)data.get(Generator.PERIOD_DO), true));
    zag.setNisuNaplaceniDo(MetaGenerator.createDatum(frmPDV2.getInstance().getNextMonth().getTime()+"", true));
    
    SPorezniObveznik obveznik = factory.createSPorezniObveznik();
    SAdresa adresa = factory.createSAdresa();
    adresa.setUlica((String)data.get(Generator.PODNOSITELJ_ADRESA_ULICA));
    adresa.setMjesto((String)data.get(Generator.PODNOSITELJ_ADRESA_MJESTO));
    adresa.setBroj((String)data.get(Generator.PODNOSITELJ_ADRESA_BROJ));
    obveznik.setAdresa(adresa);
    obveznik.setNaziv((String)data.get(Generator.PODNOSITELJ_NAZIV));
    obveznik.setOIB((String)data.get(Generator.PODNOSITELJ_OIB));
    obveznik.setEmail((String)data.get(Generator.PODNOSITELJ_EMAIL));
    zag.setPorezniObveznik(obveznik);
    
    SIzvjesceSastavio sastavio = factory.createSIzvjesceSastavio();
    
    if (data.containsKey(Generator.SASTAVIO_IME)) sastavio.setIme((String)data.get(Generator.SASTAVIO_IME));
    if (data.containsKey(Generator.SASTAVIO_PREZIME)) sastavio.setPrezime((String)data.get(Generator.SASTAVIO_PREZIME));
    if (data.containsKey(Generator.SASTAVIO_TEL)) sastavio.setTelefon((String)data.get(Generator.SASTAVIO_TEL));
    if (data.containsKey(Generator.SASTAVIO_FAX)) sastavio.setFax((String)data.get(Generator.SASTAVIO_FAX));
    if (data.containsKey(Generator.SASTAVIO_EMAIL)) sastavio.setEmail((String)data.get(Generator.SASTAVIO_EMAIL));
    zag.setIzvjesceSastavio(sastavio);

    return zag;
  }


  private STijelo getTijelo() throws Exception {
    STijelo tijelo = factory.createSTijelo();
    tijelo.setKupci(factory.createSKupci());
    
    StorageDataSet ds = frmPDV2.getInstance().getSetOPZ();
    
    ds.setSort(new SortDescriptor(new String[] {"NAZPAR", "DATDOK"}));
    
    String oib = "";
    int rbr = 0, rbs = 0;
    SKupac kupac = null;
    
    DataRow sums = new DataRow(ds, new String[] {"SSALDO", "SALDO", "PDV"});
    sums.clearValues();
    
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (!ds.getString("OIB").equals(oib)) {
        oib = ds.getString("OIB");
        kupac = factory.createSKupac();
        tijelo.getKupci().getKupac().add(kupac);
        
        kupac.setK1(BigInteger.valueOf(++rbr));
        kupac.setK2(ds.getInt("TIP"));
        kupac.setK3(oib);
        kupac.setK4(ds.getString("NAZPAR"));
        kupac.setK5(Aus.zero2);
        kupac.setK6(Aus.zero2);
        kupac.setK7(Aus.zero2);
        kupac.setK8(Aus.zero2);
        kupac.setK9(Aus.zero2);
        rbs = 0;
        kupac.setRacuni(factory.createSRacuni());
      }
      SRacun racun = factory.createSRacun();
      kupac.getRacuni().getRacun().add(racun);
      
      racun.setR1(BigInteger.valueOf(++rbs));
      racun.setR2(ds.getString("BROJDOK"));
      racun.setR3(MetaGenerator.createDatum(ds.getTimestamp("DATDOK").getTime()+"", true));
      racun.setR4(MetaGenerator.createDatum(ds.getTimestamp("DATDOSP").getTime()+"", true));
      racun.setR5(BigInteger.valueOf(raDateUtil.getraDateUtil().DateDifference(ds.getTimestamp("DATDOSP"), frmPDV2.getInstance().getNextMonth())));
      racun.setR6(ds.getBigDecimal("SSALDO").subtract(ds.getBigDecimal("PDV")));
      racun.setR7(ds.getBigDecimal("PDV"));
      racun.setR8(ds.getBigDecimal("SSALDO"));
      racun.setR9(ds.getBigDecimal("SSALDO").subtract(ds.getBigDecimal("SALDO")));
      racun.setR10(ds.getBigDecimal("SALDO"));
      kupac.setK5(kupac.getK5().add(racun.getR6()));
      kupac.setK6(kupac.getK6().add(racun.getR7()));
      kupac.setK7(kupac.getK7().add(racun.getR8()));
      kupac.setK8(kupac.getK8().add(racun.getR9()));
      kupac.setK9(kupac.getK9().add(racun.getR10()));
      
      Aus.add(sums, "SSALDO", ds);
      Aus.add(sums, "SALDO", ds);
      Aus.add(sums, "PDV", ds);
    }
    
    tijelo.setUkupanIznosRacunaObrasca(sums.getBigDecimal("SSALDO").subtract(sums.getBigDecimal("PDV")));
    tijelo.setUkupanIznosPdvObrasca(sums.getBigDecimal("PDV"));
    tijelo.setUkupanIznosRacunaSPdvObrasca(sums.getBigDecimal("SSALDO"));
    tijelo.setUkupniPlaceniIznosRacunaObrasca(sums.getBigDecimal("SSALDO").subtract(sums.getBigDecimal("SALDO")));
    tijelo.setNeplaceniIznosRacunaObrasca(sums.getBigDecimal("SALDO"));
    tijelo.setOPZUkupanIznosPdv(Aus.zero2);
    tijelo.setOPZUkupanIznosRacunaSPdv(Aus.zero2);
    
    return tijelo;
  }


  private hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SOPZmetapodaci getMeta() throws Exception {
    return MetaGenerator.generateSOPZmeta(raUser.getInstance().getImeUsera(), System.currentTimeMillis()+"", 
        "Obrazac OPZ", "ObrazacOPZ-v1-0");
  }

  private void initialize() throws Exception {
    context = JAXBContext.newInstance("hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacopz.v1_0");
    factory = new ObjectFactory();
  }
}
