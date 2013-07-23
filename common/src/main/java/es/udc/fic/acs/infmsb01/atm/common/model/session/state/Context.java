package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateTO;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

public class Context {

	private State state;
	
	public Context() {
		state = State.getInitialState();
	}
	
	void setState(State state) {
		this.state = state;
	}
	
	public void setState(StateTO state) {
		this.state = State.getState(state.getStateName());
	}
	
	public StateTO getStateTO() {
		return new StateTO(State.getStateName(state));
	}
	
	public void acceptDataMessage() throws InvalidStateException {
		state.acceptDataMessage(this);
	}
	
	public void requestOpenSession() throws InvalidStateException {
		state.requestOpenSession(this);
	}
	
	public void openSessionAccepted() throws InvalidStateException {
		state.openSessionAccepted(this);
	}
	
	public void requestStopTraffic() throws InvalidStateException {
		state.requestStopTraffic(this);
	}
	
	public void stopTrafficAccepted() throws InvalidStateException {
		state.stopTrafficAccepted(this);
	}
	
	public void requestRestartTraffic() throws InvalidStateException {
		state.requestRestartTraffic(this);
	}
	
	public void restartTrafficAccepted() throws InvalidStateException {
		state.restartTrafficAccepted(this);
	}
	
	public void requestCloseSession() throws InvalidStateException {
		state.requestCloseSession(this);
	}
	
	public void closeSessionAccepted() throws InvalidStateException {
		state.closeSessionAccepted(this);
	}
	
	public void recoveryAccepted() throws InvalidStateException {
		state.recoveryAccepted(this);
	}
	
	public void endRecoveryAccepted() throws InvalidStateException {
		state.endRecoveryAccepted(this);
	}

	public void requestRecovery() throws InvalidStateException {
		state.requestRecovery(this);
	}

	public void requestEndRecovery() throws InvalidStateException {
		state.requestEndRecovery(this);
	}

	public void deniedRecovery() throws InvalidStateException {
		state.deniedRecovery(this);
	}

	public void deniedEndRecovery() throws InvalidStateException {
		state.deniedEndRecovery(this);
	}

	public void deniedCloseSession() throws InvalidStateException {
		state.deniedCloseSession(this);
	}

	public void deniedOpenSession() throws InvalidStateException {
		state.deniedOpenSession(this);
	}

	public void deniedRestartTraffic() throws InvalidStateException {
		state.deniedRestartTraffic(this);
	}

	public void deniedStopTraffic() throws InvalidStateException {
		state.deniedStopTraffic(this);
	}
	
}
