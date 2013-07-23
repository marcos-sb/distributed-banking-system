package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.sql.Connection;

import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class ResponseWithdrawConsortiumPersistenceAction extends
		DataResponseConsortiumPersistenceAction {

	public ResponseWithdrawConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	protected final void alterSession(Connection connection) {

		ConsortiumSession cs = (ConsortiumSession) getSession();
		ResponseWithdraw response = (ResponseWithdraw) getMessage();
		
		RequestWithdraw request = (RequestWithdraw) cs.getCurrentRequest(response.getChannelNumber());
		
		cs.setSumWithdrawals(cs.getSumWithdrawals() + request.getAmmount());

	}
}
