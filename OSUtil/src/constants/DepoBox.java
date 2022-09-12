package constants;

import org.osbot.rs07.api.Map;
import org.osbot.rs07.api.map.Area;

public enum DepoBox {
	/* All F2P deposit boxes */
	CORSAIR_COVE(new Area(2567, 2863, 2571, 2860)),
	SHANTAY_PASS(new Area(3311, 3122, 3306, 3127)),
	DRAYNOR_VILLAGE(new Area(3093, 3240, 3096, 3237)),
	DUEL_ARENA(new Area(3381, 3270, 3384, 3274)),
	EDGEVILLE(new Area(3095, 3500, 3098, 3497)),
	FALADOR_WEST(new Area(2943, 3374, 2946, 3371)),
	FALADOR_EAST(new Area(3016, 3357, 3019, 3355)),
	LUMBRIDGE(new Area(3207, 3218, 3210, 3215).setPlane(2)),
	PORT_SARIM(new Area(3043, 3234, 3045, 3236)),
	VARROCK(new Area(3180, 3436, 3183, 3433));
	
	private Area area;

	DepoBox(Area area) {
		this.area = area;
	}

	public Area getArea() {
		return this.area;
	}
	
	private static int distanceTo(Map m, DepoBox b) {
		return m.distance(b.getArea().getCentralPosition());
	}
	
	public static DepoBox getClosest(Map m) {
		DepoBox closestBox = null;
		int shortestDist = Integer.MAX_VALUE;
		
		for (DepoBox db : DepoBox.values()) {
			int dist = distanceTo(m, db);
			if (dist < shortestDist) {
				shortestDist = dist;
				closestBox = db;
			}
		}
		
		return closestBox;
	}
}
