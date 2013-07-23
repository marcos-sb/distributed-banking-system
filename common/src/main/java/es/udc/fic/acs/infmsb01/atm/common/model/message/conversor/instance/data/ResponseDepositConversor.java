package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.DataResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseDepositConversor extends
		DataResponseMessageConversor {

	public final FAPMessage create(ResponseDeposit message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSEDEPOSIT);
		
		double balance = message.getAccountBalance();
		newMessage.appendASCIIField(balance >= 0 ? "+" : "-", 1);
		newMessage.appendNumericField(balance, 10, 2);
		
		return newMessage;
	}
	
	public final ResponseDeposit create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseDeposit newMessage = new ResponseDeposit();
		
		super.fill(message, newMessage);
		String sign = message.extractASCIIField(28, 1);
		double balance = Double.parseDouble(message.extractNumericField(29, 10, 2));
		
		if(sign.equals("-")) {
			balance *= -1;
		}
		
		newMessage.setAccountBalance(balance);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseDeposit) message);
	}
	
}
