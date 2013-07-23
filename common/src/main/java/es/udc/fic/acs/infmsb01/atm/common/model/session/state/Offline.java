package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

final class Offline extends State {

	private static final Offline instance = new Offline();
	
	private Offline() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void requestOpenSession(Context context) {
		context.setState(OpenSessionRequested.getInstance());
	}
	
}
