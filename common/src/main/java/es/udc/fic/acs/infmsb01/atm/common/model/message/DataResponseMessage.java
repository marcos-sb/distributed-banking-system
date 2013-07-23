package es.udc.fic.acs.infmsb01.atm.common.model.message;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;

public abstract class DataResponseMessage extends DataMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataResponseCode responseCode;

	public DataResponseMessage(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			DataResponseCode responseCode) {

		super(from, to, channelNumber, messageNumber, bankOnline);
		this.responseCode = responseCode;

	}
	
	public DataResponseMessage() {}

	public DataResponseCode getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(DataResponseCode responseCode) {
		this.responseCode = responseCode;
	}
	
	@Override
	public String toString() {
		return super.toString() + " rc:" + responseCode;
	}
	
//	public static void main(String [] args) {
//		System.out.printf("%s", DataResponseCode.DENIEDCARDHELD);
//	}

}
