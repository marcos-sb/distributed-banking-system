package es.udc.fic.acs.infmsb01.atm.bank.net.transport;

import java.io.IOException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.bank.model.message.processor.BankConsortiumQueueManagerRunnable;
import es.udc.fic.acs.infmsb01.atm.bank.model.message.processor.BankMessageProcessor;

public final class BankTransportProcessorRunnable implements Runnable {

	private BankMessageProcessor bmp;
	
	public BankTransportProcessorRunnable(BankMessageProcessor bmp) {
		this.bmp = bmp;
	}
	
	public void run() {
		
		try {
			
			BankConsortiumQueueManagerRunnable.setBankMessageProcessor(bmp);
			
			while(true)
				BankConsortiumQueueManagerRunnable.queueMessage(bmp.getTransport().receive());
			
		} catch (IOException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

}
