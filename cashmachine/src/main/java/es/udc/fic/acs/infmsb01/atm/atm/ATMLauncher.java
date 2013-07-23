package es.udc.fic.acs.infmsb01.atm.atm;

import java.io.IOException;

import es.udc.fic.acs.infmsb01.atm.atm.controller.ATMViewController;
import es.udc.fic.acs.infmsb01.atm.atm.model.gateway.ATMControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.atm.model.message.processor.ATMMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.atm.net.transport.ATMTransportProcessor;
import es.udc.fic.acs.infmsb01.atm.atm.net.transport.ATMTransportProcessorRunnable;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ATMInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.util.ConfigurationParametersManager;

public final class ATMLauncher {

	private static final String ATM_SOCKET_PORT_CONFIGURATION_PARAMETER = "ATMLauncher/ATMSocketPort";
	private static final String ATM_SOCKET_IP_CONFIGURATION_PARAMETER = "ATMLauncher/ATMSocketIP";
	private static final String ATM_ID_CONFIGURATION_PARAMETER = "ATMLauncher/ATMID";

	private static final String CONSORTIUM_PORT_CONFIGURATION_PARAMETER = "ATMLauncher/consortiumPort";
	private static final String CONSORTIUM_IP_CONFIGURATION_PARAMETER = "ATMLauncher/consortiumIP";
	private static final String CONSORTIUM_ID_CONFIGURATION_PARAMETER = "ATMLauncher/consortiumID";

	public static void main(String[] args) {

		try {

			ATMInfo ai = new ATMInfo();
			ai.setId(getParameter(ATM_ID_CONFIGURATION_PARAMETER));
			ai.setIp(getParameter(ATM_SOCKET_IP_CONFIGURATION_PARAMETER));
			ai.setPort(Integer
					.parseInt(getParameter(ATM_SOCKET_PORT_CONFIGURATION_PARAMETER)));

			ConsortiumInfo ci = new ConsortiumInfo();
			ci.setId(getParameter(CONSORTIUM_ID_CONFIGURATION_PARAMETER));
			ci.setIp(getParameter(CONSORTIUM_IP_CONFIGURATION_PARAMETER));
			ci.setPort(Integer
					.parseInt(getParameter(CONSORTIUM_PORT_CONFIGURATION_PARAMETER)));

			ATMTransportProcessor atp = new ATMTransportProcessor(ai, ci);

			ATMViewController avc = new ATMViewController(ai.getId());

			ATMMessageProcessor amp = new ATMMessageProcessor(
					new RecipientInfo(ai.getId()),
					new RecipientInfo(ci.getId()), atp);

			new ATMControllerModelGateway(amp, avc);

			Thread consortiumNETThread = new Thread(
					new ATMTransportProcessorRunnable(amp));

			consortiumNETThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final String getParameter(String param) throws IOException {
		return ConfigurationParametersManager.getParameter(param);
	}

}
