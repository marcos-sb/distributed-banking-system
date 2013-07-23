package es.udc.fic.acs.infmsb01.atm.common.model.message.code;

import java.util.HashMap;

public enum ControlResponseCode {

	ACCEPTED((byte)0),
	DENIED((byte)11);
	
	private static HashMap<Byte, ControlResponseCode> map;
	private byte code;
	
	private ControlResponseCode(byte code) {
		this.code = code;
		put(code, this);
	}
	
	private static final void put(byte code, ControlResponseCode crc) {
		if(map == null) {
			map = new HashMap<Byte, ControlResponseCode>();
		}
		map.put(code, crc);
	}
	
	public static final ControlResponseCode getControlResponseCode(byte code) {
		return map.get(code);
	}
	
	public byte getCode() {
		return this.code;
	}
	
//	public static void main(String [] args) {
//		
//		System.out.printf("%s", ControlResponseCode.getControlResponseCode((byte)0));
//		System.out.printf("%s", ControlResponseCode.getControlResponseCode((byte)11));
//	}
	
}
