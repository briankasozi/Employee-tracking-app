 import java.util.*;

public class test{
  public static void main(String[] args){
  Date date = new Date();
  String TimeZoneIds[] = TimeZone.getAvailableIDs();
  for(int i = 0; i < TimeZoneIds.length; i++){
  TimeZone tz = TimeZone.getTimeZone(TimeZoneIds[i]);
  String tzName = 
  tz.getDisplayName(tz.inDaylightTime(date), TimeZone.LONG);
  System.out.print(TimeZoneIds[i] + ": ");
  // Get the number of hours from GMT
  int rawOffset = tz.getRawOffset();
  int hour = rawOffset / (60*60*1000);
  int minute = Math.abs(rawOffset / (60*1000)) % 60;
  System.out.println(tzName + " " + hour + ":" + minute);
  }
  }
}