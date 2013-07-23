package es.udc.fic.acs.infmsb01.atm.bank.model.dao.to;

import java.util.Calendar;

public final class AccountMovementTO {

	byte type;
	float ammount;
	Calendar date;
	
	public AccountMovementTO() {}
	
	public AccountMovementTO(byte type, float ammount, Calendar date) {
		this.type = type;
		this.ammount = ammount;
		this.date = date;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public float getAmmount() {
		return ammount;
	}

	public void setAmmount(float ammount) {
		this.ammount = ammount;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	
	
}
