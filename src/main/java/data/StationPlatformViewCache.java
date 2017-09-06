package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import fxviews.StationView;



public enum StationPlatformViewCache {
  INSTANCE;

  private StationPlatformViewCache() {
    buildViewCaches();
  }

  private final Map<String, ArrayList<Platform>> stationCache = new LinkedHashMap<String, ArrayList<Platform>>();

  private void buildViewCaches() {
    Map<String, ArrayList<Platform>> stations = StationCache.INSTANCE.getStationCache();

    for (Map.Entry<String, ArrayList<Platform>> station : stations.entrySet()) {
      StationView stationView = new StationView(station.getKey(),station.getValue());
      
      
    }
  }

  public Map<String, ArrayList<Platform>> getStationCache() {
    return Collections.unmodifiableMap(stationCache);
  }


}
