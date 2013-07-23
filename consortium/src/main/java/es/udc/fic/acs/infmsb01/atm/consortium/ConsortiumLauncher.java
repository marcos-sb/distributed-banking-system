package es.udc.fic.acs.infmsb01.atm.consortium;

import java.io.IOException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.util.ConfigurationParametersManager;
import es.udc.fic.acs.infmsb01.atm.consortium.controller.ConsortiumViewController;
import es.udc.fic.acs.infmsb01.atm.consortium.model.gateway.ConsortiumControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor.ConsortiumMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.consortium.net.transport.ConsortiumATMTransportProcessorRunnable;
import es.udc.fic.acs.infmsb01.atm.consortium.net.transport.ConsortiumBankTransportProcessorRunnable;

public final class ConsortiumLauncher {

	private static final String CONSORTIUM_BANK_SOCKET_PORT_CONFIGURATION_PARAMETER = "ConsortiumLauncher/consortiumBankSocketPort";
	private static final String CONSORTIUM_ATM_SOCKET_PORT_CONFIGURATION_PARAMETER = "ConsortiumLauncher/consortiumATMSocketPort";
	private static final String CONSORTIUM_IP_CONFIGURATION_PARAMETER = "ConsortiumLauncher/consortiumSocketIP";
	private static final String CONSORTIUM_ID_CONFIGURATION_PARAMETER = "ConsortiumLauncher/consortiumID";

	public static void main(String[] args) {

		try {

			ConsortiumInfo ci = new ConsortiumInfo();
			ci.setId(getParameter(CONSORTIUM_ID_CONFIGURATION_PARAMETER));
			ci.setIp(getParameter(CONSORTIUM_IP_CONFIGURATION_PARAMETER));
			ci.setPort(Integer
					.parseInt(getParameter(CONSORTIUM_ATM_SOCKET_PORT_CONFIGURATION_PARAMETER)));
			ci.setBankPort(Integer
					.parseInt(getParameter(CONSORTIUM_BANK_SOCKET_PORT_CONFIGURATION_PARAMETER)));

			ConsortiumViewController cvc = new ConsortiumViewController(ci.getId());

			ConsortiumMessageProcessor cmp = new ConsortiumMessageProcessor(ci);

			new ConsortiumControllerModelGateway(cmp, cvc);

			Thread atmNetThread = new Thread(
					new ConsortiumATMTransportProcessorRunnable(cmp));
			Thread bankNetThread = new Thread(
					new ConsortiumBankTransportProcessorRunnable(cmp));

			atmNetThread.start();
			bankNetThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final String getParameter(String param) throws IOException {
		return ConfigurationParametersManager.getParameter(param);
	}
}
