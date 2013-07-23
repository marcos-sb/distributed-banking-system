package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MovementType;

public final class AccountMovement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float movementAmmount;
	private Calendar date;
	private MovementType movementType;
	
	public AccountMovement(float movementAmmount, Calendar date,
			MovementType movementType) {
		
		this.movementAmmount = movementAmmount;
		this.date = date;
		this.movementType = movementType;
		
	}
	
	public float getMovementAmmount() {
		return movementAmmount;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public MovementType getMovementType() {
		return movementType;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return super.toString() + " ma:" + movementAmmount + " @ " + sdf.format(date.getTime()) + " mt:" + movementType;
	}
	
}
