package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

final class RestartTrafficRequested extends State {

	private static final RestartTrafficRequested instance =
			new RestartTrafficRequested();
	
	private RestartTrafficRequested() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void restartTrafficAccepted(Context context) {
		context.setState(SessionOpened.getInstance());
	}
	
	@Override
	void deniedRestartTraffic(Context context) throws InvalidStateException {
		context.setState(TrafficStopped.getInstance());
	}
	
}
