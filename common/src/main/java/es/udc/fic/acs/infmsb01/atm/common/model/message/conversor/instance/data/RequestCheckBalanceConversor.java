package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.DataRequestMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestCheckBalanceConversor extends DataRequestMessageConversor {

	public final FAPMessage create(RequestCheckBalance message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTCHECKBALANCE);
		newMessage.appendNumericField(new Long(message.getAssociatedAccountNumber()), 1);
		
		return newMessage;
	}
	
	public final RequestCheckBalance create(FAPMessage message) throws UnsupportedEncodingException {
		RequestCheckBalance newMessage = new RequestCheckBalance();
		
		super.fill(message, newMessage);
		newMessage.setAssociatedAccountNumber(
				Byte.parseByte(message.extractNumericField(37, 1)));
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestCheckBalance) message);
	}
	
}
