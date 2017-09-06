package data;

import fxviews.PlatformView;
import fxviews.StationView;

public class Platform {
	Integer platId;
	String name, station;
	PlatformView platformView;
	StationView stationView;	

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
	
	public PlatformView getPlatformView(){
		return platformView;
	}
	
	public void setPlatformView(PlatformView platformView){
		this.platformView = platformView;
	}

	protected void setStationView(StationView stationView){
		this.stationView = stationView;
	}
	
}