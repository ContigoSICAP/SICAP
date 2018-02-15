package datapro.eibs.beans;

import java.math.BigDecimal;
import java.util.Hashtable;

import datapro.eibs.sockets.CharacterField;
import datapro.eibs.sockets.DecimalField;
import datapro.eibs.sockets.MessageField;
import datapro.eibs.sockets.MessageRecord;

/**
* Class generated by AS/400 CRTCLASS command from CRF003701 physical file definition.
* 
* File level identifier is 1090313115904.
* Record format level identifier is 378422B5DF4E0.
*/

public class CRF003701Message extends MessageRecord
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
                                     "E01FAP",
                                     "E01NCU",
                                     "E01CAP",
                                     "E01CPA",
                                     "E01IAP",
                                     "E01IPA",
                                     "E01MAP",
                                     "E01MPA",
                                     "E01FPA",
                                     "E01EST"
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
                                   "E01FAP",
                                   "E01NCU",
                                   "E01CAP",
                                   "E01CPA",
                                   "E01IAP",
                                   "E01IPA",
                                   "E01MAP",
                                   "E01MPA",
                                   "E01FPA",
                                   "E01EST"
                                 };
  final static String fid = "1090313115904";
  final static String rid = "378422B5DF4E0";
  final static String fmtname = "CRF003701";
  final int FIELDCOUNT = 20;
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
  private DecimalField fieldE01FAP = null;
  private DecimalField fieldE01NCU = null;
  private DecimalField fieldE01CAP = null;
  private DecimalField fieldE01CPA = null;
  private DecimalField fieldE01IAP = null;
  private DecimalField fieldE01IPA = null;
  private DecimalField fieldE01MAP = null;
  private DecimalField fieldE01MPA = null;
  private DecimalField fieldE01FPA = null;
  private CharacterField fieldE01EST = null;

  /**
  * Constructor for CRF003701Message.
  */
  public CRF003701Message()
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
    recordsize = 180;
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
    fields[10] = fieldE01FAP =
    new DecimalField(message, HEADERSIZE + 55, 9, 0, "E01FAP");
    fields[11] = fieldE01NCU =
    new DecimalField(message, HEADERSIZE + 64, 4, 0, "E01NCU");
    fields[12] = fieldE01CAP =
    new DecimalField(message, HEADERSIZE + 68, 17, 2, "E01CAP");
    fields[13] = fieldE01CPA =
    new DecimalField(message, HEADERSIZE + 85, 17, 2, "E01CPA");
    fields[14] = fieldE01IAP =
    new DecimalField(message, HEADERSIZE + 102, 17, 2, "E01IAP");
    fields[15] = fieldE01IPA =
    new DecimalField(message, HEADERSIZE + 119, 17, 2, "E01IPA");
    fields[16] = fieldE01MAP =
    new DecimalField(message, HEADERSIZE + 136, 17, 2, "E01MAP");
    fields[17] = fieldE01MPA =
    new DecimalField(message, HEADERSIZE + 153, 17, 2, "E01MPA");
    fields[18] = fieldE01FPA =
    new DecimalField(message, HEADERSIZE + 170, 9, 0, "E01FPA");
    fields[19] = fieldE01EST =
    new CharacterField(message, HEADERSIZE + 179, 1, "E01EST");

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
  * Set field E01FAP using a String value.
  */
  public void setE01FAP(String newvalue)
  {
    fieldE01FAP.setString(newvalue);
  }

  /**
  * Get value of field E01FAP as a String.
  */
  public String getE01FAP()
  {
    return fieldE01FAP.getString();
  }

  /**
  * Set numeric field E01FAP using a BigDecimal value.
  */
  public void setE01FAP(BigDecimal newvalue)
  {
    fieldE01FAP.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01FAP as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01FAP()
  {
    return fieldE01FAP.getBigDecimal();
  }

  /**
  * Set field E01NCU using a String value.
  */
  public void setE01NCU(String newvalue)
  {
    fieldE01NCU.setString(newvalue);
  }

  /**
  * Get value of field E01NCU as a String.
  */
  public String getE01NCU()
  {
    return fieldE01NCU.getString();
  }

  /**
  * Set numeric field E01NCU using a BigDecimal value.
  */
  public void setE01NCU(BigDecimal newvalue)
  {
    fieldE01NCU.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01NCU as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01NCU()
  {
    return fieldE01NCU.getBigDecimal();
  }

  /**
  * Set field E01CAP using a String value.
  */
  public void setE01CAP(String newvalue)
  {
    fieldE01CAP.setString(newvalue);
  }

  /**
  * Get value of field E01CAP as a String.
  */
  public String getE01CAP()
  {
    return fieldE01CAP.getString();
  }

  /**
  * Set numeric field E01CAP using a BigDecimal value.
  */
  public void setE01CAP(BigDecimal newvalue)
  {
    fieldE01CAP.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01CAP as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01CAP()
  {
    return fieldE01CAP.getBigDecimal();
  }

  /**
  * Set field E01CPA using a String value.
  */
  public void setE01CPA(String newvalue)
  {
    fieldE01CPA.setString(newvalue);
  }

  /**
  * Get value of field E01CPA as a String.
  */
  public String getE01CPA()
  {
    return fieldE01CPA.getString();
  }

  /**
  * Set numeric field E01CPA using a BigDecimal value.
  */
  public void setE01CPA(BigDecimal newvalue)
  {
    fieldE01CPA.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01CPA as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01CPA()
  {
    return fieldE01CPA.getBigDecimal();
  }

  /**
  * Set field E01IAP using a String value.
  */
  public void setE01IAP(String newvalue)
  {
    fieldE01IAP.setString(newvalue);
  }

  /**
  * Get value of field E01IAP as a String.
  */
  public String getE01IAP()
  {
    return fieldE01IAP.getString();
  }

  /**
  * Set numeric field E01IAP using a BigDecimal value.
  */
  public void setE01IAP(BigDecimal newvalue)
  {
    fieldE01IAP.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01IAP as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01IAP()
  {
    return fieldE01IAP.getBigDecimal();
  }

  /**
  * Set field E01IPA using a String value.
  */
  public void setE01IPA(String newvalue)
  {
    fieldE01IPA.setString(newvalue);
  }

  /**
  * Get value of field E01IPA as a String.
  */
  public String getE01IPA()
  {
    return fieldE01IPA.getString();
  }

  /**
  * Set numeric field E01IPA using a BigDecimal value.
  */
  public void setE01IPA(BigDecimal newvalue)
  {
    fieldE01IPA.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01IPA as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01IPA()
  {
    return fieldE01IPA.getBigDecimal();
  }

  /**
  * Set field E01MAP using a String value.
  */
  public void setE01MAP(String newvalue)
  {
    fieldE01MAP.setString(newvalue);
  }

  /**
  * Get value of field E01MAP as a String.
  */
  public String getE01MAP()
  {
    return fieldE01MAP.getString();
  }

  /**
  * Set numeric field E01MAP using a BigDecimal value.
  */
  public void setE01MAP(BigDecimal newvalue)
  {
    fieldE01MAP.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01MAP as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01MAP()
  {
    return fieldE01MAP.getBigDecimal();
  }

  /**
  * Set field E01MPA using a String value.
  */
  public void setE01MPA(String newvalue)
  {
    fieldE01MPA.setString(newvalue);
  }

  /**
  * Get value of field E01MPA as a String.
  */
  public String getE01MPA()
  {
    return fieldE01MPA.getString();
  }

  /**
  * Set numeric field E01MPA using a BigDecimal value.
  */
  public void setE01MPA(BigDecimal newvalue)
  {
    fieldE01MPA.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01MPA as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01MPA()
  {
    return fieldE01MPA.getBigDecimal();
  }

  /**
  * Set field E01FPA using a String value.
  */
  public void setE01FPA(String newvalue)
  {
    fieldE01FPA.setString(newvalue);
  }

  /**
  * Get value of field E01FPA as a String.
  */
  public String getE01FPA()
  {
    return fieldE01FPA.getString();
  }

  /**
  * Set numeric field E01FPA using a BigDecimal value.
  */
  public void setE01FPA(BigDecimal newvalue)
  {
    fieldE01FPA.setBigDecimal(newvalue);
  }

  /**
  * Return value of numeric field E01FPA as a BigDecimal.
  */
  public BigDecimal getBigDecimalE01FPA()
  {
    return fieldE01FPA.getBigDecimal();
  }

  /**
  * Set field E01EST using a String value.
  */
  public void setE01EST(String newvalue)
  {
    fieldE01EST.setString(newvalue);
  }

  /**
  * Get value of field E01EST as a String.
  */
  public String getE01EST()
  {
    return fieldE01EST.getString();
  }

}
