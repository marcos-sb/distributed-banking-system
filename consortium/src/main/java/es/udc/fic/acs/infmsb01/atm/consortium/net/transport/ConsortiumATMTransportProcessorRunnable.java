package es.udc.fic.acs.infmsb01.atm.consortium.net.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor.ConsortiumATMQueueManagerRunnable;
import es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor.ConsortiumMessageProcessor;

public final class ConsortiumATMTransportProcessorRunnable implements Runnable {
	
	ConsortiumMessageProcessor cmp;
	
	public ConsortiumATMTransportProcessorRunnable(ConsortiumMessageProcessor cmp) throws SocketException, UnknownHostException {
		
		this.cmp = cmp;
	}
	
	public void run() {
		
		try {
			
			ConsortiumATMQueueManagerRunnable.setConsortiumMessageProcessor(cmp);
			
			while(true)
				ConsortiumATMQueueManagerRunnable.queueMessage(cmp.getTransport().receiveATM());

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
