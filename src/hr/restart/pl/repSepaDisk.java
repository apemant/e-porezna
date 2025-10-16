package hr.restart.pl;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.frmVirmani;
import hr.restart.zapod.repDisk;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.CreditTransferTransaction34;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.DateAndDateTime2Choice;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.Document;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.DocumentType3Code;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.ObjectFactory;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.PaymentInstruction30;
import iso.std.iso._20022.tech.xsd.scthr.pain_001_001.PaymentMethod3Code;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class repSepaDisk extends repDisk {  
  QueryDataSet qds;
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  JAXBContext context;
  ObjectFactory factory;
  
  boolean onlybatch, onlynonbatch;
  String nalog;
  
  public repSepaDisk() {
    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      this.getPrinter().setNewline(System.getProperty("line.separator"));
      VarStr dat = new VarStr(new Timestamp(vl.getNowMS()).toString());
      dat.truncate(10).remove('-');
      
      nalog = frmParam.getParam("pl", "sepaNum", "0001", "Redni broj SEPA naloga u danu (npr. 0001)", true);
      
      this.setPrint("UN." + dat + "."+nalog+".300.xml");
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
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private void initialize() throws Exception {
    context = JAXBContext.newInstance(Document.class);
    factory = new ObjectFactory();
  }
  
  private void marshall() throws Exception {
    Marshaller m = context.createMarshaller();
    m.setProperty("jaxb.formatted.output", Boolean.TRUE);
    m.marshal(factory.createDocument(sepa), new FileOutputStream(mxReport.TMPPRINTFILE));
  }
  
  Document sepa;
  private void generate() throws Exception {
    qds = frmVirmani.getInstance().getRaQueryDataSet();
    qds.open();
    qds.getColumn("BRRACNT").setRowId(true);
    qds.setSort(new SortDescriptor(new String[] { "BRRACNT" }));
    qds.first();
    
    VarStr dat = new VarStr(vl.getToday().toString());
    dat.truncate(10).remove('-');
    if (onlybatch) dat.append("-1"); 
    if (onlynonbatch) dat.append("-2"); 
    ld.raLocate((DataSet)dm.getLogotipovi(), "CORG", qds.getString("KNJIG"));
    String naziv = dm.getLogotipovi().getString("NAZIVLOG");
    String adr = dm.getLogotipovi().getString("ADRESA").trim();
    String mj = dm.getLogotipovi().getString("MJESTO");
    String pbr = Integer.toString(dm.getLogotipovi().getInt("PBR"));
    String oib = dm.getLogotipovi().getString("OIB");
    int ls = adr.lastIndexOf(' ');
    String street = adr.substring(0, ls);
    String kbr = adr.substring(ls + 1);
    
    System.out.println("DBTR adr: " + street + "|" + kbr + "|" + pbr + "|" + mj);
    
    sepa = factory.createDocument();
    sepa.setCstmrCdtTrfInitn(factory.createCustomerCreditTransferInitiationV09());
    sepa.getCstmrCdtTrfInitn().setGrpHdr(factory.createGroupHeader85());
    sepa.getCstmrCdtTrfInitn().getGrpHdr().setMsgId("UN" + dat + nalog);
    sepa.getCstmrCdtTrfInitn().getGrpHdr().setCreDtTm(createTimestamp(new Timestamp(vl.getNowMS())));
    sepa.getCstmrCdtTrfInitn().getGrpHdr().setInitgPty(factory.createPartyIdentification1352());
    sepa.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().setNm(naziv);
    
    PaymentInstruction30 pi, hnb = null;
    HashMap<Object, Object> kreds = new HashMap<Object, Object>();
    HashMap<Object, Object> netos = new HashMap<Object, Object>();
    
    int tx = 0;
    BigDecimal total = Aus.zero2;
    for (qds.first(); qds.inBounds(); qds.next()) {
      String val = (vl.findYear(qds.getTimestamp("DATUMIZV")).compareTo("2022") <= 0) ? "HRK" : "EUR";
      String ibanpl = frmVirmani.getIBAN_HR(qds.getString("BRRACNT"), false);
      String ibanpr = frmVirmani.getIBAN_HR(qds.getString("BRRACUK"), false);
      String prefix = ibanpr.substring(4, 11);
      boolean dr = prefix.equals("1001005");
      String oibpr = qds.getString("RID").trim();
      boolean kred = (oibpr.length() > 0);
      
      if (dr) pi = hnb;
      else if (!kred) pi = (PaymentInstruction30)netos.get(prefix);
      else pi = (PaymentInstruction30)kreds.get(oibpr);
      
      if (pi == null) {
        pi = factory.createPaymentInstruction30();
        String num = String.valueOf(tx);
        pi.setPmtInfId(dat + nalog + ((num.length() < 3) ? Aus.string(3 - num.length(), '0') : "") + num);
        pi.setPmtMtd(PaymentMethod3Code.TRF);
        if (!dr && !kred && ld.raLocate((DataSet)dm.getBankepl(), "PREFIKS", prefix) && dm.getBankepl().getString("BATCHB").equalsIgnoreCase("D")) {
          pi.setBtchBookg(Boolean.TRUE);
        } else {
          pi.setBtchBookg(Boolean.FALSE);
        } 
        pi.setNbOfTxs("1");
        pi.setCtrlSum(qds.getBigDecimal("IZNOS"));
        pi.setReqdExctnDt(createDatum(qds.getTimestamp("DATUMIZV")));
        pi.setDbtr(factory.createPartyIdentification1353());
        pi.getDbtr().setNm(naziv);
        pi.getDbtr().setPstlAdr(factory.createPostalAddress24());
        pi.getDbtr().getPstlAdr().setCtry("HR");
        pi.getDbtr().getPstlAdr().setTwnNm(mj);
        pi.getDbtr().getPstlAdr().setStrtNm(street);
        pi.getDbtr().getPstlAdr().setBldgNb(kbr);
        pi.getDbtr().getPstlAdr().setPstCd(pbr);
        pi.getDbtr().setId(factory.createParty38Choice1());
        pi.getDbtr().getId().setOrgId(factory.createOrganisationIdentification291());
        pi.getDbtr().getId().getOrgId().setOthr(factory.createGenericOrganisationIdentification1());
        pi.getDbtr().getId().getOrgId().getOthr().setId(oib);
        pi.setDbtrAcct(factory.createCashAccount38Dbtr());
        pi.getDbtrAcct().setCcy(val);
        pi.getDbtrAcct().setId(factory.createAccountIdentification4Choice());
        pi.getDbtrAcct().getId().setIBAN(frmVirmani.getIBAN_HR(qds.getString("BRRACNT"), false));
        if (!dr) {
          pi.setUltmtDbtr(factory.createPartyIdentification1354());
          pi.getUltmtDbtr().setId(factory.createParty38Choice1());
          if (!kred) {
            pi.getUltmtDbtr().getId().setOrgId(factory.createOrganisationIdentification291());
            pi.getUltmtDbtr().getId().getOrgId().setOthr(factory.createGenericOrganisationIdentification1());
            pi.getUltmtDbtr().getId().getOrgId().getOthr().setId(oib);
          } else {
            pi.getUltmtDbtr().getId().setPrvtId(factory.createPersonIdentification13());
            pi.getUltmtDbtr().getId().getPrvtId().setOthr(factory.createGenericPersonIdentification1());
            pi.getUltmtDbtr().getId().getPrvtId().getOthr().setId(oibpr);
          } 
          pi.setPmtTpInf(factory.createPaymentTypeInformation26PmtInf());
          pi.getPmtTpInf().setCtgyPurp(factory.createCategoryPurpose1Choice());
          pi.getPmtTpInf().getCtgyPurp().setCd("SALA");
        } 
        pi.setDbtrAgt(factory.createBranchAndFinancialInstitutionIdentification6DbtrAgt());
        pi.getDbtrAgt().setFinInstnId(factory.createFinancialInstitutionIdentification18DbtrAgt());
        pi.getDbtrAgt().getFinInstnId().setBICFI((String)sw.get(ibanpl.substring(4, 11)));
        if (include(pi)) sepa.getCstmrCdtTrfInitn().getPmtInf().add(pi); 
        if (dr) hnb = pi;
        else if (!kred) netos.put(prefix, pi);
        else kreds.put(oibpr, pi); 
      } else {
        pi.setNbOfTxs(Integer.toString(Aus.getNumber(pi.getNbOfTxs()) + 1));
        pi.setCtrlSum(pi.getCtrlSum().add(qds.getBigDecimal("IZNOS")));
      } 
      if (!include(pi)) continue;
      
	    tx++;
	    total = total.add(qds.getBigDecimal("IZNOS"));
	    CreditTransferTransaction34 cti = factory.createCreditTransferTransaction34();
	    cti.setAmt(factory.createAmountType4Choice());
	    cti.getAmt().setInstdAmt(factory.createActiveOrHistoricCurrencyAndAmount());
	    cti.getAmt().getInstdAmt().setCcy(val);
	    cti.getAmt().getInstdAmt().setValue(qds.getBigDecimal("IZNOS"));
	    cti.setCdtr(factory.createPartyIdentification1351());
	    String cred = qds.getString("UKORIST");
	    int nl = cred.indexOf("\n");
	    cti.getCdtr().setPstlAdr(factory.createPostalAddress24());
	    cti.getCdtr().getPstlAdr().setCtry("HR");
	    cti.getCdtr().getPstlAdr().setTwnNm("Zagreb");
	    if (nl > 0) {
	      String padr = cred.substring(nl + 1).trim();
	      int cp = padr.indexOf(',');
	      if (cp > 0) {
	        String pbrmj = padr.substring(cp + 1).trim();
	        padr = padr.substring(0, cp);
	        int pls = padr.lastIndexOf(' ');
	        int mls = pbrmj.indexOf(' ');
	        if (pls > 0 && mls > 0) {
	          cti.getCdtr().getPstlAdr().setStrtNm(padr.substring(0, pls));
	          cti.getCdtr().getPstlAdr().setBldgNb(padr.substring(pls + 1));
	          cti.getCdtr().getPstlAdr().setPstCd(pbrmj.substring(0, mls));
	          cti.getCdtr().getPstlAdr().setTwnNm(pbrmj.substring(mls + 1));
	        } 
	      } else {
	        int pls = padr.indexOf(' ');
	        if (pls > 0 && padr.length() > 5 && Aus.isDigit(padr.substring(0, 5))) {
	          cti.getCdtr().getPstlAdr().setPstCd(padr.substring(0, pls));
	          cti.getCdtr().getPstlAdr().setTwnNm(padr.substring(pls).trim());
	        } else {
	          cti.getCdtr().getPstlAdr().setTwnNm(padr.trim());
	        } 
	      } 
	      cred = cred.substring(0, nl);
	    } 
	    cti.getCdtr().setNm(cred);
	    cti.setCdtrAcct(factory.createCashAccount38Cdtr());
	    cti.getCdtrAcct().setId(factory.createAccountIdentification4Choice2());
	    cti.getCdtrAcct().getId().setIBAN(ibanpr);
	    cti.setCdtrAgt(factory.createBranchAndFinancialInstitutionIdentification6CdtrAgt());
	    cti.getCdtrAgt().setFinInstnId(factory.createFinancialInstitutionIdentification18CdtrAgt());
	    cti.getCdtrAgt().getFinInstnId().setBICFI((String)sw.get(ibanpr.substring(4, 11)));
	    if (!dr) {
	      cti.setPurp(factory.createPurpose2Choice());
	      cti.getPurp().setCd("SALA");
	    } 
	    cti.setPmtId(factory.createPaymentIdentification6());
	    cti.getPmtId().setInstrId("nalog " + tx);
	    String mod = qds.getString("PNBZ1").trim();
	    String pnb = qds.getString("PNBZ2").trim();
	    if (mod.length() == 0 && pnb.length() == 0) {
	      cti.getPmtId().setEndToEndId("HR99");
	    } else if (mod.length() == 0) {
	      cti.getPmtId().setEndToEndId("HR00" + pnb);
	    } else if (mod.length() == 1) {
	      cti.getPmtId().setEndToEndId("HR0" + mod + pnb);
	    } else {
	      cti.getPmtId().setEndToEndId("HR" + mod + pnb);
	    } 
	    cti.setRmtInf(factory.createRemittanceInformation16());
	    cti.getRmtInf().setStrd(factory.createStructuredRemittanceInformation16());
	    cti.getRmtInf().getStrd().setAddtlRmtInf(qds.getString("SVRHA"));
	    cti.getRmtInf().getStrd().setCdtrRefInf(factory.createCreditorReferenceInformation2());
	    cti.getRmtInf().getStrd().getCdtrRefInf().setTp(factory.createCreditorReferenceType2());
	    cti.getRmtInf().getStrd().getCdtrRefInf().getTp().setCdOrPrtry(factory.createCreditorReferenceType1Choice());
	    cti.getRmtInf().getStrd().getCdtrRefInf().getTp().getCdOrPrtry().setCd(DocumentType3Code.SCOR);
	    mod = qds.getString("PNBO1").trim();
	    pnb = qds.getString("PNBO2").trim();
	    String ref = "";
	    if (mod.length() == 0 && pnb.length() == 0) {
	      ref = "HR99";
	    } else if (mod.length() == 0) {
	      ref = "HR00" + pnb;
	    } else if (mod.length() == 1) {
	      ref = "HR0" + mod + pnb;
	    } else {
	      ref = "HR" + mod + pnb;
	    } 
	    cti.getRmtInf().getStrd().getCdtrRefInf().setRef(ref);
	    pi.getCdtTrfTxInf().add(cti);
      
    } 
    sepa.getCstmrCdtTrfInitn().getGrpHdr().setNbOfTxs(String.valueOf(tx));
    sepa.getCstmrCdtTrfInitn().getGrpHdr().setCtrlSum(total);
  }
  
  private boolean include(PaymentInstruction30 pi) {
    if (onlybatch)
      return pi.isBtchBookg().booleanValue(); 
    if (onlynonbatch)
      return !pi.isBtchBookg().booleanValue(); 
    return true;
  }
  
  private DateAndDateTime2Choice createDatum(Timestamp dat) throws Exception {
    DateAndDateTime2Choice ret = factory.createDateAndDateTime2Choice();
    Calendar c = Calendar.getInstance();
    c.setTime(dat);
    XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
        c.get(1), c.get(2) + 1, c.get(5), -2147483648);
    ret.setDt(xc);
    return ret;
  }
  
  private XMLGregorianCalendar createTimestamp(Timestamp dat) throws Exception {
    Calendar c = Calendar.getInstance();
    c.setTime(dat);
    XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
        c.get(1), c.get(2) + 1, c.get(5), -2147483648);
    xc.setHour(c.get(10));
    xc.setMinute(c.get(12));
    xc.setSecond(c.get(13));
    xc.setMillisecond(c.get(14));
    System.out.println(xc);
    return xc;
  }
  
  String[][] swl = {
		    {"4133006", "SKOVHR22"},
		    {"4109006", "DALMHR22"},
		    {"2488001", "BFKKHR22"},
		    {"2485003", "CROAHR2X"},
		    {"2402006", "ESBCHR22"},
		    {"2493003", "HKBOHR2X"},
		    {"1001005", "NBHRHR2D"},
		    {"2390001", "HPBZHR2X"},
		    {"2500009", "HAABHR22"},
		    {"2492008", "IMXXHR22"},
		    {"2380006", "ISKBHR2X"},
		    {"2411006", "JADRHR2X"},
		    {"2400008", "KALCHR2X"},
		    {"4124003", "KENBHR22"},
		    {"2481000", "KREZHR2X"},
		    {"2407000", "OTPVHR2X"},
		    {"2408002", "PAZGHR2X"},
		    {"2386002", "PDKCHR2X"},
		    {"4132003", "SPRMHR22"},
		    {"2340009", "PBZGHR2X"},
		    {"2484008", "RZBHHR2X"},
		    {"2403009", "SMBRHR22"},
		    {"2503007", "VBCRHR22"},
		    {"2412009", "SBSLHR2X"},
		    {"2330003", "SOGEHR22"},
		    {"2483005", "STEDHR22"},
		    {"6717002", "ASBZHR22"},
		    {"2489004", "VBVZHR22"},
		    {"2381009", "CCBZHR2X"},
		    {"2360000", "ZABAHR2X"}
		  };
  HashMap sw = new HashMap();
  {
    for (int i = 0; i < swl.length; i++)
      sw.put(swl[i][0], swl[i][1]);
  }
}
