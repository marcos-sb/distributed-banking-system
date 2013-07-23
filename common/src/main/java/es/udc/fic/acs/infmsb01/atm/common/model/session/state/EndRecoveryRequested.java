package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

final class EndRecoveryRequested extends State {

	private static final EndRecoveryRequested instance =
			new EndRecoveryRequested();
	
	private EndRecoveryRequested() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void endRecoveryAccepted(Context context) {
		context.setState(SessionOpened.getInstance());
	}
	
	@Override
	void deniedEndRecovery(Context context) {
		context.setState(RecoveringMessages.getInstance());
	}
}
