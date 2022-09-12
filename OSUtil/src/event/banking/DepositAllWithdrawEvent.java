package event.banking;

import java.util.HashMap;

import org.osbot.rs07.event.Event;

import event.banking.internal.BankDepositAllEvent;
import event.banking.internal.BankWithdrawEvent;
import event.banking.internal.CloseBankEvent;
import event.banking.internal.OpenBankEvent;
import event.banking.internal.WalkToClosestBankEvent;

public class DepositAllWithdrawEvent extends Event {
	
	private HashMap<String, Integer> withdrawItems;
	public DepositAllWithdrawEvent(HashMap<String, Integer> withdrawItems) {
		this.withdrawItems = withdrawItems;
	}
	
	@Override public int execute() throws InterruptedException {
		execute(new WalkToClosestBankEvent());

		OpenBankEvent openBank = new OpenBankEvent();
		execute(openBank);
		
		if (openBank.hasFailed()) {
			log("[DepositAllWithdrawEvent] Failed to open bank");
			setFailed();
			return 100;
		}
		
		BankDepositAllEvent bankDepo = new BankDepositAllEvent();
		execute(bankDepo);
		if (bankDepo.hasFailed()) {
			log("[DepositAllWithdrawEvent] Failed to depo items");
			setFailed();
			return 100;
		}
		
		
		BankWithdrawEvent withdrawEvent = new BankWithdrawEvent(withdrawItems);
		execute(withdrawEvent);
		if (withdrawEvent.hasFailed()) {
			log("[DepositAllWithdrawEvent] Failed to withdraw items");
			setFailed();
			return 100;
		}
		
		
		CloseBankEvent closeBank = new CloseBankEvent();
		execute(closeBank);
		if (closeBank.hasFailed()) {
			log("[DepositEvent] Failed to close bank");
			setFailed();
			return 100;
		}
		
		setFinished();
		return 100;
	}

}
