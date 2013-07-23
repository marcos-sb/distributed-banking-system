package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.sql.Connection;

import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class ResponseDepositConsortiumPersistenceAction extends
		DataResponseConsortiumPersistenceAction {

	public ResponseDepositConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	protected final void alterSession(Connection connection) {

		ConsortiumSession cs = (ConsortiumSession) getSession();
		ResponseDeposit response = (ResponseDeposit) getMessage();

		RequestDeposit request = (RequestDeposit) cs.getCurrentRequest(response
				.getChannelNumber());

		cs.setSumDeposits(cs.getSumDeposits() + request.getAmmount());
	}

}
