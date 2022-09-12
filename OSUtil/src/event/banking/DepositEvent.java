package event.banking;

import org.osbot.rs07.event.Event;

import event.banking.internal.BankDepositEvent;
import event.banking.internal.CloseBankEvent;
import event.banking.internal.OpenBankEvent;
import event.banking.internal.WalkToClosestBankEvent;

public class DepositEvent extends Event {
	
	private String name;
	public DepositEvent(String name) {
		this.name = name;
	}
	
	@Override public int execute() throws InterruptedException {
		execute(new WalkToClosestBankEvent());

		OpenBankEvent openBank = new OpenBankEvent();
		execute(openBank);
		
		if (openBank.hasFailed()) {
			log("[DepositEvent] Failed to open bank");
			setFailed();
			return 100;
		}
		
		BankDepositEvent bankDepo = new BankDepositEvent(name);
		execute(bankDepo);
		if (bankDepo.hasFailed()) {
			log("[DepositEvent] Failed to depo items");
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
