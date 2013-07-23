package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlMessage;

public final class RequestOpenSession extends ControlMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte numberOfChannels;
	private Calendar date;
	private BankInfo bankInfo;

	public RequestOpenSession(RecipientInfo from, RecipientInfo to,
			byte numberOfChannels, Calendar date, BankInfo bi) {

		super(from, to);
		this.numberOfChannels = numberOfChannels;
		this.date = date;
		this.bankInfo = bi;
	}

	public RequestOpenSession() {
		super();
	}

	public BankInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfo bankInfo) {
		this.bankInfo = bankInfo;
	}

	public Calendar getDate() {
		return date;
	}

	public byte getNumberOfChannels() {
		return numberOfChannels;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setNumberOfChannels(byte numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " nc:" + numberOfChannels + " @ "
				+ sdf.format(date.getTime()) + " bi:" + bankInfo.toString();
	}

	// public static void main(String [] args) {
	// Calendar date = Calendar.getInstance();
	// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	// System.out.printf("%s", sdf.format(date.getTime()));
	// }

}
