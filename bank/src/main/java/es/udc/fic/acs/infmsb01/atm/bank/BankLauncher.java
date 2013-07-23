package es.udc.fic.acs.infmsb01.atm.bank;

import java.io.IOException;

import es.udc.fic.acs.infmsb01.atm.bank.controller.BankViewController;
import es.udc.fic.acs.infmsb01.atm.bank.model.gateway.BankControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.bank.model.message.processor.BankMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.bank.net.transport.BankTransportProcessor;
import es.udc.fic.acs.infmsb01.atm.bank.net.transport.BankTransportProcessorRunnable;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.util.ConfigurationParametersManager;

public final class BankLauncher {

	private static final String BANK_SOCKET_PORT_CONFIGURATION_PARAMETER = "BankLauncher/bankSocketPort";
	private static final String BANK_SOCKET_IP_CONFIGURATION_PARAMETER = "BankLauncher/bankSocketIP";
	private static final String BANK_ID_CONFIGURATION_PARAMETER = "BankLauncher/bankID";

	private static final String CONSORTIUM_PORT_CONFIGURATION_PARAMETER = "BankLauncher/consortiumPort";
	private static final String CONSORTIUM_IP_CONFIGURATION_PARAMETER = "BankLauncher/consortiumIP";
	private static final String CONSORTIUM_ID_CONFIGURATION_PARAMETER = "BankLauncher/consortiumID";

	public static void main(String[] args) {

		try {

			BankInfo bi = new BankInfo();
			bi.setId(getParameter(BANK_ID_CONFIGURATION_PARAMETER));
			bi.setIp(getParameter(BANK_SOCKET_IP_CONFIGURATION_PARAMETER));
			bi.setPort(Integer
					.parseInt(getParameter(BANK_SOCKET_PORT_CONFIGURATION_PARAMETER)));

			ConsortiumInfo ci = new ConsortiumInfo();
			ci.setId(getParameter(CONSORTIUM_ID_CONFIGURATION_PARAMETER));
			ci.setIp(getParameter(CONSORTIUM_IP_CONFIGURATION_PARAMETER));
			ci.setPort(Integer.parseInt(getParameter(CONSORTIUM_PORT_CONFIGURATION_PARAMETER)));

			BankTransportProcessor btp = new BankTransportProcessor(bi, ci);

			BankViewController bvc = new BankViewController(bi.getId());
			
			BankMessageProcessor bmp = new BankMessageProcessor(
					new RecipientInfo(bi.getId()),
					new RecipientInfo(ci.getId()), btp);
			
			new BankControllerModelGateway(bmp, bvc);

			Thread consortiumNETThread = new Thread(new BankTransportProcessorRunnable(bmp));
			
			consortiumNETThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final String getParameter(String param) throws IOException {
		return ConfigurationParametersManager.getParameter(param);
	}
}
