package es.udc.fic.acs.infmsb01.atm.atm.net.transport;

import java.net.SocketException;
import java.net.UnknownHostException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ATMInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessor;

public final class ATMTransportProcessor extends TransportProcessor {

	public ATMTransportProcessor(ATMInfo origin, ConsortiumInfo destination)
			throws SocketException, UnknownHostException {
		super(origin, destination);
	}

}
