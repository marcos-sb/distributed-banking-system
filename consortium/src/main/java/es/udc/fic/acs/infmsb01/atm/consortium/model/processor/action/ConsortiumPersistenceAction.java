package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action;

import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.PersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.gateway.ConsortiumControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.consortium.net.transport.ConsortiumTransportProcessor;

public abstract class ConsortiumPersistenceAction extends PersistenceAction {

	private ConsortiumTransportProcessor transport;
	private ConsortiumControllerModelGateway gateway;
	
	public ConsortiumPersistenceAction(boolean transactional) {
		super(transactional);
	}

	public final void setTransportProcessor(ConsortiumTransportProcessor ctp) {
		this.transport = ctp;
	}

	public final ConsortiumTransportProcessor getTransportProcessor() {
		return this.transport;
	}

	public ConsortiumControllerModelGateway getGateway() {
		return gateway;
	}

	public void setGateway(ConsortiumControllerModelGateway gateway) {
		this.gateway = gateway;
	}
	
}
