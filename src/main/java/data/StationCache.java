package data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public enum StationCache {

  INSTANCE;

  private StationCache() {
    buildStationCache();
  }

  private final Map<String, ArrayList<Platform>> stationCache =
      new LinkedHashMap<String, ArrayList<Platform>>();

  private void buildStationCache() {
    Map<Integer, Platform> platforms = PlatformCache.INSTANCE.getPlatformCache();

    for (Platform platform : platforms.values()) {
      ArrayList<Platform> stationPlatforms = stationCache.get(platform.station);

      if (stationPlatforms == null) {
        stationPlatforms = new ArrayList<Platform>(4);
      }

      stationPlatforms.add(platform);
      stationCache.put(platform.station, stationPlatforms);

    }

  }
  
  public Map<String, ArrayList<Platform>> getStationCache(){
    return Collections.unmodifiableMap(stationCache);
  }


}
