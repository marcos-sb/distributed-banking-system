package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlMessage;

public final class RequestRestartTraffic extends ControlMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestRestartTraffic(RecipientInfo from, RecipientInfo to) {
		super(from, to);
	}
	
	public RequestRestartTraffic() {
		super();
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString();
	}

}
