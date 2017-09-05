import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public enum Platforms {

  INSTANCE;

  private Platforms() {
    buildPlatformCache();
  }

  private final Map<Integer, Platform> platformCache = new LinkedHashMap<Integer, Platform>();
  

  private void buildPlatformCache() {
    platformCache.put(1, new Platform(1, "M90-1", "M90"));
    platformCache.put(2, new Platform(2, "M90-2", "M90"));
    platformCache.put(3, new Platform(3, "M90-3", "M90"));
    platformCache.put(4, new Platform(4, "M80-1", "M80"));
    platformCache.put(5, new Platform(5, "M80-2", "M80"));
    platformCache.put(6, new Platform(6, "M70-1", "M70"));
    platformCache.put(7, new Platform(7, "M70-2", "M70"));
    platformCache.put(8, new Platform(8, "M60-1", "M60"));
    platformCache.put(9, new Platform(9, "M60-2", "M60"));
    platformCache.put(10, new Platform(10, "M50-1", "M50"));
    platformCache.put(11, new Platform(11, "M50-2", "M50"));
    platformCache.put(12, new Platform(12, "M40-1", "M40"));
    platformCache.put(13, new Platform(13, "M40-2", "M40"));
    platformCache.put(14, new Platform(14, "M30-1", "M30"));
    platformCache.put(15, new Platform(15, "M30-2", "M30"));
    platformCache.put(16, new Platform(16, "M20-1", "M20"));
    platformCache.put(17, new Platform(17, "M20-2", "M20"));
    platformCache.put(18, new Platform(18, "M16-1", "M16"));
    platformCache.put(19, new Platform(19, "M16-2", "M16"));
    platformCache.put(20, new Platform(20, "M10-1", "M10"));
    platformCache.put(21, new Platform(21, "M10-2", "M10"));
    platformCache.put(22, new Platform(22, "A90-1", "A90"));
    platformCache.put(23, new Platform(23, "A90-2", "A90"));
    platformCache.put(24, new Platform(24, "A80-1", "A80"));
    platformCache.put(25, new Platform(25, "A80-2", "A80"));
    platformCache.put(26, new Platform(26, "A70-1", "A70"));
    platformCache.put(27, new Platform(27, "A70-2", "A70"));
    platformCache.put(28, new Platform(28, "A60-1", "A60"));
    platformCache.put(29, new Platform(29, "A60-2", "A60"));
    platformCache.put(30, new Platform(30, "A50-1", "A50"));
    platformCache.put(31, new Platform(31, "A50-2", "A50"));
    platformCache.put(32, new Platform(32, "A40-1", "A40"));
    platformCache.put(33, new Platform(33, "A40-2", "A40"));
    platformCache.put(34, new Platform(34, "A30-1", "A30"));
    platformCache.put(35, new Platform(35, "A30-2", "A30"));
    platformCache.put(36, new Platform(36, "A20-1", "A20"));
    platformCache.put(37, new Platform(37, "A20-2", "A20"));
    platformCache.put(38, new Platform(38, "A10-1", "A10"));
    platformCache.put(39, new Platform(39, "A10-2", "A10"));
    platformCache.put(40, new Platform(40, "R60-1", "R60"));
    platformCache.put(41, new Platform(41, "R60-2", "R60"));
    platformCache.put(42, new Platform(42, "R50-1", "R50"));
    platformCache.put(43, new Platform(43, "R50-2", "R50"));
    platformCache.put(44, new Platform(44, "R40-1", "R40"));
    platformCache.put(45, new Platform(45, "R40-2", "R40"));
    platformCache.put(46, new Platform(46, "R30-1", "R30"));
    platformCache.put(47, new Platform(47, "R30-2", "R30"));
    platformCache.put(48, new Platform(48, "R20-1", "R20"));
    platformCache.put(49, new Platform(49, "R20-2", "R20"));
    platformCache.put(50, new Platform(50, "R10-1", "R10"));
    platformCache.put(51, new Platform(51, "R10-2", "R10"));
    platformCache.put(52, new Platform(52, "C60-1", "C60"));
    platformCache.put(53, new Platform(53, "C60-2", "C60"));
    platformCache.put(54, new Platform(54, "C50-1", "C50"));
    platformCache.put(55, new Platform(55, "C50-2", "C50"));
    platformCache.put(56, new Platform(56, "C40-1", "C40"));
    platformCache.put(57, new Platform(57, "C40-2", "C40"));
    platformCache.put(58, new Platform(58, "C30-1", "C30"));
    platformCache.put(59, new Platform(59, "C30-2", "C30"));
    platformCache.put(60, new Platform(60, "C20-1", "C20"));
    platformCache.put(61, new Platform(61, "C20-2", "C20"));
    platformCache.put(62, new Platform(62, "C10-1", "C10"));
    platformCache.put(63, new Platform(63, "C10-2", "C10"));
    platformCache.put(64, new Platform(64, "K30-1", "K30"));
    platformCache.put(65, new Platform(65, "K30-2", "K30"));
    platformCache.put(66, new Platform(66, "K30-3", "K30"));
    platformCache.put(67, new Platform(67, "K30-4", "K30"));
    platformCache.put(68, new Platform(68, "K20-1", "K20"));
    platformCache.put(69, new Platform(69, "K20-2", "K20"));
    platformCache.put(70, new Platform(70, "K20-3", "K20"));
    platformCache.put(71, new Platform(71, "K10-1", "K10"));
    platformCache.put(72, new Platform(72, "K10-2", "K10"));
    platformCache.put(73, new Platform(73, "K10-3", "K10"));
    platformCache.put(74, new Platform(74, "C70-1", "C70"));
    platformCache.put(75, new Platform(75, "C70-2", "C70"));
    platformCache.put(76, new Platform(76, "W10-1", "W10"));
    platformCache.put(77, new Platform(77, "W10-2", "W10"));
    platformCache.put(78, new Platform(78, "W10-3", "W10"));
    platformCache.put(79, new Platform(79, "C80-1", "C80"));
    platformCache.put(80, new Platform(80, "C80-2", "C80"));
    platformCache.put(81, new Platform(81, "L10-1", "L10"));
    platformCache.put(82, new Platform(82, "L10-2", "L10"));
    platformCache.put(83, new Platform(83, "L30-1", "L30"));
    platformCache.put(84, new Platform(84, "L30-2", "L30"));
    platformCache.put(85, new Platform(85, "W20-1", "W20"));
    platformCache.put(86, new Platform(86, "W20-2", "W20"));
    platformCache.put(87, new Platform(87, "W30-1", "W30"));
    platformCache.put(88, new Platform(88, "W30-2", "W30"));
    platformCache.put(89, new Platform(89, "W40-1", "W40"));
    platformCache.put(90, new Platform(90, "W40-2", "W40"));
    platformCache.put(91, new Platform(91, "W40-3", "W40"));
    platformCache.put(92, new Platform(92, "Y10-1", "Y10"));
    platformCache.put(93, new Platform(93, "Y10-2", "Y10"));
    platformCache.put(94, new Platform(94, "Y10-3", "Y10"));
    platformCache.put(95, new Platform(95, "Y10-4", "Y10"));
    platformCache.put(96, new Platform(96, "L20-1", "L20"));
    platformCache.put(97, new Platform(97, "L20-2", "L20"));
    platformCache.put(98, new Platform(98, "S20-1", "S20"));
    platformCache.put(99, new Platform(99, "S20-2", "S20"));
    platformCache.put(101, new Platform(101, "S40-1", "S40"));
    platformCache.put(102, new Platform(102, "S40-2", "S40"));
    platformCache.put(103, new Platform(103, "S50-1", "S50"));
    platformCache.put(104, new Platform(104, "S50-2", "S50"));
  }

  public Map<Integer, Platform> getPlatformCache(){
    return Collections.unmodifiableMap(platformCache);
  }
  
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
    
    public String toString(){
      return name;
      
    }

  }

}
