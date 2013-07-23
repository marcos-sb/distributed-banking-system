package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;

public final class ResponseAccountsTransfer extends DataResponseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double sourceAccountBalance;
	private Double destinationAccountBalance;

	public ResponseAccountsTransfer(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			DataResponseCode responseCode, Double sourceAccountBalance,
			Double destinationAccountBalance) {

		super(from, to, channelNumber, messageNumber, bankOnline, responseCode);
		this.destinationAccountBalance = destinationAccountBalance;
		this.sourceAccountBalance = sourceAccountBalance;

	}

	public ResponseAccountsTransfer() {
		super();
	}

	public void setSourceAccountBalance(Double sourceAccountBalance) {
		this.sourceAccountBalance = sourceAccountBalance;
	}

	public void setDestinationAccountBalance(Double destinationAccountBalance) {
		this.destinationAccountBalance = destinationAccountBalance;
	}

	public Double getSourceAccountBalance() {
		return sourceAccountBalance;
	}

	public Double getDestinationAccountBalance() {
		return destinationAccountBalance;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " sab:" + sourceAccountBalance + " dab:"
				+ destinationAccountBalance;
	}

}
