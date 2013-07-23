package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class MessageConversor {

	public abstract FAPMessage create(Message message);
	
	public abstract Message create(FAPMessage message) throws UnsupportedEncodingException, ParseException;
	
	protected void fill(Message from, FAPMessage to, MessageCode messageCode) {
		//TODO throw empty message EX
		//TODO throw error decoding EX
		to.appendASCIIField(from.getFrom().getId().trim(), 8);
		to.appendASCIIField(from.getTo().getId().trim(), 8);
		//TODO fix below  
		to.appendNumericField(new Long(messageCode.getCode()), 2);
	}
	
	protected void fill(FAPMessage from, Message to) throws UnsupportedEncodingException {
		//TODO throw empty message EX
		//TODO throw error decoding EX
		to.setFrom(new RecipientInfo(from.extractASCIIField(0, 8)));
		to.setTo(new RecipientInfo(from.extractASCIIField(8, 8)));
		//message code not used
	}
	
}
