package es.udc.fic.acs.infmsb01.atm.common.model.message.protocol;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public final class FAPMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6509574961438095175L;
	private ArrayList<Byte> codedFAPMessage;
	
	public FAPMessage(ArrayList<Byte> codedFAPMessage) {
		this.codedFAPMessage = codedFAPMessage;
	}
	
	public FAPMessage(byte [] codedFAPMessage) {
		this.codedFAPMessage = new ArrayList<Byte>();
		for(byte b : codedFAPMessage) {
			this.codedFAPMessage.add(b);
		}
	}
	
	public FAPMessage() {
		this(new ArrayList<Byte>());
	}
	
	public ArrayList<Byte> getCodedFAPMessage() {
		return codedFAPMessage;
	}
	
	public final byte getFAPMessageCode() {
		//TODO throw empty FAPMessage EX
		StringBuilder tempStr = new StringBuilder();
		tempStr.append(codedFAPMessage.get(16).byteValue() - 48);
		tempStr.append(codedFAPMessage.get(17).byteValue() - 48);
		
		return Byte.parseByte(tempStr.toString());
	}
	
	public final void appendIPPortField(String IP, int port) {
		StringBuilder tempStr = new StringBuilder();
		String [] tempStrArr = IP.split("[.]");
		
		for(String s : tempStrArr) {
			tempStr.append(String.format("%03d", Integer.parseInt(s)));
			tempStr.append('.');
		}
		tempStr.setCharAt(15, '/');
		tempStr.append(String.format("%04d", port));
		
		appendASCIIField(tempStr.toString(), 20);
	}
	
	public final void appendDateTimeField(Calendar date) {
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");
		String tempString;
		byte [] asciiTempString;
		
		tempString = sdfDateTime.format(date.getTime());
		
		try {
			asciiTempString = tempString.getBytes("US-ASCII");
		
			for(byte bc : asciiTempString) {
				codedFAPMessage.add(bc);
			}
		} catch (UnsupportedEncodingException ex) {}
		
	}
	
	public final void appendNumericField(
			Long value, int fieldLength) {
		
		String valueString = value.toString();
		int valueStringLength = valueString.length();
		String tempString;
		byte [] asciiTempString;
		
		if(valueStringLength < fieldLength) { //pad with zeros
			tempString = String.format("%0"+ fieldLength + "d", value);
		} else { //cut
			tempString = valueString.substring(valueStringLength - fieldLength);
		}
		
		try {
			asciiTempString = tempString.getBytes("US-ASCII");
		
			for(byte bc : asciiTempString) {
				//bc = (byte) (bc - 48); //refer to ascii table
				codedFAPMessage.add(bc);
			}
		} catch (UnsupportedEncodingException ex) {}
		
	}
	
	public final void appendNumericField(
			Double value, int fieldLength, int floatingLength) {
		
		double absValue = Math.abs(value);
		
		String tempStringFormatted = String.format(
				"%1." + floatingLength + "f", absValue);
		
		int tempStringFormattedLength = tempStringFormatted.length();
		String floatString = tempStringFormatted.substring(
				tempStringFormattedLength - floatingLength);
		
		String integerString = String.format("%.0f", absValue);
		int integerStringLength = integerString.length();
		int integerFieldLength = fieldLength - floatingLength;
		String finalIntegerString;
		byte [] asciiTempString;
		
		if(integerStringLength < integerFieldLength) { //pad with zeros (integer part)
			finalIntegerString = String.format("%0" + integerFieldLength + ".0f", absValue);
		} else { //cut
			finalIntegerString = integerString.substring(integerStringLength - fieldLength);
		}
		
		try {
			asciiTempString = (finalIntegerString + floatString).getBytes("US-ASCII");
		
			for(byte bc : asciiTempString) {
				//bc = (byte) (bc - 48); //refer to ascii table
				codedFAPMessage.add(bc);
			}
		} catch (UnsupportedEncodingException ex) {}
		
	}
	
	public final void appendASCIIField(
			String value, int fieldLength) {
		
		int valueLength = value.length();
		int dif = Math.abs(valueLength - fieldLength);
		String tempString;
		byte [] asciiTempString;
		
		if(valueLength < fieldLength) { //pad with white spaces
			StringBuilder tempStringBuilder = new StringBuilder(value);
			for(int i = 0; i<dif; i++) {
				tempStringBuilder.append(' ');
			}
			tempString = tempStringBuilder.toString();
		}
		else { //cut
			tempString = value.substring(dif);
		}
		
		try {
			asciiTempString = tempString.getBytes("US-ASCII");
			
			for(byte bc : asciiTempString) {
				codedFAPMessage.add(bc);
			}
		} catch(UnsupportedEncodingException ex) {}
		
	}
	
	public final String extractIPPort(int startingIndex) throws UnsupportedEncodingException {
		return extractASCIIField(startingIndex, 20);
	}
	
	public final Calendar extractDateTimeField(int startingIndex) throws UnsupportedEncodingException, ParseException {
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");
		int fieldLength = 18;
		byte [] tempArr = new byte [fieldLength];
		int endingIndex = startingIndex + fieldLength;
		int i;
		int j = 0;
		
		for(i = startingIndex; i<endingIndex; i++) {
			tempArr[j] = codedFAPMessage.get(i);
			j++;
		}
		
		Calendar tempCal = Calendar.getInstance();
		tempCal.setTime(
				sdfDateTime.parse(new String(tempArr, "US-ASCII")));
		
		return tempCal;
		
	}
	
	public final String extractNumericField(
			int startingIndex, int fieldLength, int floatingLength) {
		
		int integerLength = fieldLength - floatingLength;
		String integerString = extractNumericField(startingIndex, integerLength);
		String floatString = extractNumericField(startingIndex+integerLength, floatingLength);
		StringBuilder finalValueString =
				new StringBuilder(integerString).append('.').append(floatString);
		
		return finalValueString.toString();
	}
	
	public final String extractNumericField(
			int startingIndex, int fieldLength) {
		
		StringBuilder tempStr = new StringBuilder();
		int endingIndex = startingIndex + fieldLength;
		
		for(int i = startingIndex; i<endingIndex; i++) {
			tempStr.append(codedFAPMessage.get(i) - 48);
		}
		
		return tempStr.toString();
		
	}
	
	public final String extractASCIIField(
			int startingIndex, int fieldLength) throws UnsupportedEncodingException {
		
		byte [] tempArr = new byte [fieldLength];
		int endingIndex = startingIndex + fieldLength;
		int i;
		int j = 0;
		
		for(i = startingIndex; i<endingIndex; i++) {
			tempArr[j] = codedFAPMessage.get(i);
			j++;
		}
		
		return new String(tempArr, "US-ASCII");
		
	}
	
	@Override
	public String toString() {
		return codedFAPMessage.toString();
	}
	
	public final byte [] serialize() {
		int i = 0;
		byte [] temp = new byte [codedFAPMessage.size()];
		
		for(Byte b : codedFAPMessage) {
			temp[i] = b;
			i++;
		}
		return temp;
	}
	
//	public static void main(String [] args) {
//		
//		FAPMessage fapm = new FAPMessage();
//		fapm.appendIPPortField("127.0.0.1", 2002);
//		
//		String ipport = "";
//		try {
//			ipport = fapm.extractIPPort(0);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.printf("%s", ipport);
//
//	}
}
