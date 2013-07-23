package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action;

import es.udc.fic.acs.infmsb01.atm.bank.model.gateway.BankControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.PersistenceAction;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessor;


public abstract class BankPersistenceAction extends PersistenceAction {

	private TransportProcessor transport;
	private BankControllerModelGateway gateway;
	
	public BankPersistenceAction(boolean transactional) {
		super(transactional);
	}
	
	public TransportProcessor getTransport() {
		return transport;
	}

	public void setTransport(TransportProcessor transport) {
		this.transport = transport;
	}

	public BankControllerModelGateway getGateway() {
		return gateway;
	}

	public void setGateway(BankControllerModelGateway gateway) {
		this.gateway = gateway;
	}

}
