import java.util.ArrayList;

import data.Platform;
import javafx.scene.control.Label;

public class StationView {

	private final String stationName;
	private final ArrayList<Platform> stationPlatforms;
	private ArrayList<PlatformView> stationPlatformViews = new ArrayList<PlatformView>();
	private final Label stationNameLabel;
	
	public StationView(String stationName,  ArrayList<Platform> stationPlatforms){
		this.stationName=stationName;
		this.stationPlatforms=stationPlatforms;
		this.stationNameLabel= new Label(stationName);
		buildStationPlatforms();
	}
	
	private void buildStationPlatforms(){
		for (Platform platform : stationPlatforms) {
	    	  
	    	  PlatformView platView = new PlatformView(platform);
	    	  stationPlatformViews.add(platView);
	      }
	}
	
}
