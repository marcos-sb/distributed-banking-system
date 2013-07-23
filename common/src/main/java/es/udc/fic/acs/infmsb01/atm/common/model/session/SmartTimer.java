package es.udc.fic.acs.infmsb01.atm.common.model.session;

import java.util.Timer;
import java.util.TimerTask;

public final class SmartTimer {

	private TimerTask task;
	private Timer timer;
	private long delay;
	
	public SmartTimer(long delay) {
		this.delay = delay;
		this.timer = new Timer();
	}

	public TimerTask getTask() {
		return task;
	}
	
	public boolean cancelTask() {
		return task != null ? task.cancel() : false;
	}

	public void setTask(TimerTask task) {
		this.task = task;
		timer.scheduleAtFixedRate(task, delay, 1000);
	}

}
