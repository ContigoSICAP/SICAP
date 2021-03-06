package datapro.eibs.beans;

import java.math.BigDecimal;
import java.util.Hashtable;

import datapro.eibs.sockets.CharacterField;
import datapro.eibs.sockets.DecimalField;
import datapro.eibs.sockets.MessageField;
import datapro.eibs.sockets.MessageRecord;

/**
* Class generated by AS/400 CRTCLASS command from CRF003801 physical file definition.
* 
* File level identifier is 1090604165403.
* Record format level identifier is 277232064202C.
*/

public class CRF003801Message extends MessageRecord
{
  final static String fldnames[] = {
                                     "H01USERID",
                                     "H01PROGRM",
                                     "H01TIMSYS",
                                     "H01SCRCOD",
                                     "H01OPECOD",
                                     "H01FLGMAS",
                                     "H01FLGWK1",
                                     "H01FLGWK2",
                                     "H01FLGWK3",
                                     "E01ACC",
                                     "E01CTA",
                                     "E01FEC",
                                     "E01DSC",
                                     "E01MON",
                                     "E01CDE",
                                     "E01SAL",
                                     "E01RDT",
                                     "E01BNK"
                                   };
  final static String tnames[] = {
                                   "H01USERID",
                                   "H01PROGRM",
                                   "H01TIMSYS",
                                   "H01SCRCOD",
                                   "H01OPECOD",
                                   "H01FLGMAS",
                                   "H01FLGWK1",
                                   "H01FLGWK2",
                                   "H01FLGWK3",
                                   "E01ACC",
                                   "E01CTA",
                                   "E01FEC",
                                   "E01DSC",
                                   "E01MON",
                                   "E01CDE",
                                   "E01SAL",
                                   "E01RDT",
                                   "E01BNK"
                                 };
  final static String fid = "1090604165403";
  final static String rid = "277232064202C";
  final static String fmtname = "CRF003801";
  final int FIELDCOUNT = 18;
  private static Hashtable tlookup = new Hashtable();
  private CharacterField fieldH01USERID = null;
  private CharacterField fieldH01PROGRM = null;
  private CharacterField fieldH01TIMSYS = null;
  private CharacterField fieldH01SCRCOD = null;
  private CharacterField fieldH01OPECOD = null;
  private CharacterField fieldH01FLGMAS = null;
  private CharacterField fieldH01FLGWK1 = null;
  private CharacterField fieldH01FLGWK2 = null;
  private CharacterField fieldH01FLGWK3 = null;
  private DecimalField fieldE01ACC = null;
  private DecimalField fieldE01CTA = null;
  private DecimalField fieldE01FEC = null;
  private CharacterField fieldE01DSC = null;
  private DecimalField fieldE01MON = null;
  private CharacterField fieldE01CDE = null;
  private DecimalField fieldE01SAL = null;
  private DecimalField fieldE01RDT = null;
  private CharacterField fieldE01BNK = null;

  /**
  * Constructor for CRF003801Message.
  */
  public CRF003801Message()
  {
    createFields();
    initialize();
  }

