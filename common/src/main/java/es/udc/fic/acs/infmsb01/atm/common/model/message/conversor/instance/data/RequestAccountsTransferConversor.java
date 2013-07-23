package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.DataRequestMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class RequestAccountsTransferConversor extends
		DataRequestMessageConversor {
	
	public final FAPMessage create(RequestAccountsTransfer message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.REQUESTACCOUNTSTRANSFER);
		newMessage.appendNumericField(new Long(message.getSourceAccountNumber()), 1);
		newMessage.appendNumericField(new Long(message.getDestinationAccountNumber()), 1);
		newMessage.appendNumericField(new Long(message.getAmmount()), 4);
		
		return newMessage;
	}
	
	public final RequestAccountsTransfer create(FAPMessage message) throws UnsupportedEncodingException {
		RequestAccountsTransfer newMessage = new RequestAccountsTransfer();
		
		super.fill(message, newMessage);
		newMessage.setSourceAccountNumber(
				Byte.parseByte(message.extractNumericField(37, 1)));
		newMessage.setDestinationAccountNumber(
				Byte.parseByte(message.extractNumericField(38, 1)));
		newMessage.setAmmount(Short.parseShort(message.extractNumericField(39, 4)));
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((RequestAccountsTransfer) message);
	}

}
