package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlMessage;

public final class RequestCloseSession extends ControlMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long sumWidrawals;
	private long sumDeposits;
	private long sumTransfers;

	public RequestCloseSession(RecipientInfo from, RecipientInfo to,
			long sumWidrawals, long sumDeposits, long sumTransfers) {

		super(from, to);
		this.sumDeposits = sumDeposits;
		this.sumTransfers = sumTransfers;
		this.sumWidrawals = sumWidrawals;

	}

	public RequestCloseSession() {
	}

	public long getSumWidrawals() {
		return sumWidrawals;
	}

	public long getSumDeposits() {
		return sumDeposits;
	}

	public long getSumTransfers() {
		return sumTransfers;
	}

	public void setSumWidrawals(long sumWidrawals) {
		this.sumWidrawals = sumWidrawals;
	}

	public void setSumDeposits(long sumDeposits) {
		this.sumDeposits = sumDeposits;
	}

	public void setSumTransfers(long sumTransfers) {
		this.sumTransfers = sumTransfers;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " sw:" + sumWidrawals + " sd:" + sumDeposits + " st:"
				+ sumTransfers;
	}

}
