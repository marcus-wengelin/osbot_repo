package script;

import java.awt.Color;
import java.awt.Graphics2D;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import events.DepositEvent;
import events.HopperEvent;
import events.PayDirtEvent;

@ScriptManifest(author = "LiamN",
				info = "Motherlode miner",
				logo = "",
				name = "NGMotherLode",
				version = 0)
public class Main extends Script {

	private long startTime = 0;
	
	public static String formatTime(long ms) {
		long s = (ms / 1000L) % 60L;
		long m = (ms / (1000L * 60L)) % 60L;
		long h = (ms / (1000L * 60L * 60L));
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
	@Override
	public void onStart() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		boolean invFull = getInventory().isFull();
		boolean hasPaydirt = getInventory().contains("Pay-dirt");
		boolean hasOre = getInventory().contains(new Filter<Item>() {
			@Override public boolean match(Item i) {
				return (i.getName().contains("ore") || 
						i.getName().contains("nugget"));
			}
		});
		
		if (hasOre)							// deposit ore
			execute(new DepositEvent());
		else if (invFull && hasPaydirt)		// operate hopper
			execute(new HopperEvent());
		else								// mine paydirt
			execute(new PayDirtEvent());
		return 300;
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		g.setColor(Color.RED);
		/* Draw mouse */
		int mx = (int)getMouse().getPosition().getX();
		int my = (int)getMouse().getPosition().getY();
		g.drawLine(mx-5, my, mx+5, my);
		g.drawLine(mx, my-5, mx, my+5);
		
		long ms = startTime == 0 ? 0 : System.currentTimeMillis() - startTime;
		
		g.setColor(Color.GREEN);
		g.drawString(String.format("NGMotherLode: %s", formatTime(ms)), 5, 20);
	}
}
