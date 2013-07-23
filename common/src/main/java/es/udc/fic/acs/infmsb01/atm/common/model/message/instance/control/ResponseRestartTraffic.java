package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;

public final class ResponseRestartTraffic extends ControlResponseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResponseRestartTraffic(RecipientInfo from, RecipientInfo to,
			ControlResponseCode accepted, ErrorCode errorCode) {
		
		super(from, to, accepted, errorCode);
		
	}

	public ResponseRestartTraffic() {
		super();
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString();
	}

}
