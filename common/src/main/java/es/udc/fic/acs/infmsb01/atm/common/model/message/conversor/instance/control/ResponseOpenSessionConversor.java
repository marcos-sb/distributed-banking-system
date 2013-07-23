package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseOpenSessionConversor extends
		ControlResponseMessageConversor {

	public final FAPMessage create(ResponseOpenSession message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSEOPENSESSION);
		
		return newMessage;
	}
	
	public final ResponseOpenSession create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseOpenSession newMessage = new ResponseOpenSession();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseOpenSession) message);
	}
	
}
