package util;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;

public class Combat {

	public static void setDefenceStyle(MethodProvider api) {
		api.getTabs().open(Tab.ATTACK);
		RS2Widget widg = api.getWidgets().get(593, 16);
		if (widg != null) widg.interact(widg.getInteractActions()[0]);
		api.getTabs().open(Tab.INVENTORY);
	}
	
	public static void setAttackStyle(MethodProvider api) {
		api.getTabs().open(Tab.ATTACK);
		RS2Widget widg = api.getWidgets().get(593, 4);
		if (widg != null) widg.interact(widg.getInteractActions()[0]);
		api.getTabs().open(Tab.INVENTORY);

	}
	
	public static void setStrengthStyle(MethodProvider api) {
		api.getTabs().open(Tab.ATTACK);
		RS2Widget widg = api.getWidgets().get(593, 8);
		if (widg != null) widg.interact(widg.getInteractActions()[0]);
		api.getTabs().open(Tab.INVENTORY);

	}
}
