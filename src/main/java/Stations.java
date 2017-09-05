import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public enum Stations {

  INSTANCE;

  private Stations() {
    buildStationCache();
  }

  private final Map<String, ArrayList<Platforms.Platform>> stationCache =
      new LinkedHashMap<String, ArrayList<Platforms.Platform>>();

  private void buildStationCache() {
    Map<Integer, Platforms.Platform> platforms = Platforms.INSTANCE.getPlatformCache();

    for (Platforms.Platform platform : platforms.values()) {
      ArrayList<Platforms.Platform> stationPlatforms = stationCache.get(platform.station);

      if (stationPlatforms == null) {
        stationPlatforms = new ArrayList<Platforms.Platform>(4);
      }

      stationPlatforms.add(platform);
      stationCache.put(platform.station, stationPlatforms);

    }

  }
  
  public Map<String, ArrayList<Platforms.Platform>> getStationCache(){
    return Collections.unmodifiableMap(stationCache);
  }


}