  /**
  * Create fields for this message.
  * This method implements the abstract method in the MessageRecord superclass.
  */
  protected void createFields()
  {
    recordsize = 159;
    fileid = fid;
    recordid = rid;
    message = new byte[getByteLength()];
    formatname = fmtname;
    fieldnames = fldnames;
    tagnames = tnames;
    fields = new MessageField[FIELDCOUNT];

    fields[0] = fieldH01USERID =
    new CharacterField(message, HEADERSIZE + 0, 10, "H01USERID");
    fields[1] = fieldH01PROGRM =
    new CharacterField(message, HEADERSIZE + 10, 10, "H01PROGRM");
    fields[2] = fieldH01TIMSYS =
    new CharacterField(message, HEADERSIZE + 20, 12, "H01TIMSYS");
    fields[3] = fieldH01SCRCOD =
    new CharacterField(message, HEADERSIZE + 32, 2, "H01SCRCOD");
    fields[4] = fieldH01OPECOD =
    new CharacterField(message, HEADERSIZE + 34, 4, "H01OPECOD");
    fields[5] = fieldH01FLGMAS =
    new CharacterField(message, HEADERSIZE + 38, 1, "H01FLGMAS");
    fields[6] = fieldH01FLGWK1 =
    new CharacterField(message, HEADERSIZE + 39, 1, "H01FLGWK1");
    fields[7] = fieldH01FLGWK2 =
    new CharacterField(message, HEADERSIZE + 40, 1, "H01FLGWK2");
    fields[8] = fieldH01FLGWK3 =
    new CharacterField(message, HEADERSIZE + 41, 1, "H01FLGWK3");
    fields[9] = fieldE01ACC =
    new DecimalField(message, HEADERSIZE + 42, 13, 0, "E01ACC");
    fields[10] = fieldE01CTA =
    new DecimalField(message, HEADERSIZE + 55, 13, 0, "E01CTA");
    fields[11] = fieldE01FEC =
    new DecimalField(message, HEADERSIZE + 68, 9, 0, "E01FEC");
    fields[12] = fieldE01DSC =
    new CharacterField(message, HEADERSIZE + 77, 30, "E01DSC");
    fields[13] = fieldE01MON =
    new DecimalField(message, HEADERSIZE + 107, 17, 2, "E01MON");
    fields[14] = fieldE01CDE =
    new CharacterField(message, HEADERSIZE + 124, 5, "E01CDE");
    fields[15] = fieldE01SAL =
    new DecimalField(message, HEADERSIZE + 129, 17, 2, "E01SAL");
    fields[16] = fieldE01RDT =
    new DecimalField(message, HEADERSIZE + 146, 9, 0, "E01RDT");
    fields[17] = fieldE01BNK =
    new CharacterField(message, HEADERSIZE + 155, 4, "E01BNK");

    synchronized (tlookup)
    {
      if (tlookup.isEmpty())
      {
        for (int i = 0; i < tnames.length; i++)
          tlookup.put(tnames[i], new Integer(i));
      }
    }

    taglookup = tlookup;
  }

  /**
  * Set field H01USERID using a String value.
  */
  public void setH01USERID(String newvalue)
  {
    fieldH01USERID.setString(newvalue);
  }

  /**
  * Get value of field H01USERID as a String.
  */
  public String getH01USERID()
  {
    return fieldH01USERID.getString();
  }

  /**
  * Set field H01PROGRM using a String value.
  */
  public void setH01PROGRM(String newvalue)
  {
    fieldH01PROGRM.setString(newvalue);
  }

  /**
  * Get value of field H01PROGRM as a String.
  */
  public String getH01PROGRM()
  {
    return fieldH01PROGRM.getString();
  }

  /**
  * Set field H01TIMSYS using a String value.
  */
  public void setH01TIMSYS(String newvalue)
  {
    fieldH01TIMSYS.setString(newvalue);
  }

  /**
  * Get value of field H01TIMSYS as a String.
  */
  public String getH01TIMSYS()
  {
    return fieldH01TIMSYS.getString();
  }

  /**
  * Set field H01SCRCOD using a String value.
  */
  public void setH01SCRCOD(String newvalue)
  {
    fieldH01SCRCOD.setString(newvalue);
  }

  /**
  * Get value of field H01SCRCOD as a String.
  */
  public String getH01SCRCOD()
  {
    return fieldH01SCRCOD.getString();
  }

  /**
  * Set field H01OPECOD using a String value.
  */
  public void setH01OPECOD(String newvalue)
  {
    fieldH01OPECOD.setString(newvalue);
  }

  /**
  * Get value of field H01OPECOD as a String.
  */
  public String getH01OPECOD()
  {
    return fieldH01OPECOD.getString();
  }

  /**
  * Set field H01FLGMAS using a String value.
  */
  public void setH01FLGMAS(String newvalue)
  {
    fieldH01FLGMAS.setString(newvalue);
  }

  /**
  * Get value of field H01FLGMAS as a String.
  */
  public String getH01FLGMAS()
  {
    return fieldH01FLGMAS.getString();
  }

  /**
  * Set field H01FLGWK1 using a String value.
  */
  public void setH01FLGWK1(String newvalue)
  {
    fieldH01FLGWK1.setString(newvalue);
  }

  /**
  * Get value of field H01FLGWK1 as a String.
  */
  public String getH01FLGWK1()
  {
    return fieldH01FLGWK1.getString();
  }

  /**
  * Set field H01FLGWK2 using a String value.
  */
  public void setH01FLGWK2(String newvalue)
  {
    fieldH01FLGWK2.setString(newvalue);
  }

  /**
  * Get value of field H01FLGWK2 as a String.
  */
  public String getH01FLGWK2()
  {
    return fieldH01FLGWK2.getString();
  }

  /**
  * Set field H01FLGWK3 using a String value.
  */
  public void setH01FLGWK3(String newvalue)
  {
    fieldH01FLGWK3.setString(newvalue);
  }

