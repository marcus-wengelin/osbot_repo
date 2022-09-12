package constants;

public enum Ore {
	/* TODO: Update ore world ids */
	EMPTY("None", 0, -1, 7469, 7468), COPPER("Copper ore", 0, 436, 11161, 10943),
	TIN("Tin ore", 0, 438, 7485, 7486), IRON("Iron ore", 15, 440, 11364, 11365),
	COAL("Coal", 30, 453, 7489, 7456), MITHRIL("Mithril ore", 55, 447, 7459, 7492),
	ADAMANTITE("Adamantite ore", 70, 449, 7460, 7493), CLAY("Clay", 1, 434, 11362, 11363);

	private String name;
	private int reqlvl;
	private int invid;
	private int[] ids;

	Ore(String name, int reqlvl, int invid, int... ids) {
		this.name = name;
		this.reqlvl = reqlvl;
		this.invid = invid;
		this.ids = ids;
	}

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.reqlvl;
	}

	public int[] getIds() {
		return this.ids;
	}

	public int getInvId() {
		return this.invid;
	}
}
