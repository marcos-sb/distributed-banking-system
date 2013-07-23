package es.udc.fic.acs.infmsb01.atm.common.model.message.code;

import java.util.HashMap;

public enum ErrorCode {
	
	CORRECT((byte)0),
	SESSIONOPENED((byte)30),
	MESSAGEOUTOFORDER((byte)31),
	BUSYCHANNEL((byte)32),
	UNSTOPPEDTRAFFIC((byte)33),
	RECOVERYNOTSET((byte)34),
	NOSESSION((byte)35),
	OTHER((byte)99);
	
	private static HashMap<Byte, ErrorCode> map;
	private byte code;
	
	private ErrorCode(byte code) {
		this.code = code;
		put(code, this);
	}
	
	private static final void put(byte code, ErrorCode ec) {
		if(map == null) {
			map = new HashMap<Byte, ErrorCode>();
		}
		map.put(code, ec);
	}
	
	public static final ErrorCode getErrorCode(byte code) {
		return map.get(code);
	}
	
	public byte getCode() {
		return this.code;
	}
	
	
	
}
