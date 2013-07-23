package es.udc.fic.acs.infmsb01.atm.common.model.exception;

import es.udc.fic.acs.infmsb01.atm.common.model.session.state.Context;

public final class InvalidStateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context currentContext;

	public InvalidStateException(Context currentContext) {
		this.currentContext = currentContext;
	}

	public InvalidStateException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidStateException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidStateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public Context getCurrentContext() {
		return currentContext;
	}
	
}
