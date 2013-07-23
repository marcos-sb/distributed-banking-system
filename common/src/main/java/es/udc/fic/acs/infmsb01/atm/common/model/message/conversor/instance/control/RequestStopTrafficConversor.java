package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestStopTrafficConversor extends ControlMessageConversor {

	public final FAPMessage create(RequestStopTraffic message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTSTOPTRAFFIC);
		
		return newMessage;
	}
	
	public final RequestStopTraffic create(FAPMessage message) throws UnsupportedEncodingException {
		RequestStopTraffic newMessage = new RequestStopTraffic();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestStopTraffic) message);
	}
	
}
