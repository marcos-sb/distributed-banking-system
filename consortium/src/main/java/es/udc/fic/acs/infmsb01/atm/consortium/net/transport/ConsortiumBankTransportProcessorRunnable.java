package es.udc.fic.acs.infmsb01.atm.consortium.net.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor.ConsortiumBankQueueManagerRunnable;
import es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor.ConsortiumMessageProcessor;

public final class ConsortiumBankTransportProcessorRunnable implements Runnable {

	private ConsortiumMessageProcessor cmp;
		
	public ConsortiumBankTransportProcessorRunnable(ConsortiumMessageProcessor cmp)
			throws SocketException, UnknownHostException {
	
		this.cmp = cmp;
				
	}

	public void run() {
		
		try {
			
			ConsortiumBankQueueManagerRunnable.setConsortiumMessageProcessor(cmp);
			
			while(true)
				ConsortiumBankQueueManagerRunnable.queueMessage(cmp.getTransport().receiveBank());
				
			
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

}
