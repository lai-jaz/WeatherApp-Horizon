import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class Info {
    
}

//---------------------------------------------------- Weather
class WeatherInfo extends Info
{
    private double Temperature;
    private double rainVol1h;
    private double rainVol3h;
    private int Visibility;
    private double Humidity;
    private double feelsLike;
    private String weatherDescr;

    public WeatherInfo(double temp, double rain1, double rain3, int visib, double humid,
    double feels, String descr)
    {
        Temperature = temp;
        rainVol1h = rain1;
        rainVol3h = rain3;
        Visibility = visib;
        Humidity = humid;
        feelsLike = feels;
        weatherDescr = descr;
    }

     // Getters for WeatherInfo properties
     public double getTemperature() {
        return Temperature;
    }

    public double getRainVol1h() {
        return rainVol1h;
    }

    public double getRainVol3h() {
        return rainVol3h;
    }

    public int getVisibility() {
        return Visibility;
    }

    public double getHumidity() {
        return Humidity;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public String getWeatherDescr() {
        return weatherDescr;
    }
}
//------------------------------------------------------ Air Pollution
class AirPollutionInfo extends Info {
    private double AirQualityIndex;
    private double CO;
    private double NO;
    private double NO2;
    private double Ozone;
    private double SO2;
    private double Ammonia;
    private double pm2_5;
    private double pm10;

    public AirPollutionInfo(double AirQualityIndex, double CO, double NO, double N20, double Ozone, double SO2, double Ammonia, double pm2_5, double pm10) {
        this.AirQualityIndex = AirQualityIndex;
        this.CO = CO;
        this.NO = NO;
        this.NO2 = N20;
        this.Ozone = Ozone;
        this.SO2 = SO2;
        this.Ammonia = Ammonia;
        this.pm2_5 = pm2_5;
        this.pm10 = pm10;
    }

    // Getters for AirpollutionInfo properties
    public double getAmmonia() {
        return Ammonia;
    }

    public double getOzone() {
        return Ozone;
    }

    public double getNO2() {
        return NO2;
    }

    public double getNO() {
        return NO;
    }

    public double getCO() {
        return CO;
    }

    public double getAirQualityIndex() {
        return AirQualityIndex;
    }

    public double getpm10() {
        return pm10;
    }

    public double getpm2_5() {
        return pm2_5;
    }

    public double getSO2(){
        return SO2;
    }
}

//---------------------------------------------------Sunset / Sunrise timings
class SunInfo extends Info
{
    private String sunrise_time;
    private String sunset_time;

    public SunInfo(long sunset_unix, long sunrise_unix)
    {
        sunrise_time = this.convertUnixTime(sunrise_unix);
        sunset_time = this.convertUnixTime(sunset_unix);
    }

    // Getters for SunInfo properties
    public String getSunrise_Time()
    {
        return sunrise_time;
    }

    public String getSunset_Time()
    {
        return sunset_time;
    }

    private String convertUnixTime(long unixTime) {
        java.util.Date time = new java.util.Date(unixTime * 1000);
        return new java.text.SimpleDateFormat("HH:mm:ss").format(time);
    }
}

// ------------------------------------------  DATE AND TIME
class DateTime
{
    LocalDateTime datetime;
    String date;
    String time;

    public DateTime()
    {
        datetime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        date = datetime.format(formatter);
        time = datetime.format(formatterTime);
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }
}

// ------------------------------------------ LOCATION
class Location
{
    double Latitude;
    double Longitude;
    String City;

    public Location(double lat, double lon, String City)
    {
        Latitude = lat;
        Longitude = lon;
        this.City = City;
    }

    public double getLat()
    {
        return Latitude;
    }

    public double getLon()
    {
        return Longitude;
    }

    public String getCity()
    {
        return City;
    }
}

// ------------------------------------------ WEATHER INSTANCE
class WeatherInstance
{
    private WeatherInfo weatherinfo;
    private SunInfo suninfo;
    private AirPollutionInfo airpolinfo;
    private ArrayList<WeatherInfo> fivedayinfo;
    private DateTime date;
    private Location location;

    public WeatherInstance()
    {

    }

    public WeatherInstance(DateTime date, Location location, WeatherInfo weatherinfo, SunInfo suninfo, 
    AirPollutionInfo airpolinfo, ArrayList<WeatherInfo> fivedayinfo)
    {
        this.date = date;
        this.location = location;
        this.weatherinfo = weatherinfo;
        this.suninfo = suninfo;
        this.airpolinfo = airpolinfo;
        this.fivedayinfo = fivedayinfo;
    }

    public DateTime getDate()
    {
        return date;
    }

    public Location getLocation()
    {
        return location;
    }

    public WeatherInfo getWeatherInfo()
    {
        return weatherinfo;
    }

    public SunInfo getSunInfo()
    {
        return suninfo;
    }

    public AirPollutionInfo getAirPollutionInfo()
    {
        return airpolinfo;
    }
    
    public ArrayList<WeatherInfo> getFiveDayWeather()
    {
        return fivedayinfo;
    }

    public void setDate(DateTime date)
    {
        this.date = date;
    }

    public void setLocation(Location loc)
    {
        this.location = loc;
    }
    
    public void setWeatherInfo(WeatherInfo weatherInfo)
    {
        this.weatherinfo = weatherInfo;
    }

    public void setAirPollutionInfo(AirPollutionInfo airpolinfo)
    {
        this.airpolinfo = airpolinfo;
    }

    public void setSunInfo(SunInfo sunInfo)
    {
        this.suninfo = sunInfo;
    }

}
