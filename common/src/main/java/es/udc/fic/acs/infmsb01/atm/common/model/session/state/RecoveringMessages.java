package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

final class RecoveringMessages extends State {

	private static final RecoveringMessages instance =
			new RecoveringMessages();
	
	private RecoveringMessages() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void requestEndRecovery(Context context) {
		context.setState(EndRecoveryRequested.getInstance());
	}
	
	@Override
	void acceptDataMessage(Context context) throws InvalidStateException {
		
	}
}
