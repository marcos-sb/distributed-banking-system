package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.DataResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseAccountsTransferConversor extends
		DataResponseMessageConversor {

	public final FAPMessage create(ResponseAccountsTransfer message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSEACCOUNTSTRANSFER);
		newMessage.appendASCIIField(
				message.getSourceAccountBalance() >= 0?"+":"-", 1);
		newMessage.appendNumericField(message.getSourceAccountBalance(), 10, 2);
		
		newMessage.appendASCIIField(
				message.getDestinationAccountBalance()>=0?"+":"-", 1);
		newMessage.appendNumericField(message.getDestinationAccountBalance(), 10, 2);
		
		return newMessage;
	}
	
	public final ResponseAccountsTransfer create(FAPMessage message) throws UnsupportedEncodingException {
		ResponseAccountsTransfer newMessage = new ResponseAccountsTransfer();
		
		super.fill(message, newMessage);
		
		String signSourceAccount = message.extractASCIIField(28, 1);
		double sourceAccountBalance =
				Double.parseDouble(message.extractNumericField(29, 10, 2));
		
		if(signSourceAccount.equals("-")) {
			sourceAccountBalance *= -1;
		}
		newMessage.setSourceAccountBalance(sourceAccountBalance);
		
		String signDestinationAccount = message.extractASCIIField(39, 1);
		double destinationAccountBalance =
				Double.parseDouble(message.extractNumericField(40, 10, 2));
		
		if(signDestinationAccount.equals("-")) {
			destinationAccountBalance *= -1;
		}
		newMessage.setDestinationAccountBalance(destinationAccountBalance);
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseAccountsTransfer) message);
	}
	
}
