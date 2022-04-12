package edu.wpi.cs3733.D22.teamZ.nfcCardReader;

import edu.wpi.cs3733.D22.teamZ.nfcCardReader.util.ACR122Util;

import javax.smartcardio.CardException;
import java.io.UnsupportedEncodingException;

public class nfcCardReaderController {
    private String username;
    private String password;
    private String UID;
    private static ACR122UReaderHelper reader;
    private static ACR122Util readerUtil;

    public nfcCardReaderController(){

    }

    public static void initialize() throws CardException {
        reader = ACR122UReaderHelper.getInstance();
        readerUtil = ACR122Util.getInstance();
        reader.connectReader();
    }

    public void setUID() throws UnsupportedEncodingException {
        byte[] byteUID = reader.readCardUsingDefaultKey(1);
        UID = new String(byteUID, "US-ASCII");
    }

    public void setUsername() throws UnsupportedEncodingException {
        byte[] byteUsername = reader.readCardUsingDefaultKey(1);
        username = new String(byteUsername, "US-ASCII");
    }

    public void setPassword(String password) throws UnsupportedEncodingException{
        byte[] bytePassword = reader.readCardUsingDefaultKey(2);
        password = new String(bytePassword, "US-ASCII");
    }
}
