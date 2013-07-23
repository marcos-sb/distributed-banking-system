package es.udc.fic.acs.infmsb01.atm.common.model.dao.to;

import java.util.HashMap;

public enum StateName {

	OFFLINE((byte)0),
	OPENSESSIONREQUESTED((byte)1),
	SESSIONOPENED((byte)2),
	STOPTRAFFICREQUESTED((byte)3),
	RESTARTTRAFFICREQUESTED((byte)4),
	RECOVERMESSAGESREQUESTED((byte)5),
	RECOVERINGMESSAGES((byte)6),
	ENDRECOVERYREQUESTED((byte)7),
	TRAFFICSTOPPED((byte)8),
	CLOSINGSESSION((byte)9);
	
	private static HashMap<Byte, StateName> map;
	private byte code;
	
	private StateName(byte code) {
		this.code = code;
		put(code, this);
	}

	private static final void put(byte code, StateName sn) {
		if(map == null) {
			map = new HashMap<Byte, StateName>();
		}
		map.put(code, sn);
	}
	
	public static final StateName getStateName(byte code) {
		return map.get(code);
	}

	public byte getCode() {
		return code;
	}
	
}
