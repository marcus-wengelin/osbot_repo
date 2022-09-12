package util;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.script.MethodProvider;

public class Graphics {
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
	public static void renderCursor(MethodProvider api, Graphics2D g, Color color) {
		Color oldColor = g.getColor();
		g.setColor(color);
		/* Draw mouse */
		int mx = (int)api.getMouse().getPosition().getX();
		int my = (int)api.getMouse().getPosition().getY();
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
		g.setColor(oldColor);
	}
}
