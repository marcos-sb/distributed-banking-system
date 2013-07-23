package es.udc.fic.acs.infmsb01.atm.bank.net.transport;

import java.net.SocketException;
import java.net.UnknownHostException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessor;

public final class BankTransportProcessor extends TransportProcessor {

	public BankTransportProcessor(BankInfo origin, ConsortiumInfo destination)
			throws SocketException, UnknownHostException {
		super(origin, destination);
	}

}
