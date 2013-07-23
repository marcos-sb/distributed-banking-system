package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

final class ClosingSession extends State {

	private static final ClosingSession instance =
			new ClosingSession();
	
	private ClosingSession() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void closeSessionAccepted(Context context) {
		context.setState(State.getInitialState());
	}
	
	@Override
	void deniedCloseSession(Context context) {
		context.setState(TrafficStopped.getInstance());
	}
}
