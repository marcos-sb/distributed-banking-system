package es.udc.fic.acs.infmsb01.atm.common.model.message.code;

import java.util.HashMap;

public enum MovementType {
	
	DEPOSIT((byte)10),
	TRANSFERSENT((byte)11),
	TRANSFERRECEIVED((byte)12),
	BILLPAYMENT((byte)13),
	WITHDRAWAL((byte)50),
	CHECK((byte)51),
	OTHER((byte)99);
	
	private static HashMap<Byte, MovementType> map;
	private byte code;
	
	private MovementType(byte code) {
		this.code = code;
		put(code, this);
	}
	
	private static final void put(byte code, MovementType mt) {
		if(map == null) {
			map = new HashMap<Byte, MovementType>();
		}
		map.put(code, mt);
	}
	
	public static final MovementType getMovementType(byte code) {
		return map.get(code);
	}

	public byte getCode() {
		return code;
	}

}
