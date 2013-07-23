package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import java.util.HashMap;
import java.util.Set;

import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

abstract class State {

	private static HashMap<StateName, State> stateName2State;
	private static HashMap<State, StateName> state2StateName;
	
	static State getInitialState() {
		return Offline.getInstance();
	}
	
	static State getState(StateName stateName) {
		if(stateName2State == null) {
			fillMaps();
		}
		
		return stateName2State.get(stateName);
		
	}
	
	static StateName getStateName(State state) {
		if(state2StateName == null) {
			fillMaps();
		}
		return state2StateName.get(state);
	}
	
	private static void fillMaps() {
		
		stateName2State = new HashMap<StateName, State>();
		state2StateName = new HashMap<State, StateName>();
		
		stateName2State.put(StateName.CLOSINGSESSION, ClosingSession.getInstance());
		stateName2State.put(StateName.OFFLINE, Offline.getInstance());
		stateName2State.put(StateName.OPENSESSIONREQUESTED, OpenSessionRequested.getInstance());
		stateName2State.put(StateName.RECOVERMESSAGESREQUESTED, RecoverMessagesRequested.getInstance());
		stateName2State.put(StateName.RECOVERINGMESSAGES, RecoveringMessages.getInstance());
		stateName2State.put(StateName.ENDRECOVERYREQUESTED, EndRecoveryRequested.getInstance());
		stateName2State.put(StateName.RESTARTTRAFFICREQUESTED, RestartTrafficRequested.getInstance());
		stateName2State.put(StateName.SESSIONOPENED, SessionOpened.getInstance());
		stateName2State.put(StateName.STOPTRAFFICREQUESTED, StopTrafficRequested.getInstance());
		stateName2State.put(StateName.TRAFFICSTOPPED, TrafficStopped.getInstance());
		
		Set<StateName> keySet = stateName2State.keySet();
		State state;
		
		for(StateName stateName : keySet) {
			state = stateName2State.get(stateName);
			state2StateName.put(state, stateName);
		}
		
	}

	void requestOpenSession(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void openSessionAccepted(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void requestStopTraffic(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void stopTrafficAccepted(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void requestRestartTraffic(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void restartTrafficAccepted(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void requestCloseSession(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void closeSessionAccepted(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void recoveryAccepted(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}
	
	void endRecoveryAccepted(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void acceptDataMessage(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void requestRecovery(Context context) throws InvalidStateException {
	
		throw new InvalidStateException(context);
		
	}

	void requestEndRecovery(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void deniedRecovery(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void deniedEndRecovery(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void deniedCloseSession(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void deniedOpenSession(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void deniedRestartTraffic(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

	void deniedStopTraffic(Context context) throws InvalidStateException {
		throw new InvalidStateException(context);
	}

}
