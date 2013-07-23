package es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.processor;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MessageCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.MessageConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.RequestCloseSessionConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.RequestEndRecoveryConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.RequestOpenSessionConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.RequestRecoveryConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.RequestRestartTrafficConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.RequestStopTrafficConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.ResponseCloseSessionConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.ResponseEndRecoveryConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.ResponseOpenSessionConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.ResponseRecoveryConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.ResponseRestartTrafficConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.control.ResponseStopTrafficConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.RequestAccountMovementsConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.RequestAccountsTransferConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.RequestCheckBalanceConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.RequestDepositConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.RequestWithdrawConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.ResponseAccountMovementsConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.ResponseAccountsTransferConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.ResponseCheckBalanceConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.ResponseDepositConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.conversor.instance.data.ResponseWithdrawConversor;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.protocol.FAPMessage;

public abstract class MessageConversorProcessor {

	private static HashMap<Byte, Class<? extends MessageConversor>>
	FAPMessageCode2Conversor;
	private static HashMap<Class<? extends Message>, Class<? extends MessageConversor>>
	message2Conversor;
	
	public static final Message convert(FAPMessage message)
			throws InstantiationException, IllegalAccessException, UnsupportedEncodingException, ParseException {
		
		MessageConversor conversor = getConversor(message); 
			
		return conversor.create(message);
		
	}
	
	public static final FAPMessage convert(Message message)
			throws InstantiationException, IllegalAccessException {
		
		MessageConversor conversor = getConversor(message);
		
		return conversor.create(message);
		
	}
	
	private static final MessageConversor getConversor(FAPMessage message) throws InstantiationException, IllegalAccessException {
		if(FAPMessageCode2Conversor == null) {
			fillFAPMessageCode2Conversor();
		}
		
		return FAPMessageCode2Conversor.get(
				message.getFAPMessageCode()).newInstance();
	}
	
	private static final MessageConversor getConversor(Message message) throws InstantiationException, IllegalAccessException {
		
		if(message2Conversor == null) {
			fillMessage2Conversor();
		}
		
		return message2Conversor.get(message.getClass()).newInstance();
	}
	
	private static final void fillFAPMessageCode2Conversor() {
		
		FAPMessageCode2Conversor =
				new HashMap<Byte, Class<? extends MessageConversor>>();
		
		FAPMessageCode2Conversor.put(MessageCode.REQUESTACCOUNTMOVEMENTS.getCode(), RequestAccountMovementsConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTACCOUNTSTRANSFER.getCode(), RequestAccountsTransferConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTCHECKBALANCE.getCode(), RequestCheckBalanceConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTCLOSESESSION.getCode(), RequestCloseSessionConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTDEPOSIT.getCode(), RequestDepositConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTENDRECOVERY.getCode(), RequestEndRecoveryConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTOPENSESSION.getCode(), RequestOpenSessionConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTRECOVERY.getCode(), RequestRecoveryConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTRESTARTTRAFFIC.getCode(), RequestRestartTrafficConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTSTOPTRAFFIC.getCode(), RequestStopTrafficConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.REQUESTWITHDRAW.getCode(), RequestWithdrawConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSEACCOUNTMOVEMENTS.getCode(), ResponseAccountMovementsConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSEACCOUNTSTRANSFER.getCode(), ResponseAccountsTransferConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSECHECKBALANCE.getCode(), ResponseCheckBalanceConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSECLOSESESSION.getCode(), ResponseCloseSessionConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSEDEPOSIT.getCode(), ResponseDepositConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSEENDRECOVERY.getCode(), ResponseEndRecoveryConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSEOPENSESSION.getCode(), ResponseOpenSessionConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSERECOVERY.getCode(), ResponseRecoveryConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSERESTARTTRAFFIC.getCode(), ResponseRestartTrafficConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSESTOPTRAFFIC.getCode(), ResponseStopTrafficConversor.class);
		FAPMessageCode2Conversor.put(MessageCode.RESPONSEWITHDRAW.getCode(), ResponseWithdrawConversor.class);
		
	}
	
	private static final void fillMessage2Conversor() {
		
		message2Conversor
			= new HashMap<Class<? extends Message>, Class<? extends MessageConversor>>();
	
		message2Conversor.put(RequestCloseSession.class, RequestCloseSessionConversor.class);
		message2Conversor.put(RequestEndRecovery.class, RequestEndRecoveryConversor.class);
		message2Conversor.put(RequestOpenSession.class, RequestOpenSessionConversor.class);
		message2Conversor.put(RequestRecovery.class, RequestRecoveryConversor.class);
		message2Conversor.put(RequestRestartTraffic.class, RequestRestartTrafficConversor.class);
		message2Conversor.put(RequestStopTraffic.class, RequestStopTrafficConversor.class);
		message2Conversor.put(RequestAccountMovements.class, RequestAccountMovementsConversor.class);
		message2Conversor.put(RequestAccountsTransfer.class, RequestAccountsTransferConversor.class);
		message2Conversor.put(RequestCheckBalance.class, RequestCheckBalanceConversor.class);
		message2Conversor.put(RequestDeposit.class, RequestDepositConversor.class);
		message2Conversor.put(RequestWithdraw.class, RequestWithdrawConversor.class);
		message2Conversor.put(ResponseCloseSession.class, ResponseCloseSessionConversor.class);
		message2Conversor.put(ResponseEndRecovery.class, ResponseEndRecoveryConversor.class);
		message2Conversor.put(ResponseOpenSession.class, ResponseOpenSessionConversor.class);
		message2Conversor.put(ResponseRecovery.class, ResponseRecoveryConversor.class);
		message2Conversor.put(ResponseRestartTraffic.class, ResponseRestartTrafficConversor.class);
		message2Conversor.put(ResponseStopTraffic.class, ResponseStopTrafficConversor.class);
		message2Conversor.put(ResponseAccountMovements.class, ResponseAccountMovementsConversor.class);
		message2Conversor.put(ResponseAccountsTransfer.class, ResponseAccountsTransferConversor.class);
		message2Conversor.put(ResponseCheckBalance.class, ResponseCheckBalanceConversor.class);
		message2Conversor.put(ResponseDeposit.class, ResponseDepositConversor.class);
		message2Conversor.put(ResponseWithdraw.class, ResponseWithdrawConversor.class);
		
	}
	
}
