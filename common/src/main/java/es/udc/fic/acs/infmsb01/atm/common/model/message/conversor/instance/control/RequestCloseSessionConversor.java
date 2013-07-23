package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public class RequestCloseSessionConversor extends ControlMessageConversor {

	public final FAPMessage create(RequestCloseSession message) {
		FAPMessage newFAPMessage = new FAPMessage();
		
		super.fill(message, newFAPMessage,
				MessageCode.REQUESTCLOSESESSION);
		
		newFAPMessage.appendNumericField(message.getSumDeposits(), 10);
		newFAPMessage.appendNumericField(message.getSumWidrawals(), 10);
		newFAPMessage.appendNumericField(message.getSumTransfers(), 10);
		
		return newFAPMessage;
	}
	
	public final RequestCloseSession create(FAPMessage message) throws UnsupportedEncodingException {
		RequestCloseSession newMessage = new RequestCloseSession();
		
		super.fill(message, newMessage);
		newMessage.setSumDeposits(Long.parseLong(message.extractNumericField(18, 10)));
		newMessage.setSumWidrawals(Long.parseLong(message.extractNumericField(28, 10)));
		newMessage.setSumTransfers(Long.parseLong(message.extractNumericField(38, 10)));
		
		return newMessage;
	}

	@Override
	public final FAPMessage create(Message message) {
		return create((RequestCloseSession) message);
	}
	
}
