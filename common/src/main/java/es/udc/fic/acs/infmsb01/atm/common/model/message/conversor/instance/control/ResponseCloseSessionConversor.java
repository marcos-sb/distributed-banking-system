package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseCloseSessionConversor extends
		ControlResponseMessageConversor {

	public final FAPMessage create(ResponseCloseSession message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSECLOSESESSION);
		newMessage.appendNumericField(message.getSumDeposits(), 10);
		newMessage.appendNumericField(message.getSumWidrawals(), 10);
		newMessage.appendNumericField(message.getSumTransfers(), 10);
		
		return newMessage;
	}
	
	public final ResponseCloseSession create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseCloseSession newMessage = new ResponseCloseSession();
		
		super.fill(message, newMessage);
		newMessage.setSumDeposits(
				Long.parseLong(message.extractNumericField(22, 10)));
		newMessage.setSumWidrawals(
				Long.parseLong(message.extractNumericField(32, 10)));
		newMessage.setSumTransfers(Long.parseLong(message.extractNumericField(42, 10)));
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseCloseSession) message);
	}
	
}
