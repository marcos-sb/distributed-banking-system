package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestEndRecovery;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class RequestEndRecoveryConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public RequestEndRecoveryConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InvalidStateException, InstantiationException,
			IllegalAccessException {
		
		RequestEndRecovery message = (RequestEndRecovery) getMessage();

		final ConsortiumSession cs = (ConsortiumSession) getSession();
		
		cs.requestEndRecovery();

		message.setFrom(cs.getConsortium());
		message.setTo(cs.getBank());

		cs.getSmartTimer().setTask(new TimerTask() {
			
			@Override
			public void run() {
				
				this.cancel();
				
				cs.deniedEndRecovery();
				getGateway().updateView(cs);
				
			}
			
		});
		
		getTransportProcessor().send2Bank(message);
		
		return message;
		
	}

}
