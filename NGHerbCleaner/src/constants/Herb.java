package constants;

public enum Herb {

	GUAM("Grimy guam leaf", "Guam leaf", 2.5, 3),
	MARRENTILL("Grimy marrentill", "Marrentill", 3.8, 5),
	TARROMIN("Grimy tarromin", "Tarromin", 5, 11),
	HARRALANDER("Grimy harralander", "Harralander", 6.3, 20),
	RANARR("Grimy ranarr weed", "Ranarr weed", 7.5, 25),
	TOADFLAX("Grimy toadflax", "Toadflax", 8, 30),
	IRIT("Grimy irit leaf", "Irit leaf", 8.8, 40),
	AVANTOE("Grimy avantoe", "Avantoe", 10, 48),
	KWUARM("Grimy kwuarm", "Kwuarm", 11.3, 54),
	SNAPDRAGON("Grimy snapdragon", "Snapdragon", 11.8, 59),
	CADANTINE("Grimy cadantine", "Cadantine", 12.5, 65),
	LANTADYME("Grimy lantadyme", "Lantadyme", 13.1, 67),
	DWARF("Grimy dwarf weed", "Dwarf weed", 13.8, 70),
	TORSTOL("Grimy torstol", "Torstol", 15, 75);
	
	
	public String grimyName;
	public String cleanName;
	public double xp;
	public int    lvl;
	Herb(String grimyName, String cleanName, double xp, int lvl) {
		this.grimyName = grimyName;
		this.cleanName = cleanName;
		this.xp  = xp;
		this.lvl = lvl;
	}
}
