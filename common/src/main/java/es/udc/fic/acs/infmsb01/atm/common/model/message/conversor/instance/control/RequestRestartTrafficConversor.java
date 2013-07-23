package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestRestartTrafficConversor extends
		ControlMessageConversor {

	public final FAPMessage create(RequestRestartTraffic message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTRESTARTTRAFFIC);
		
		return newMessage;
	}
	
	public final RequestRestartTraffic create(FAPMessage message) throws UnsupportedEncodingException {
		RequestRestartTraffic newMessage = new RequestRestartTraffic();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestRestartTraffic) message);
	}
	
}
