package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MovementType;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.DataResponseMessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.AccountMovement;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public final class ResponseAccountMovementsConversor extends
		DataResponseMessageConversor {
	
	public final FAPMessage create(ResponseAccountMovements message) {
		FAPMessage newMessage = new FAPMessage();
		
		super.fill(message, newMessage, MessageCode.RESPONSEACCOUNTMOVEMENTS);
		newMessage.appendNumericField(new Long(message.getNumberOfMovements()), 2);
		
		ArrayList<AccountMovement> tempAccMovs = message.getAccountMovements();
		for(AccountMovement mov : tempAccMovs) {
			newMessage.appendNumericField(new Long(mov.getMovementType().getCode()), 2);
			newMessage.appendASCIIField(mov.getMovementAmmount() >= 0 ? "+" : "-", 1);
			newMessage.appendNumericField(new Double(mov.getMovementAmmount()), 8, 2);
			newMessage.appendDateTimeField(mov.getDate());
		}
		
		return newMessage;
	}
	
	public final ResponseAccountMovements create(FAPMessage message) throws UnsupportedEncodingException, ParseException {
		ResponseAccountMovements newMessage = new ResponseAccountMovements();
		
		super.fill(message, newMessage);
		
		ArrayList<AccountMovement> tempArrMov = newMessage.getAccountMovements();
		byte numberOfMovements = Byte.parseByte(message.extractNumericField(28, 2));
		int alignedPosition;
		for(int i = 0; i < numberOfMovements; i++) {
			
			alignedPosition = i*29;
			
			MovementType movementType =
					MovementType.getMovementType(Byte.parseByte(
							message.extractNumericField(alignedPosition + 30, 2)));
			
			String sign = message.extractASCIIField(alignedPosition + 32, 1);
			
			float ammount = Float.parseFloat
					(message.extractNumericField(alignedPosition + 33, 8, 2));
			
			if(sign.equals("-")) {
				ammount *= -1;
			}
			
			Calendar date = message.extractDateTimeField(alignedPosition + 41);
			AccountMovement am = 
					new AccountMovement(ammount, date, movementType);
			tempArrMov.add(am);
		}
		
		return newMessage;
	}

	@Override
	public FAPMessage create(Message message) {
		return create((ResponseAccountMovements) message);
	}
	
	public static void main(String [] args) {
		
	}

}
