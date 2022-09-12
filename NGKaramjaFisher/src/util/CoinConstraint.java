package util;

import org.osbot.rs07.script.MethodProvider;

public class CoinConstraint implements Constraint {
	
	private MethodProvider api;
	
	public CoinConstraint(MethodProvider api) {
		this.api = api;
	}
	
	@Override public boolean solveable() {
		return false;
	}
	@Override public boolean solved() {
		return api.getInventory().getAmount("Coins") >= 30;
	}
	@Override public boolean solve() {
		return false;
	}

}
