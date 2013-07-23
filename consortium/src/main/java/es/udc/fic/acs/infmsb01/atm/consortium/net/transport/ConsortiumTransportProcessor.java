package es.udc.fic.acs.infmsb01.atm.consortium.net.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ATMInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.processor.MessageConversorProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.UDPTransport;

public final class ConsortiumTransportProcessor {

	private DatagramSocket atmSocket;
	private DatagramSocket bankSocket;

	private ConsortiumInfo ci;

	private static HashMap<String, BankInfo> bankID2Bank;
	private static HashMap<String, ATMInfo> atmID2ATM;

	private boolean enabledBankLinkReception, enabledBankLinkTransmission,
			enabledATMLinkReception, enabledATMLinkTransmission;

	public ConsortiumTransportProcessor(ConsortiumInfo ci,
			ArrayList<BankInfo> bis) throws SocketException,
			UnknownHostException {
		this.ci = ci;
		atmSocket = new DatagramSocket(ci.getPort());
		bankSocket = new DatagramSocket(ci.getBankPort());

		bankID2Bank = fillBankID2Bank(bis);
		atmID2ATM = new HashMap<String, ATMInfo>();

		enabledBankLinkReception = true;
		enabledBankLinkTransmission = true;
		enabledATMLinkReception = true;
		enabledATMLinkTransmission = true;
	}

	private HashMap<String, BankInfo> fillBankID2Bank(ArrayList<BankInfo> bis) {

		HashMap<String, BankInfo> bankId2bi = new HashMap<String, BankInfo>();

		for (BankInfo bi : bis) {
			bankId2bi.put(bi.getId(), bi);
		}

		return bankId2bi;

	}

	public final void send2Bank(Message message) throws IOException,
			InstantiationException, IllegalAccessException {

		if (enabledBankLinkTransmission) {
			UDPTransport transport = new UDPTransport();
			BankInfo bi = bankID2Bank.get(message.getTo().getId());

			transport.send(MessageConversorProcessor.convert(message)
					.serialize(), bi.getIp(), bi.getPort(), bankSocket);

			System.out.printf("CTP_OUT: %s%n", message.toString());
		} else {
			System.out.printf("CTP_OUT(DROPPED FRAME): %s%n",
					message.toString());
		}

	}

	public final void send2ATM(Message message) throws IOException,
			InstantiationException, IllegalAccessException {

		if (enabledATMLinkTransmission) {
			UDPTransport transport = new UDPTransport();
			ATMInfo ai = atmID2ATM.get(message.getTo().getId());

			transport.send(MessageConversorProcessor.convert(message)
					.serialize(), ai.getIp(), ai.getPort(), atmSocket);

			System.out.printf("CTP_OUT: %s%n", message.toString());
		} else {
			System.out.printf("CTP_OUT(DROPPED FRAME): %s%n",
					message.toString());
		}

	}

	public final BankInfo getRegisteredBank(RecipientInfo bankID) {
		return bankID2Bank.get(bankID.getId());
	}

	public final ConsortiumInfo getConsortiumInfo() {
		return this.ci;
	}

	public final Message receiveBank() throws UnsupportedEncodingException,
			InstantiationException, IllegalAccessException, ParseException,
			IOException { // register bank upon each reception

		while (true) {
			UDPTransport transport = new UDPTransport();
			DatagramPacket receivedPacket = transport.receive(bankSocket);
			Message receivedMessage = MessageConversorProcessor
					.convert(new FAPMessage(receivedPacket.getData()));

			if (enabledBankLinkReception) {
				if(receivedMessage instanceof RequestOpenSession) {
					RequestOpenSession req = (RequestOpenSession) receivedMessage;
					BankInfo bi = req.getBankInfo();

					bankID2Bank.put(bi.getId(), bi);
				}
				

				System.out.printf("CTP_IN: %s%n", receivedMessage.toString());

				return receivedMessage;
			} else {
				System.out.printf("CTP_IN(DROPPED FRAME): %s%n",
						receivedMessage.toString());
			}
		}

	}

	public final Message receiveATM() throws UnsupportedEncodingException,
			InstantiationException, IllegalAccessException, ParseException,
			IOException { // register ATM upon each reception

		while (true) {
			UDPTransport transport = new UDPTransport();
			DatagramPacket receivedPacket = transport.receive(atmSocket);
			Message receivedMessage = MessageConversorProcessor
					.convert(new FAPMessage(receivedPacket.getData()));

			if (enabledATMLinkReception) {
				ATMInfo ai = new ATMInfo();
				ai.setId(receivedMessage.getFrom().getId());
				ai.setIp(receivedPacket.getAddress().getHostAddress());
				ai.setPort(receivedPacket.getPort());

				atmID2ATM.put(ai.getId(), ai);

				System.out.printf("CTP_IN: %s%n", receivedMessage.toString());

				return receivedMessage;
			} else {
				
				System.out.printf("CTP_IN(DROPPED FRAME): %s%n",
						receivedMessage.toString());
			}
		}
	}

	public final void setBankLinkState(TransportProcessorLinkState linkState) {
		switch (linkState) {
		case ONLINE:
			this.enabledBankLinkReception = true;
			this.enabledBankLinkTransmission = true;
			break;

		case DISABLEDRECEPTION:
			this.enabledBankLinkReception = false;
			this.enabledBankLinkTransmission = true;
			break;

		case DISABLEDTRANSMISSION:
			this.enabledBankLinkReception = true;
			this.enabledBankLinkTransmission = false;
			break;

		case OFFLINE:
			this.enabledBankLinkReception = false;
			this.enabledBankLinkTransmission = false;
		}
	}

	public final void setATMLinkState(TransportProcessorLinkState linkState) {
		switch (linkState) {
		case ONLINE:
			this.enabledATMLinkReception = true;
			this.enabledATMLinkTransmission = true;
			break;

		case DISABLEDRECEPTION:
			this.enabledATMLinkReception = false;
			this.enabledATMLinkTransmission = true;
			break;

		case DISABLEDTRANSMISSION:
			this.enabledATMLinkReception = true;
			this.enabledATMLinkTransmission = false;
			break;

		case OFFLINE:
			this.enabledATMLinkReception = false;
			this.enabledATMLinkTransmission = false;
		}
	}

}
