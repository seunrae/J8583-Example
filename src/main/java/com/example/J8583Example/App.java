package com.example.J8583Example;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

import java.io.IOException;
import java.text.ParseException;


public class App {
    //make header a constant
    private static final String HEADER = "ISO1987";

    public static void main(String[] args) throws IOException, ParseException {
        createIsoMessage();
    }
    private static void createIsoMessage() throws IOException, ParseException {
        MessageFactory<IsoMessage> messageFactory = ConfigParser.createDefault();

        IsoMessage isoMessage = messageFactory.newMessage(0x200);

        isoMessage.setIsoHeader(HEADER);
        isoMessage.setValue(2, "1234567890123456", IsoType.LLVAR, 19);
        isoMessage.setValue(3, "344321", IsoType.NUMERIC, 6);
        isoMessage.setValue(4, "50.00", IsoType.AMOUNT, 12);
        isoMessage.setValue(7,"0926221800",IsoType.NUMERIC, 10);
        isoMessage.setValue(11, "123456", IsoType.NUMERIC, 6);
        isoMessage.setValue(12, "180000", IsoType.NUMERIC, 12);
        isoMessage.setValue(13, "0926", IsoType.NUMERIC, 4);
        isoMessage.setValue(32, "456789", IsoType.LLVAR, 11);
        isoMessage.setValue(37, "123456", IsoType.ALPHA, 12);
        isoMessage.setValue(41, "12345678", IsoType.ALPHA, 8);
        isoMessage.setValue(42, "123456789012345", IsoType.ALPHA, 15);
        isoMessage.setValue(46, "0100", IsoType.ALPHA, 999);

        System.out.println("The Iso message is : \n==================\n " + new String(isoMessage.writeData()));
        readIsoMessage(isoMessage.writeData());
    }
    private static void readIsoMessage(byte[] messageStream) throws IOException, ParseException {
        MessageFactory<IsoMessage> messageFactory = ConfigParser.createFromClasspathConfig("fields.xml");

        IsoMessage receivedMessage = messageFactory.parseMessage(messageStream, HEADER.length());

        System.out.println("Received Iso message:\n ===================\n" + new String(receivedMessage.writeData()));

        System.out.println("Header: " + receivedMessage.getIsoHeader());
        System.out.println("Data Elements ----------");
        printIsoField(receivedMessage, 2);
        printIsoField(receivedMessage, 3);
        printIsoField(receivedMessage, 4);
        printIsoField(receivedMessage, 7);

    }
    private static void printIsoField(IsoMessage isoMessage, int fieldNumber){
        IsoValue<Object> isoValue = isoMessage.getField(fieldNumber);
        System.out.println(fieldNumber + " : "+ isoValue.getType() + " : " + isoValue.getLength() + " : " + isoValue.getValue());
    }
}
