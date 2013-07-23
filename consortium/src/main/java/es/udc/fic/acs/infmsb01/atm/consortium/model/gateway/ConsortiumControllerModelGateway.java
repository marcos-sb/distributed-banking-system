package es.udc.fic.acs.infmsb01.atm.consortium.model.gateway;

import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRecovery;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;
import es.udc.fic.acs.infmsb01.atm.consortium.controller.ConsortiumViewController;
import es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor.ConsortiumMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class ConsortiumControllerModelGateway {

	private ConsortiumMessageProcessor messageProcessor;
	private ConsortiumViewController controller;

	public ConsortiumControllerModelGateway(
			ConsortiumMessageProcessor messageProcessor,
			ConsortiumViewController controller) {
		this.messageProcessor = messageProcessor;
		this.controller = controller;

		messageProcessor.setGateway(this);
		controller.setGateway(this);
	}

	public final void requestRecovery(String bankID) {

		RequestRecovery request = new RequestRecovery();
		request.setTo(new RecipientInfo(bankID));

		try {

			messageProcessor.processUI(request);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public final void updateView(ConsortiumSession session) {
		controller
				.updateViewSession(session, messageProcessor.getAllSessions());
	}
	
	public final void updateView(ArrayList<ConsortiumSession> allSessions) {
		controller.updateViewSession(allSessions.get(0), allSessions);
	}

	public final void requestEndRecovery(String bankID) {

		RequestEndRecovery request = new RequestEndRecovery();
		request.setTo(new RecipientInfo(bankID));

		try {

			messageProcessor.processUI(request);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public final ConsortiumSession fetchConsortiumSession(String BankId) {
		return messageProcessor.getConsortiumSession(BankId);
	}
	
	public void setTransportProcessorBankLinkState(TransportProcessorLinkState linkState) {
		messageProcessor.setTransportProcessorBankLinkState(linkState);
	}
	
	public void setTransportProcessorATMLinkState(TransportProcessorLinkState linkState) {
		messageProcessor.setTransportProcessorATMLinkState(linkState);
	}

}
