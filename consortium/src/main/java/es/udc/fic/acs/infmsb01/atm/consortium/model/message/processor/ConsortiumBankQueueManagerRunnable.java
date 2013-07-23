package es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor;

import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;

public final class ConsortiumBankQueueManagerRunnable implements Runnable {

	private static ArrayList<Message> processingQueue = new ArrayList<Message>();
	private static Thread thread = new Thread(
			new ConsortiumBankQueueManagerRunnable());
	private static ConsortiumMessageProcessor cmp;

	private ConsortiumBankQueueManagerRunnable() {
	}

	public static final void queueMessage(Message message) {
		if(!thread.isAlive()) {
			thread.start();
		}
		synchronized (processingQueue) {
			processingQueue.add(message);
			processingQueue.notify();
		}
	}

	public static final void setConsortiumMessageProcessor(
			ConsortiumMessageProcessor cmp) {
		ConsortiumBankQueueManagerRunnable.cmp = cmp;
	}

	public void run() {
		try {
			
			while (true) {
				Message message;
				synchronized (processingQueue) {
				
					if (processingQueue.isEmpty()) {
						processingQueue.wait();
					}
				
					message = processingQueue.remove(0);
				}
				cmp.processBank(message);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
