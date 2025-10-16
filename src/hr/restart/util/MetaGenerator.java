package hr.restart.util;

import hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.*;
import hr.porezna_uprava.e_porezna.sheme.temeljnitipovi.v2_1.TFormat;
import hr.porezna_uprava.e_porezna.sheme.temeljnitipovi.v2_1.TJezik;
import hr.porezna_uprava.e_porezna.sheme.temeljnitipovi.v2_1.TTip;
import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacid.v3_0.Generator;
import hr.restart.pl.raObracunPL;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.borland.dx.sql.dataset.QueryDataSet;

public class MetaGenerator {
  private MetaGenerator() {
    
  };
  
  public static SPPO20132014Metapodaci generateSPPO20132014meta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPPO20132014ObjectFactory mf =
        new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPPO20132014ObjectFactory();
    SPPO20132014Metapodaci mpd = mf.createSPPO20132014Metapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static SPPOmetapodaci generateSPPOmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPPOObjectFactory mf =
        new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.SPPOObjectFactory();
    SPPOmetapodaci mpd = mf.createSPPOmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static SJOPPDmetapodaci generateSJOPPDmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.JOPPDObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.JOPPDObjectFactory();
    SJOPPDmetapodaci mpd = mf.createSJOPPDmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  
  public static SOPZmetapodaci generateSOPZmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.OPZObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.OPZObjectFactory();
    SOPZmetapodaci mpd = mf.createSOPZmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }

  public static SPDVKmetapodaci generateSPDVKmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.PDVKObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.PDVKObjectFactory();
    SPDVKmetapodaci mpd = mf.createSPDVKmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static SURAmetapodaci generateSURAmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.URAObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.URAObjectFactory();
    SURAmetapodaci mpd = mf.createSURAmetapodaci();
    
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    
    return mpd;
  }
  
  public static SPDVmetapodaci generateSPDVmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.PDVObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.PDVObjectFactory();
    SPDVmetapodaci mpd = mf.createSPDVmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static SPDVSmetapodaci generateSPDVSmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.PDVSObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.PDVSObjectFactory();
    SPDVSmetapodaci mpd = mf.createSPDVSmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static SZPmetapodaci generateSZPmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.ZPObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.ZPObjectFactory();
    SZPmetapodaci mpd = mf.createSZPmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static SIDmetapodaci generateSIDmeta(String autor, String sdat, String naslov, String usklad) throws Exception {
    hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.IDObjectFactory mf = new hr.porezna_uprava.e_porezna.sheme.metapodaci.v2_0.IDObjectFactory();
    SIDmetapodaci mpd = mf.createSIDmetapodaci();
    mpd.setAdresant(mf.createSAdresantTemeljni());
    mpd.getAdresant().setValue("Ministarstvo Financija, Porezna uprava, Zagreb");
    mpd.setAutor(mf.createSAutorTemeljni());
    mpd.getAutor().setValue(autor);
    mpd.getAutor().setDc(mpd.getAutor().getDc());
    mpd.setDatum(mf.createSDatumTemeljni());
    mpd.getDatum().setValue(createDatum(sdat, true));
    mpd.getDatum().setDc(mpd.getDatum().getDc());
    mpd.setIdentifikator(mf.createSIdentifikatorTemeljni());
    mpd.getIdentifikator().setValue(UUID.randomUUID().toString());
    mpd.getIdentifikator().setDc(mpd.getIdentifikator().getDc());
    mpd.setNaslov(mf.createSNaslovTemeljni());
    mpd.getNaslov().setValue(naslov);
    mpd.getNaslov().setDc(mpd.getNaslov().getDc());
    mpd.setUskladjenost(mf.createSUskladjenost());
    mpd.getUskladjenost().setValue(usklad);
    mpd.getUskladjenost().setDc(mpd.getUskladjenost().getDc());
    //temljni tipovi
    mpd.setFormat(mf.createSFormatTemeljni());
    mpd.getFormat().setValue(TFormat.TEXT_XML);
    mpd.getFormat().setDc(mpd.getFormat().getDc());
    mpd.setJezik(mf.createSJezikTemeljni());
    mpd.getJezik().setValue(TJezik.HR_HR);
    mpd.getJezik().setDc(mpd.getJezik().getDc());
    mpd.setTip(mf.createSTipTemeljni());
    mpd.getTip().setValue(TTip.ELEKTRONIČKI_OBRAZAC);
    mpd.getTip().setDc(mpd.getTip().getDc());
    return mpd;
  }
  
  public static XMLGregorianCalendar createDatum(String sdat, boolean time) throws Exception {
    GregorianCalendar c = (GregorianCalendar)Calendar.getInstance();
    long datum; 
    try {
      datum = Long.parseLong(sdat);
    } catch (Exception e) {
      datum = System.currentTimeMillis();
    }
    c.setTimeInMillis(datum);
    XMLGregorianCalendar gd = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE), DatatypeConstants.FIELD_UNDEFINED);
    if (time) {
      gd.setHour(12);
      gd.setMinute(0);
      gd.setSecond(0);
    }
    return gd;
  }

  public static HashMap<String, Object> headerData(long frommillis, long tomillis) {
    return headerData(frommillis, tomillis, null);
  }
  
  public static HashMap<String, Object> headerData(long frommillis, long tomillis, String knjcorg) {
    if ("".equals(knjcorg)) return null;
    if (knjcorg == null) knjcorg = hr.restart.zapod.OrgStr.getKNJCORG();
    String knjst = "SELECT Logotipovi.nazivlog as naziv, Logotipovi.mjesto, Logotipovi.adresa, Logotipovi.pbr as hpbroj, Logotipovi.ziro, " +
        (raObracunPL.isOIB()?"Logotipovi.oib":"Logotipovi.matbroj") +" AS MATBROJ" +    
        ", Logotipovi.sifdjel, Logotipovi.email "+
                   "FROM Logotipovi "+
                   "WHERE logotipovi.corg ='" + knjcorg + "'";
    QueryDataSet knjigovodstvo = Aus.q(knjst);
    if (knjigovodstvo.rowCount() == 0) return null;
    knjigovodstvo.first();
    HashMap<String, Object> data = new HashMap<String, Object>();
    //zag (str 1)
//    rdz.getRow(0);
    String userOverride = frmParam.getParam("sk", 
        "e-sastavio"+knjcorg, "", 
        "Tko je sastavio obrasce za e-por (Ime;Prezime;Tel;Fax;e-mail) za knjig."+knjcorg);
    if (userOverride.equals("")) {
      data.put(Generator.AUTOR, raUser.getInstance().getImeUsera());
      StringTokenizer ip = new StringTokenizer((String)data.get(Generator.AUTOR)," ");
      if (ip.hasMoreTokens()) data.put(Generator.SASTAVIO_IME, ip.nextToken());
      if (ip.hasMoreTokens()) data.put(Generator.SASTAVIO_PREZIME, ip.nextToken());
      if (ip.hasMoreTokens()) data.put(Generator.SASTAVIO_TEL, ip.nextToken());
      if (ip.hasMoreTokens()) data.put(Generator.SASTAVIO_FAX, ip.nextToken());
      if (ip.hasMoreTokens()) data.put(Generator.SASTAVIO_EMAIL, ip.nextToken());
      //data.put(Generator.SASTAVIO_EMAIL, knjigovodstvo.getString("EMAIL"));
      data.put(Generator.PODNOSITELJ_EMAIL, knjigovodstvo.getString("EMAIL"));
      
    } else {
      StringTokenizer ip = new StringTokenizer(userOverride,";");
      String tmp = "";
      if (ip.hasMoreTokens()) {
        tmp = ip.nextToken();
        if (tmp.trim().length()>1) data.put(Generator.SASTAVIO_IME, tmp);
      }
      if (ip.hasMoreTokens()) {
        tmp = ip.nextToken();
        if (tmp.trim().length()>1) data.put(Generator.SASTAVIO_PREZIME, tmp);
      }
      if (ip.hasMoreTokens()) {
        tmp = ip.nextToken();
        if (tmp.trim().length()>1) data.put(Generator.SASTAVIO_TEL, tmp);
      }
      if (ip.hasMoreTokens()) {
        tmp = ip.nextToken();
        if (tmp.trim().length()>1) data.put(Generator.SASTAVIO_FAX, tmp);
      }
      if (ip.hasMoreTokens()) {
        tmp = ip.nextToken();
        if (tmp.trim().length()>1) data.put(Generator.SASTAVIO_EMAIL, tmp);
      }
    }
    data.put(Generator.DATUM, System.currentTimeMillis()+"");
//    data.put(Generator.IDENTIFIKATOR, rdz.getIdentifikator());
    String podrured_lgcy = frmParam.getParam("pl", "podrured");
    String isppu_lgcy = frmParam.getParam("pl", "isppu");
    data.put(Generator.PODRUCNI_URED, frmParam.getParam("pl", "podrured"+knjcorg, (podrured_lgcy==null)?"Zagreb":podrured_lgcy, "e-porezna: Naziv podrucnog ureda PU za knjig." + knjcorg));
    data.put(Generator.ISPOSTAVA, frmParam.getParam("pl", "isppu"+knjcorg, (isppu_lgcy==null)?"3402":isppu_lgcy, "e-porezna: Brojcana oznaka ispostave PU za knjig." + knjcorg));
    data.put(Generator.PODNOSITELJ_NAZIV, knjigovodstvo.getString("NAZIV"));
    data.put(Generator.PODNOSITELJ_OIB, knjigovodstvo.getString("MATBROJ"));
    data.put(Generator.PODNOSITELJ_EMAIL, knjigovodstvo.getString("EMAIL"));
    data.put(Generator.PODNOSITELJ_SIFDJEL, knjigovodstvo.getString("SIFDJEL"));
    
    StringTokenizer adrtok = new StringTokenizer(knjigovodstvo.getString("ADRESA"));
    String broj = "", ulica = "";
    int i=1, c=adrtok.countTokens();
    while (adrtok.hasMoreTokens()) {
      String t = adrtok.nextToken();
      if (i<c) ulica = ulica + t + " ";
      broj = t;
      i++;
    }
    data.put(Generator.PODNOSITELJ_ADRESA_BROJ, broj.trim());
    data.put(Generator.PODNOSITELJ_ADRESA_MJESTO, knjigovodstvo.getString("MJESTO"));
    data.put(Generator.PODNOSITELJ_ADRESA_ULICA, ulica.trim());
    data.put(Generator.PERIOD_OD, frommillis + "");
    data.put(Generator.PERIOD_DO, tomillis + "");
    return data;
  }
  
}
