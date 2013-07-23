package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public class RequestRecoveryConversor extends ControlMessageConversor {

	public final FAPMessage create(RequestRecovery message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTRECOVERY);
		
		return newMessage;
	}
	
	public final RequestRecovery create(FAPMessage message) throws UnsupportedEncodingException {
		RequestRecovery newMessage = new RequestRecovery();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestRecovery) message);
	}
	
}
