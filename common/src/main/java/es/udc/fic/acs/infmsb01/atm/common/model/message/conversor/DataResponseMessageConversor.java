package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor;

import java.io.UnsupportedEncodingException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class DataResponseMessageConversor extends DataMessageConversor {

	protected void fill(DataResponseMessage from, FAPMessage to, MessageCode messageCode) {
		super.fill(from, to, messageCode);
		to.appendNumericField(new Long(from.getResponseCode().getCode()), 2);
	}
	
	protected void fill(FAPMessage from, DataResponseMessage to) throws UnsupportedEncodingException {
		super.fill(from, to);
		to.setResponseCode(DataResponseCode.getDataResponseCode(
				Byte.parseByte(from.extractNumericField(26, 2))));
	}
	
}
