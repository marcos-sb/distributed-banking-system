package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;

public final class ResponseCheckBalance extends DataResponseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double accountBalance;

	public ResponseCheckBalance(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			DataResponseCode responseCode, Double accountBalance) {

		super(from, to, channelNumber, messageNumber, bankOnline, responseCode);
		this.accountBalance = accountBalance;

	}

	public ResponseCheckBalance() {
		super();
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " ab:" + accountBalance;
	}

}
