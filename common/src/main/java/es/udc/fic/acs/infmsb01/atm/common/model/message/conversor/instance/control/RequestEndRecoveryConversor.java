package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestEndRecoveryConversor extends ControlMessageConversor {

	public final FAPMessage create(RequestEndRecovery message) {
		FAPMessage newFAPMessage = new FAPMessage();
		
		super.fill(message, newFAPMessage,
				MessageCode.REQUESTENDRECOVERY);
		
		return newFAPMessage;
	}
	
	public final RequestEndRecovery create(FAPMessage message) throws UnsupportedEncodingException {
		RequestEndRecovery newMessage = new RequestEndRecovery();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestEndRecovery) message);
	}
	
}
