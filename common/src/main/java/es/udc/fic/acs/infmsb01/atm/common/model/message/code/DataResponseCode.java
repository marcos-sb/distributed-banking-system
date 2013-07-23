package es.udc.fic.acs.infmsb01.atm.common.model.message.code;

import java.util.HashMap;

public enum DataResponseCode {

	ACCEPTED((byte)0),
	DENIED((byte)10),
	DENIEDCARDHELD((byte)11),
	DENIEDBADCARD((byte)12),
	DENIEDBADACCOUNT((byte)13),
	DENIEDAMMOUNTLIMIT((byte)14),
	DENIEDSAMEACCOUNT((byte)21),
	DENIEDNOFUNDSSOURCE((byte)22),
	DENIEDBADSOURCE((byte)23),
	DENIEDBADDESTINATION((byte)24);
	
	private static HashMap<Byte, DataResponseCode> map;
	private byte code;
	
	private DataResponseCode(byte code) {
		this.code = code;
		put(code, this);
	}
	
	private static final void put(byte code, DataResponseCode drc) {
		if(map == null) {
			map = new HashMap<Byte, DataResponseCode>();
		}
		map.put(code, drc);
	}
	
	public static final DataResponseCode getDataResponseCode(byte code) {
		return map.get(code);
	}
	
	public byte getCode() {
		return code;
	}
	
}
