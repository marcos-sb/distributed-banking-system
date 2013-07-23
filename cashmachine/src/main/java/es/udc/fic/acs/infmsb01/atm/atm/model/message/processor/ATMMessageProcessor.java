package es.udc.fic.acs.infmsb01.atm.atm.model.message.processor;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.HashMap;

import es.udc.fic.acs.infmsb01.atm.atm.model.gateway.ATMControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.ATMGatewayMessageAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.RequestAccountsTransferATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.RequestAccountMovementsATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.RequestCheckBalanceATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.RequestDepositATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.RequestWithdrawATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.ResponseAccountMovementsATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.ResponseAccountsTransferATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.ResponseCheckBalanceATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.ResponseDepositATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance.ResponseWithdrawATMAction;
import es.udc.fic.acs.infmsb01.atm.atm.model.session.ATMSession;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidRecipientException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.ActionProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.Action;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.NetAction;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessor;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class ATMMessageProcessor {
	
	private static HashMap<Class<? extends DataMessage>,
		Class<? extends Action>> message2Action;
	
	private ATMSession atmSession;
	private TransportProcessor transportProcessor;
	private ATMControllerModelGateway gateway;
	
	public ATMMessageProcessor(RecipientInfo atmID, RecipientInfo consortiumID, TransportProcessor tp) throws SocketException, UnknownHostException {
		this.atmSession = new ATMSession(consortiumID, atmID);
		transportProcessor = tp;
		gateway = null;
	}
	
	public final void setGateway(ATMControllerModelGateway gateway) {
		this.gateway = gateway;
	}
	
	public final TransportProcessor getTransport() {
		return this.transportProcessor;
	}
	
	public final void process(DataRequestMessage message) throws InstantiationException, IllegalAccessException, IOException, ParseException {
		
		message.setFrom(atmSession.getAtmID());
		message.setTo(atmSession.getConsortiumID());
		
		NetAction action = (NetAction) getAction(message);
		action.setSession(atmSession);
		action.setMessage(message);
		action.setTransportProcessor(transportProcessor);
		
		ActionProcessor.process(action);
		
	}
	
	public final void process(DataResponseMessage message) throws InstantiationException, IllegalAccessException, IOException, ParseException, InvalidRecipientException {
		
		if(!message.getFrom().equals(
				atmSession.getConsortiumID())) {
			throw new InvalidRecipientException(atmSession.getConsortiumID().toString());
		}
		
		if(!message.getTo().equals(
				atmSession.getAtmID())) {
			throw new InvalidRecipientException(atmSession.getAtmID().toString());
		}
		
		ATMGatewayMessageAction action = (ATMGatewayMessageAction) getAction(message);
		action.setSession(atmSession);
		action.setMessage(message);
		action.setGateway(gateway);
		
		ActionProcessor.process(action);
		
		gateway.updateView(message);
		
	}

	private static final Action getAction(DataMessage message) throws InstantiationException, IllegalAccessException {
		if(message2Action == null) {
			fillMessage2Action();
		}
		
		return message2Action.get(message.getClass()).newInstance();
	}

	private static final void fillMessage2Action() {
		
		message2Action = new HashMap<Class<? extends DataMessage>, Class<? extends Action>>();
		
		message2Action.put(RequestAccountMovements.class, RequestAccountMovementsATMAction.class);
		message2Action.put(RequestAccountsTransfer.class, RequestAccountsTransferATMAction.class);
		message2Action.put(RequestCheckBalance.class, RequestCheckBalanceATMAction.class);
		message2Action.put(RequestDeposit.class, RequestDepositATMAction.class);
		message2Action.put(RequestWithdraw.class, RequestWithdrawATMAction.class);
		message2Action.put(ResponseAccountMovements.class, ResponseAccountMovementsATMAction.class);
		message2Action.put(ResponseAccountsTransfer.class, ResponseAccountsTransferATMAction.class);
		message2Action.put(ResponseCheckBalance.class, ResponseCheckBalanceATMAction.class);
		message2Action.put(ResponseDeposit.class, ResponseDepositATMAction.class);
		message2Action.put(ResponseWithdraw.class, ResponseWithdrawATMAction.class);
		
	}
	
	public final void setTransportProcessorLinkState(TransportProcessorLinkState linkState) {
		transportProcessor.setLinkState(linkState);
	}
	
}
