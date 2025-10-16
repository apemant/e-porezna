package hr.restart.pl;

import java.sql.Timestamp;

import hr.restart.util.VarStr;

public class repSepaDiskNonBatch extends repSepaDisk {

  public repSepaDiskNonBatch() {
    VarStr dat = new VarStr(new Timestamp(vl.getNowMS()).toString());
    dat.truncate(10).remove('-');
    this.setPrint("UN." + dat + ".0002.300.xml");
    onlynonbatch = true;
  }
}
