package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.DataRequestMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestDepositConversor extends DataRequestMessageConversor {

	public final FAPMessage create(RequestDeposit message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTDEPOSIT);
		newMessage.appendNumericField(new Long(message.getAssociatedAccountNumber()), 1);
		newMessage.appendNumericField(new Long(message.getAmmount()), 4);
		
		return newMessage;
	}
	
	public final RequestDeposit create(FAPMessage message) throws UnsupportedEncodingException {
		RequestDeposit newMessage = new RequestDeposit();
		
		super.fill(message, newMessage);
		newMessage.setAssociatedAccountNumber(
				Byte.parseByte(message.extractNumericField(37, 1)));
		newMessage.setAmmount(
				Short.parseShort(message.extractNumericField(38, 4)));
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestDeposit) message);
	}
}
