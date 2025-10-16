package hr.restart.pl;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.restart.robno.repMemo;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.repDisk;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

public class repIDDisk extends repDisk {

  public repIDDisk() {
    try {
//      separator = hr.restart.sisfun.frmParam.getParam("sisfun", "dumpSeparator");
//      if (separator == null) separator = "#";
//      separator = " "+separator;
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.setPrint("ID_e-porezna.xml");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void makeReport() {
    try {
      new Generator(makeData(), new FileOutputStream(mxReport.TMPPRINTFILE));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private HashMap<String, Object> makeData() {
    HashMap<String, Object> data = new HashMap<String, Object>();
    //zag (str 1)
    repID2003 rdz = new repID2003();
    rdz.getRow(0);
    data.put(Generator.AUTOR, raUser.getInstance().getImeUsera());
    data.put(Generator.DATUM, System.currentTimeMillis()+"");
    data.put(Generator.IDENTIFIKATOR, rdz.getIdentifikator());
    data.put(Generator.PODRUCNI_URED, frmParam.getParam("pl", "podrured", "Zagreb", "Naziv podrucnog ureda por. uprave za e-poreznu"));
    data.put(Generator.ISPOSTAVA, frmParam.getParam("pl", "isppu", "3402", "Brojcana oznaka ispostave por. uprave za e-poreznu"));
    data.put(Generator.PODNOSITELJ_NAZIV, rdz.getI_1());
    data.put(Generator.PODNOSITELJ_OIB, rdz.getI_3());
    data.put(Generator.PODNOSITELJ_ADRESA_BROJ, rdz.frID.getKnjHpBroj()+"");
    data.put(Generator.PODNOSITELJ_ADRESA_MJESTO, rdz.frID.getKnjMjesto());
    data.put(Generator.PODNOSITELJ_ADRESA_ULICA, rdz.frID.getKnjAdresa());
    
    Calendar c = Calendar.getInstance();
    c.set(Integer.parseInt(rdz.frID.getGodinaIzEkrana()), Integer.parseInt(rdz.frID.getMjesecIzEkrana())-1 , 1);
    data.put(Generator.PERIOD_OD, c.getTimeInMillis()+"");
    c.add(Calendar.MONTH, 1);
    c.add(Calendar.DATE, -1);
    data.put(Generator.PERIOD_DO, c.getTime().getTime() + "");
    
    data.put(Generator.DU_P110, rdz.getIII_1_1_b());
    data.put(Generator.DU_P120, rdz.getV_3_3());
    data.put(Generator.DU_P210, rdz.getIII_1_2_b());
    data.put(Generator.DU_P220, rdz.getV_3_4());
    data.put(Generator.DU_P310, rdz.getIII_1_3());
    data.put(Generator.DU_P320, rdz.getV_3_6());
    data.put(Generator.DU_P330, rdz.frID.getZZuInozemstvu());//rdz.getV_3_7()
    data.put(Generator.DU_P410, rdz.getIII_1_4());
    data.put(Generator.DU_P420, rdz.getZaZaposljavanjeInvaliditet());
    data.put(Generator.DU_P500, BigInteger.valueOf(rdz.getII_1()));
    data.put(Generator.OP_P100, rdz.getII_2());
    data.put(Generator.OP_P200, rdz.getNIII_1_III_3());
    data.put(Generator.OP_P210, rdz.getIII_1_1());
    data.put(Generator.OP_P220, rdz.getIII_1_2());
    data.put(Generator.OP_P230, rdz.getIII_1_3A());
    data.put(Generator.OP_P300, rdz.getIII_4());
    data.put(Generator.OP_P400, rdz.getIII_5());
    data.put(Generator.OP_P500, rdz.getIII_6());
    data.put(Generator.OP_P600, rdz.getIII_7());
    data.put(Generator.OP_P610, rdz.getIII_7_1());
    data.put(Generator.OP_P620, rdz.getIII_7_2());
    data.put(Generator.OP_P700, rdz.getIII_7());
    data.put(Generator.OP_P800, BigInteger.valueOf(rdz.getII_1()));
    
    //porezi (str B)
    repID_B rdb = new repID_B();
    TreeMap opcs = new TreeMap();
    BigDecimal ukpor = Aus.zero2;
    BigDecimal ukpri = Aus.zero2;
    BigDecimal ukuk = Aus.zero2;
    for (int i = 0; i < rdb.getRowCount(); i++) {
      rdb.getRow(i);
      BigDecimal por = new BigDecimal(rdb.getPOREZ()).setScale(2,BigDecimal.ROUND_HALF_UP);
      BigDecimal pri = new BigDecimal(rdb.getPRIREZ()).setScale(2,BigDecimal.ROUND_HALF_UP);
      BigDecimal uk = new BigDecimal(rdb.getUKUPNO()).setScale(2,BigDecimal.ROUND_HALF_UP);
//      opcs.put(rdb.getCOPCINE().substring(0, 3), new BigDecimal[] {por,pri,uk});
      String cop = rdb.ds.getString("COPCINE").trim();
      if (cop.length() == 1) cop = "00"+cop;
      if (cop.length() == 2) cop = "0"+cop;
      opcs.put(cop, new BigDecimal[] {por,pri,uk});
//      System.err.println("rdb.ds.getString(COPCINE) "+rdb.ds.getString("COPCINE"));
      ukpor = ukpor.add(por);
      ukpri = ukpri.add(pri);
      ukuk = ukuk.add(uk);
    }
    data.put(Generator.OBRACUNATI_POREZI, opcs);
    data.put(Generator.UKUPNO_POREZA, ukpor);
    data.put(Generator.UKUPNO_PRIREZA, ukpri);
    data.put(Generator.UKUPNO_UKUPNO, ukuk);
    
    return data;
  }
}