  /**
  * Get value of field H01FLGWK3 as a String.
  */
  public String getH01FLGWK3()
  {
    return fieldH01FLGWK3.getString();
  }

  /**
  * Set field E01ACC using a String value.
  */
  public void setE01ACC(String newvalue)
  {
    fieldE01ACC.setString(newvalue);
  }

  /**
  * Get value of field E01ACC as a String.
  */
  public String getE01ACC()
  {
    return fieldE01ACC.getString();
  }

  /**
  * Set numeric field E01ACC using a BigDecimal value.
  */
  public void setE01ACC(BigDecimal newvalue)
  {
    fieldE01ACC.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01ACC as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01ACC()
  {
    return fieldE01ACC.getBigDecimal();
  }

  /**
  * Set field E01CTA using a String value.
  */
  public void setE01CTA(String newvalue)
  {
    fieldE01CTA.setString(newvalue);
  }

  /**
  * Get value of field E01CTA as a String.
  */
  public String getE01CTA()
  {
    return fieldE01CTA.getString();
  }

  /**
  * Set numeric field E01CTA using a BigDecimal value.
  */
  public void setE01CTA(BigDecimal newvalue)
  {
    fieldE01CTA.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01CTA as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01CTA()
  {
    return fieldE01CTA.getBigDecimal();
  }

  /**
  * Set field E01FEC using a String value.
  */
  public void setE01FEC(String newvalue)
  {
    fieldE01FEC.setString(newvalue);
  }

  /**
  * Get value of field E01FEC as a String.
  */
  public String getE01FEC()
  {
    return fieldE01FEC.getString();
  }

  /**
  * Set numeric field E01FEC using a BigDecimal value.
  */
  public void setE01FEC(BigDecimal newvalue)
  {
    fieldE01FEC.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01FEC as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01FEC()
  {
    return fieldE01FEC.getBigDecimal();
  }

  /**
  * Set field E01DSC using a String value.
  */
  public void setE01DSC(String newvalue)
  {
    fieldE01DSC.setString(newvalue);
  }

  /**
  * Get value of field E01DSC as a String.
  */
  public String getE01DSC()
  {
    return fieldE01DSC.getString();
  }

  /**
  * Set field E01MON using a String value.
  */
  public void setE01MON(String newvalue)
  {
    fieldE01MON.setString(newvalue);
  }

  /**
  * Get value of field E01MON as a String.
  */
  public String getE01MON()
  {
    return fieldE01MON.getString();
  }

  /**
  * Set numeric field E01MON using a BigDecimal value.
  */
  public void setE01MON(BigDecimal newvalue)
  {
    fieldE01MON.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01MON as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01MON()
  {
    return fieldE01MON.getBigDecimal();
  }

  /**
  * Set field E01CDE using a String value.
  */
  public void setE01CDE(String newvalue)
  {
    fieldE01CDE.setString(newvalue);
  }

  /**
  * Get value of field E01CDE as a String.
  */
  public String getE01CDE()
  {
    return fieldE01CDE.getString();
  }

  /**
  * Set field E01SAL using a String value.
  */
  public void setE01SAL(String newvalue)
  {
    fieldE01SAL.setString(newvalue);
  }

  /**
  * Get value of field E01SAL as a String.
  */
  public String getE01SAL()
  {
    return fieldE01SAL.getString();
  }

  /**
  * Set numeric field E01SAL using a BigDecimal value.
  */
  public void setE01SAL(BigDecimal newvalue)
  {
    fieldE01SAL.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01SAL as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01SAL()
  {
    return fieldE01SAL.getBigDecimal();
  }

  /**
  * Set field E01RDT using a String value.
  */
  public void setE01RDT(String newvalue)
  {
    fieldE01RDT.setString(newvalue);
  }

  /**
  * Get value of field E01RDT as a String.
  */
  public String getE01RDT()
  {
    return fieldE01RDT.getString();
  }

  /**
  * Set numeric field E01RDT using a BigDecimal value.
  */
  public void setE01RDT(BigDecimal newvalue)
  {
    fieldE01RDT.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01RDT as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01RDT()
  {
    return fieldE01RDT.getBigDecimal();
  }

  /**
  * Set field E01BNK using a String value.
  */
  public void setE01BNK(String newvalue)
  {
    fieldE01BNK.setString(newvalue);
  }

  /**
  * Get value of field E01BNK as a String.
  */
  public String getE01BNK()
  {
    return fieldE01BNK.getString();
  }

}
