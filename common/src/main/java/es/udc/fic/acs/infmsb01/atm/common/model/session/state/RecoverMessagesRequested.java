package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

final class RecoverMessagesRequested extends State {

	private static final RecoverMessagesRequested instance =
			new RecoverMessagesRequested();
	
	private RecoverMessagesRequested() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void recoveryAccepted(Context context) {
		context.setState(RecoveringMessages.getInstance());
	}
	
	@Override
	void deniedRecovery(Context context) {
		context.setState(SessionOpened.getInstance());
	}
	
	@Override
	void acceptDataMessage(Context context) throws InvalidStateException {
		
	}
}
