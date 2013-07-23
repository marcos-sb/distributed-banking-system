package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.DataMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class DataMessageConversor extends MessageConversor {

	protected void fill(DataMessage from, FAPMessage to, MessageCode messageCode) {
		super.fill(from, to, messageCode);
		to.appendNumericField(new Long(from.getChannelNumber()), 2);
		to.appendNumericField(new Long(from.getMessageNumber()), 5);
		to.appendNumericField(new Long(from.isBankOnline() ? 1 : 0), 1);
	}
	
	protected void fill(FAPMessage from, DataMessage to) throws UnsupportedEncodingException {
		super.fill(from, to);
		to.setChannelNumber(Byte.parseByte(from.extractNumericField(18, 2)));
		to.setMessageNumber(Short.parseShort(from.extractNumericField(20, 5)));
		
		to.setBankOnline(
				Integer.parseInt(
						from.extractNumericField(25, 1)) == 1 ? true : false);
	}
}
