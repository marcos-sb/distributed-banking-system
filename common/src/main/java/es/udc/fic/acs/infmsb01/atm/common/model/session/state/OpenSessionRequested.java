package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

final class OpenSessionRequested extends State {

	private static final OpenSessionRequested instance =
			new OpenSessionRequested();
	
	private OpenSessionRequested() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void openSessionAccepted(Context context) {
		context.setState(SessionOpened.getInstance());
	}
	
	@Override
	void deniedOpenSession(Context context) {
		context.setState(Offline.getInstance());
	}
	
}
