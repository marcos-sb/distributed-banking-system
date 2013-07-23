package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseRestartTrafficConversor extends
		ControlResponseMessageConversor {

	public final FAPMessage create(ResponseRestartTraffic message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSERESTARTTRAFFIC);
		
		return newMessage;
	}
	
	public final ResponseRestartTraffic create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseRestartTraffic newMessage = new ResponseRestartTraffic();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseRestartTraffic) message);
	}
	
}
