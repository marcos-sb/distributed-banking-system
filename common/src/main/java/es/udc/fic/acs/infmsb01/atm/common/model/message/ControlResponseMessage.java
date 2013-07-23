package es.udc.fic.acs.infmsb01.atm.common.model.message;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;

public abstract class ControlResponseMessage extends ControlMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ControlResponseCode responseCode;
	private ErrorCode errorCode;

	public ControlResponseMessage(RecipientInfo from, RecipientInfo to,
			ControlResponseCode accepted, ErrorCode errorCode) {
		super(from, to);
		this.responseCode = accepted;
		this.errorCode = errorCode;
	}

	public ControlResponseMessage() {}
	
	public ControlResponseCode getResponseCode() {
		return responseCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setResponseCode(ControlResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public String toString() {
		return super.toString() + " crc:" + responseCode + " ec:" + errorCode;
	}

}
