package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseRecoveryConversor extends
		ControlResponseMessageConversor {

	public final FAPMessage create(ResponseRecovery message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSERECOVERY);
		
		return newMessage;
	}
	
	public final ResponseRecovery create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseRecovery newMessage = new ResponseRecovery();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseRecovery) message);
	}
	
}
