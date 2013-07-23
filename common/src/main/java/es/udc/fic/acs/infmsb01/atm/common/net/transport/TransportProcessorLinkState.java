package es.udc.fic.acs.infmsb01.atm.common.net.transport;

public enum TransportProcessorLinkState {

	ONLINE((byte) 0),
	DISABLEDRECEPTION((byte)1),
	DISABLEDTRANSMISSION((byte)2),
	OFFLINE((byte)3);
	
	private byte code;
	
	private TransportProcessorLinkState(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return this.code;
	}
	
}
