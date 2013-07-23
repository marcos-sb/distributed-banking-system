package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

final class TrafficStopped extends State {

	private static final TrafficStopped instance =
			new TrafficStopped();
	
	private TrafficStopped() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void requestCloseSession(Context context) {
		context.setState(ClosingSession.getInstance());
	}
	
	@Override
	void requestRestartTraffic(Context context) {
		context.setState(RestartTrafficRequested.getInstance());
	}
}
