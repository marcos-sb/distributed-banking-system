package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseStopTrafficConversor extends
		ControlResponseMessageConversor {

	public final FAPMessage create(ResponseStopTraffic message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSESTOPTRAFFIC);
		
		return newMessage;
	}
	
	public final ResponseStopTraffic create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseStopTraffic newMessage = new ResponseStopTraffic();
		
		super.fill(message, newMessage);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseStopTraffic) message);
	}
	
}
