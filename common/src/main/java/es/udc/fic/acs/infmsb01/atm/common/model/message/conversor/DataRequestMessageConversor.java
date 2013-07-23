package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class DataRequestMessageConversor extends DataMessageConversor {

	protected void fill(DataRequestMessage from, FAPMessage to, MessageCode messageCode) {
		super.fill(from, to, messageCode);
		to.appendASCIIField(from.getCardNumber(), 11);
	}
	
	protected void fill(FAPMessage from, DataRequestMessage to) throws UnsupportedEncodingException {
		super.fill(from, to);
		to.setCardNumber(from.extractASCIIField(26, 11));
	}

}
