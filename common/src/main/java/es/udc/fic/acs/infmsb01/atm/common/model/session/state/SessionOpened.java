package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

final class SessionOpened extends State {

	private static final SessionOpened instance =
			new SessionOpened();
	
	private SessionOpened() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void requestStopTraffic(Context context) {
		context.setState(StopTrafficRequested.getInstance());
	}
		
	@Override
	void acceptDataMessage(Context context) throws InvalidStateException {
		
	}
	
	@Override
	void requestRecovery(Context context) {
		context.setState(RecoverMessagesRequested.getInstance());
	}
}
