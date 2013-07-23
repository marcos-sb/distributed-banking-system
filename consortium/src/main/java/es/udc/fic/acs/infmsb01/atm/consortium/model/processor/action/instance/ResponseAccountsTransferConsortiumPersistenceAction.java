package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.sql.Connection;

import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;

import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class ResponseAccountsTransferConsortiumPersistenceAction extends
		DataResponseConsortiumPersistenceAction {

	public ResponseAccountsTransferConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	protected final void alterSession(Connection connection) {

		ConsortiumSession cs = (ConsortiumSession) getSession();
		ResponseAccountsTransfer response = (ResponseAccountsTransfer) getMessage();

		RequestAccountsTransfer request = (RequestAccountsTransfer) cs
				.getCurrentRequest(response.getChannelNumber());

		cs.setSumTransfers(cs.getSumTransfers() + request.getAmmount());
		
		

	}

}
