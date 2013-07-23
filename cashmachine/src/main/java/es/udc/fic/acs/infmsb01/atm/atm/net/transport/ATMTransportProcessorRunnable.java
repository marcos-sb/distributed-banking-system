package es.udc.fic.acs.infmsb01.atm.atm.net.transport;

import java.io.IOException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.atm.model.message.processor.ATMMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidRecipientException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;

public final class ATMTransportProcessorRunnable implements Runnable {

	private ATMMessageProcessor amp;
	
	public ATMTransportProcessorRunnable(ATMMessageProcessor amp) {

		this.amp = amp;
	
	}
	
	public void run() {

		try {
			
			while(true)
				amp.process((DataResponseMessage) amp.getTransport().receive());
			
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (InvalidRecipientException e) {

			e.printStackTrace();
		}

	}

}
