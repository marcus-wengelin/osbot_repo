package util;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;

public class Misc {
	public static boolean isTannerOpen(MethodProvider api) {
		RS2Widget w = api.getWidgets().getWidgetContainingText("What hides would you like tanning?");
		return w != null && w.isVisible();
	}	
}
