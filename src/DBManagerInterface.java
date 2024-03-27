import java.util.ArrayList;

// ---------------------Interfaces for the 2 database managers

interface CacheManagerInterface {
    public WeatherInfo checkTopValueWeather(String date, String location);
    public ArrayList<WeatherInfo> checkTopValueFiveDay(String date, String location);
    public AirPollutionInfo checkTopValueAirPol(String date, String location);
}


public interface DBManagerInterface {
    public void saveInfo(WeatherInstance obj);
}









