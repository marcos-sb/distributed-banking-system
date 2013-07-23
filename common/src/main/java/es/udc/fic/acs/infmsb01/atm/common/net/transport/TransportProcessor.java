package es.udc.fic.acs.infmsb01.atm.common.net.transport;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.AgentInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.processor.MessageConversorProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class TransportProcessor {

	private AgentInfo destination;
	private AgentInfo origin;
	private DatagramSocket socket;

	private boolean enabledReception, enabledTransmission;

	public TransportProcessor(AgentInfo origin, AgentInfo destination)
			throws SocketException, UnknownHostException {

		socket = new DatagramSocket(origin.getPort());
		this.destination = destination;
		this.origin = origin;
		this.enabledReception = true;
		this.enabledTransmission = true;
	}

	public final void send(Message message) throws IOException,
			InstantiationException, IllegalAccessException {

		if (enabledTransmission) {
			UDPTransport transport = new UDPTransport();

			transport.send(MessageConversorProcessor.convert(message)
					.serialize(), destination.getIp(), destination.getPort(),
					socket);

			System.out.printf("TP_OUT: %s%n", message.toString());
		} else {
			System.out
					.printf("TP_OUT(DROPPED FRAME): %s%n", message.toString());
		}
	}

	public final Message receive() throws IOException, InstantiationException,
			IllegalAccessException, ParseException {

		while (true) {
			UDPTransport transport = new UDPTransport();
			Message receivedMessage = MessageConversorProcessor
					.convert(new FAPMessage(transport.receive(socket).getData()));

			if (enabledReception) {
				System.out.printf("TP_IN: %s%n", receivedMessage.toString());

				return receivedMessage;

			} else {
				System.out.printf("TP_IN(DROPPED FRAME): %s%n",
						receivedMessage.toString());
			}
		}

	}

	public final AgentInfo getDestinationInfo() {
		return destination;
	}

	public final AgentInfo getOriginInfo() {
		return origin;
	}

	public final void setLinkState(TransportProcessorLinkState linkState) {
		switch (linkState) {
		case ONLINE:
			this.enabledReception = true;
			this.enabledTransmission = true;
			break;

		case DISABLEDRECEPTION:
			this.enabledReception = false;
			this.enabledTransmission = true;
			break;

		case DISABLEDTRANSMISSION:
			this.enabledReception = true;
			this.enabledTransmission = false;
			break;

		case OFFLINE:
			this.enabledReception = false;
			this.enabledTransmission = false;
		}
	}

}
