package edu.wpi.cs3733.D22.teamZ.nfcCardReader;

import edu.wpi.cs3733.D22.teamZ.nfcCardReader.util.ACR122Util;
import java.nio.charset.StandardCharsets;
import javax.smartcardio.CardException;

public class NFCCardReaderController {
  private String username;
  private String password;
  private String uid;
  private ACR122UReaderHelper reader;
  private ACR122Util readerUtil;

  public NFCCardReaderController() {}

  public void initialize() throws CardException {
    reader = ACR122UReaderHelper.getInstance();
    readerUtil = ACR122Util.getInstance();
    reader.connectReader();
    reader.connectCard(null);
  }

  public void setUID() {
    byte[] byteUID = reader.readCardUsingDefaultKey(0);
    String tempUID = new String(byteUID, StandardCharsets.US_ASCII);
    tempUID = tempUID.substring(0, tempUID.length() - 2);
    uid = tempUID.replaceAll("\u0000", "");
  }

  public void setUsername() {
    byte[] byteUsername = reader.readCardUsingDefaultKey(1);
    String tempUsername = new String(byteUsername, StandardCharsets.US_ASCII);
    tempUsername = tempUsername.substring(0, tempUsername.length() - 2);
    username = tempUsername.replaceAll("\u0000", "");
  }

  public void setPassword() {
    byte[] bytePassword = reader.readCardUsingDefaultKey(2);
    String tempPassword = new String(bytePassword, StandardCharsets.US_ASCII);
    tempPassword = tempPassword.substring(0, tempPassword.length() - 2);
    password = tempPassword.replaceAll("\u0000", "");
  }

  public String getUid() {
    return uid;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
