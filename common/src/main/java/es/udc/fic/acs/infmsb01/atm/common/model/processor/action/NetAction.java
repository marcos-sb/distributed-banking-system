package es.udc.fic.acs.infmsb01.atm.common.model.processor.action;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.session.FAPSession;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessor;

public abstract class NetAction extends MessageAction {

	private TransportProcessor transportProcessor;
	
	public NetAction(FAPSession session, Message message, TransportProcessor tp) {
		super(session, message);
		this.transportProcessor = tp;
	}

	public NetAction() {
		super();
	}
	
	public void setTransportProcessor(TransportProcessor tp) {
		this.transportProcessor = tp;
	}
	
	public TransportProcessor getTransportProcessor() {
		return this.transportProcessor;
	}

}
