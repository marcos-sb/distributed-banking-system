package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class ControlResponseMessageConversor extends
		ControlMessageConversor {
	
	protected void fill(ControlResponseMessage from, FAPMessage to, MessageCode messageCode) {
		super.fill(from, to, messageCode);
		to.appendNumericField(new Long(from.getResponseCode().getCode()), 2);
		to.appendNumericField(new Long(from.getErrorCode().getCode()), 2);
	}
	
	protected void fill(FAPMessage from, ControlResponseMessage to) throws UnsupportedEncodingException {
		super.fill(from, to);
		to.setResponseCode(ControlResponseCode.getControlResponseCode(
				Byte.parseByte(from.extractNumericField(18, 2))));
		
		to.setErrorCode(ErrorCode.getErrorCode(
				Byte.parseByte(from.extractNumericField(20, 2))));
	}

}
