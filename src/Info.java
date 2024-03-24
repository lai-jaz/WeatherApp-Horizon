public class Info {
    
}

//---------------------------------------------------- Weather
class WeatherInfo
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
class AirPollutionInfo {
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
class SunInfo
{
    private String sunrise_time;
    private String sunset_time;
    private long sunset_unix;
    private long sunrise_unix;

    public SunInfo(long sunset_unix, long sunrise_unix)
    {
        this.sunset_unix = sunset_unix;
        this.sunrise_unix = sunrise_unix;
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


