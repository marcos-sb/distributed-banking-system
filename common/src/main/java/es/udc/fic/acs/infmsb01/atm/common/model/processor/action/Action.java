package es.udc.fic.acs.infmsb01.atm.common.model.processor.action;

import java.io.IOException;
import java.text.ParseException;

public abstract class Action {
	
	public Action() {
		
	}
	
	public abstract Object execute() throws IOException, InstantiationException, IllegalAccessException, ParseException;
	
}
