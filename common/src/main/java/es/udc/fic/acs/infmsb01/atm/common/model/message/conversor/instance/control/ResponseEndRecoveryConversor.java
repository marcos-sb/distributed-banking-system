package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseEndRecoveryConversor extends
		ControlResponseMessageConversor {

	public final FAPMessage create(ResponseEndRecovery message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSEENDRECOVERY);
		
		return newMessage;
	}
	
	public final ResponseEndRecovery create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseEndRecovery newMessage = new ResponseEndRecovery();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseEndRecovery) message);
	}
	
}
