package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;

public final class ResponseCloseSession extends ControlResponseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long sumWidrawals;
	private Long sumDeposits;
	private Long sumTransfers;

	public ResponseCloseSession(RecipientInfo from, RecipientInfo to,
			ControlResponseCode responseCode, ErrorCode errorCode,
			Long sumWidrawals, Long sumDeposits, Long sumTransfers) {

		super(from, to, responseCode, errorCode);
		this.sumDeposits = sumDeposits;
		this.sumTransfers = sumTransfers;
		this.sumWidrawals = sumWidrawals;

	}

	public ResponseCloseSession() {
		super();
	}

	public void setSumWidrawals(Long sumWidrawals) {
		this.sumWidrawals = sumWidrawals;
	}

	public void setSumDeposits(Long sumDeposits) {
		this.sumDeposits = sumDeposits;
	}

	public void setSumTransfers(Long sumTransfers) {
		this.sumTransfers = sumTransfers;
	}

	public Long getSumWidrawals() {
		return sumWidrawals;
	}

	public Long getSumDeposits() {
		return sumDeposits;
	}

	public Long getSumTransfers() {
		return sumTransfers;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " sw:" + sumWidrawals + " sd:" + sumDeposits + " st:"
				+ sumTransfers;
	}

}
