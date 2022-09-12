package events;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.filter.IdFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.script.MethodProvider;

import constants.Ore;

public class MiningEvent extends Event implements PaintableEvent {
	private static final long MAX_IDLE = 1_500;
	
	private long workingTime;
	private Position currentOrePosition;
	private RS2Object currentOre;
	private int[] targetOres;
	private boolean noMove;

	public MiningEvent(int[] targetOres, boolean noMove) {
		this.targetOres = targetOres;
		this.noMove = noMove;
		this.setBlocking();
	}
	
	private boolean arrayContains(int[] arr, int x) {
		for (int n : arr) {
			if (x == n) return true;
		}
		return false;
	}
	
	private double preciseDistance(Position p1, Position p2) {
		double p1x = p1.getX();
		double p1y = p1.getY();
		double p2x = p2.getX();
		double p2y = p2.getY();
		return Math.sqrt((p1x-p2x)*(p1x-p2x) + (p1y-p2y)*(p1y-p2y));
	}
	
	private boolean posEquals(Position a, Position b) {
		if (a == null || b == null) return false;
		return a.getX() == b.getX() && a.getY() == b.getY();
	}

	private boolean hasOre(Position pos) {
		for (RS2Object o : getObjects().get(pos.getX(), pos.getY())) {
			if (o.exists() && o.isVisible() && arrayContains(Ore.EMPTY.getIds(), o.getId()))
				return false;
			if (o.exists() && o.isVisible() && arrayContains(targetOres, o.getId()))
				return true;
		}
		return false;
	}
	
	public boolean othersInArea(Area area) {
		AreaFilter<Player> af = new AreaFilter<Player>(area);
		
		@SuppressWarnings("unchecked")
		List<Player> filter = getPlayers(). filter(af);
		for (int i = 0; i < filter.size(); i++) {
			Player p = filter.get(i);
			if (p.getId() != myPlayer().getId()) return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private List<RS2Object> findOres(@SuppressWarnings("unchecked") Filter<RS2Object>... filters) {
		
		/* Setup ore filters */
		Filter<RS2Object>[] filtersArr;
		{
			// Appends mandatory and optional filter(s) to filtersArr
			ArrayList<Filter<RS2Object>> list = new ArrayList<Filter<RS2Object>>();
			IdFilter<RS2Object> idFilter = new IdFilter<RS2Object>(targetOres);
			Filter<RS2Object> reachableFilter = new Filter<RS2Object>() {
				@Override public boolean match(RS2Object o) {
					return getMap().canReach(o);
				}
			};
			list.add(idFilter);
			list.add(reachableFilter);
			if (noMove) {
				Filter<RS2Object> besidesPlayerFilter = new Filter<RS2Object>() {
					@Override public boolean match(RS2Object o) {
						return ( (o.getX() == myPlayer().getX() && o.getY() == myPlayer().getY()+1) ||
							     (o.getX() == myPlayer().getX()+1 && o.getY() == myPlayer().getY()) ||
							     (o.getX() == myPlayer().getX() && o.getY() == myPlayer().getY()-1) ||
							     (o.getX() == myPlayer().getX()-1 && o.getY() == myPlayer().getY()) );
					}
				};
				list.add(besidesPlayerFilter);
			}
			for (Filter<RS2Object> f : filters) list.add(f);
			filtersArr = new Filter[list.size()];
			for (int i = 0; i < list.size(); i++) filtersArr[i] = list.get(i);
		}
		
		List<RS2Object> ores = getObjects().filter(filtersArr);
		ores.sort(new Comparator<RS2Object>() {
			@Override public int compare(RS2Object a, RS2Object b) {
				return Double.compare(preciseDistance(a.getPosition(), myPosition()),
									  preciseDistance(b.getPosition(), myPosition()));
			}
		});
		
		return ores;
	}
	
	public void hoverOre() {
		List<RS2Object> ores = findOres(new Filter<RS2Object>() {
			@Override public boolean match(RS2Object obj) {
				return !posEquals(currentOrePosition, obj.getPosition());
			}
		});
		if (MethodProvider.random(100) == 1 || !getMouse().isOnScreen()) {
			getMouse().moveOutsideScreen();
		} else if (!ores.isEmpty() && !getMouse().isOnCursor(ores.get(0))) {
			ores.get(0).hover();
		}
	}
		
	public boolean selectOre() {
		log("MiningEvent: Selecting ore");
		List<RS2Object> ores = findOres();
		currentOre = null;
		for (RS2Object o : ores) {
			log("MiningEvent: Found ore at "+o.getGridX()+", "+o.getGridY());
			
			if (currentOre == null) {
				currentOre = o;
			}
		}
		if (currentOre == null) return false;
		currentOrePosition = currentOre.getPosition();
		return currentOre.interact("Mine");
	}
	
	@Override
	public int execute() {
		log("Executing mining event");
		if (!getInventory().isFull()) {
			if (myPlayer().isAnimating() || myPlayer().isMoving()) {
				hoverOre();
				workingTime = System.currentTimeMillis();
			}
			if (currentOre == null || !hasOre(currentOrePosition) ||
				System.currentTimeMillis()-workingTime > MAX_IDLE) {
				if (!selectOre()) setFailed();
			}
		} else setFinished();
		
		return 300;
	}

	@Override
	public void onPaint(Graphics2D g) {
		if (currentOre != null && hasOre(currentOre.getPosition())) {
			g.draw(currentOre.getModel().getBoundingBox(currentOre.getGridX(), currentOre.getGridY(), currentOre.getZ()));
		}	
	}
}
