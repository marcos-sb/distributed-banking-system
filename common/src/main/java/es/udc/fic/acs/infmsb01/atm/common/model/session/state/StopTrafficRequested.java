package es.udc.fic.acs.infmsb01.atm.common.model.session.state;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;

final class StopTrafficRequested extends State {

	private static final StopTrafficRequested instance =
			new StopTrafficRequested();
	
	private StopTrafficRequested() {}
	
	static State getInstance() {
		return instance;
	}
	
	@Override
	void stopTrafficAccepted(Context context) {
		context.setState(TrafficStopped.getInstance());
	}
	
	@Override
	void acceptDataMessage(Context context) throws InvalidStateException {
		
	}
	
	@Override
	void deniedStopTraffic(Context context) throws InvalidStateException {
		context.setState(SessionOpened.getInstance());
	}
	
}
