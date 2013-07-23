package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.ControlMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestOpenSessionConversor extends ControlMessageConversor {

	public final FAPMessage create(RequestOpenSession message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTOPENSESSION);
		newMessage.appendNumericField(new Long(message.getNumberOfChannels()), 2);
		newMessage.appendDateTimeField(message.getDate());
		
		BankInfo bi = message.getBankInfo();
		newMessage.appendIPPortField(bi.getIp(), bi.getPort());
		
		return newMessage;
	}
	
	public final RequestOpenSession create(FAPMessage message) throws UnsupportedEncodingException, ParseException {
		RequestOpenSession newMessage = new RequestOpenSession();
		
		super.fill(message, newMessage);
		newMessage.setNumberOfChannels(Byte.parseByte(message.extractNumericField(18, 2)));
		newMessage.setDate(message.extractDateTimeField(20));
		
		String tempIPPort = message.extractIPPort(38);
		
		BankInfo bi = new BankInfo();
		bi.setId(newMessage.getFrom().getId());
		bi.setIp(tempIPPort.substring(0, 15));
		bi.setPort(Integer.parseInt(tempIPPort.substring(16)));
		
		newMessage.setBankInfo(bi);
		
		return newMessage;
		
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestOpenSession) message);
	}
	
}
