package data;


public class Platform {
	Integer platId;
	String name, station;

	public Platform(Integer platId, String name, String station) {
		this.platId = platId;
		this.name = name;
		this.station = station;
	}

	public Integer getPlatId() {
		return platId;
	}

	public String getName() {
		return name;
	}

	public String getStation() {
		return station;
	}

	public String toString() {
		return name;

	}

}