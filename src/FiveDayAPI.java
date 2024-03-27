import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

public class FiveDayAPI extends API
{
    private static final String API_KEY = "9f0a94c33290f74d0a36ae700a156ab2";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=metric";
    protected double Latitude;
    protected double Longitude;

    @Override 
    public String APIcall(String location)
    {
        try {
            String geocode = this.APIGeoCode(location);
            this.getLatLong(geocode);

            String apiUrl = String.format(API_URL, Latitude, Longitude, API_KEY);
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //------------------------Info from API call
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\\n");
                }
                in.close();
                return response.toString();
            } 
            
            else 
            {
                return "Error: Unable to fetch weather data. Response code: " + responseCode;
            }
        } 
        catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String APIGeoCode(String location)
    {
        String API_Geo = "http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&appid=" + API_KEY;
        try {
            URL url = new URL(API_Geo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //------------------------Info from API call
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }
                in.close();
                return response.toString();
            } 
            
            else 
            {
                return "Error: Unable to fetch weather data. Response code: " + responseCode;
            }
        } 
        catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    //---------------------------- Make ArrayList for FiveDayWeather
    private ArrayList<WeatherInfo> parseWeatherData(String weatherData) {
    
        JSONObject jsonObject = new JSONObject(weatherData);
        JSONArray forecastList = jsonObject.getJSONArray("list");
    
        ArrayList<WeatherInfo> fivedayweather = new ArrayList<>(5);

        // ----------------------- Make ArrayList
        for (int i = 0; i < forecastList.length(); i++) {
            JSONObject forecastItem = forecastList.getJSONObject(i);
            String dateTime = forecastItem.getString("dt_txt");
    
            if (dateTime.endsWith("12:00:00")) {

                double temperature = forecastItem.getJSONObject("main").getDouble("temp");
                double feelsLike = forecastItem.getJSONObject("main").getDouble("feels_like");
                double humidity = forecastItem.getJSONObject("main").getDouble("humidity");
                int visibility = forecastItem.getInt("visibility");
                JSONArray weatherArray = forecastItem.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String description = weatherObject.getString("description");
                double rainVol1h = 0.0;
                double rainVol3h = 0.0;

                if (forecastItem.has("rain")) {
                    JSONObject rainObject = forecastItem.getJSONObject("rain");
                    if (rainObject.has("1h")) {
                        rainVol1h = rainObject.getDouble("1h");
                    }
                    if (rainObject.has("3h")) {
                        rainVol3h = rainObject.getDouble("3h");
                    }
                }

                WeatherInfo obj = new WeatherInfo(temperature, rainVol1h, rainVol3h, visibility, humidity, feelsLike, description);
    
                fivedayweather.add(obj);
            }
        }
        return fivedayweather;
    }
    
    public  ArrayList<WeatherInfo> getInfo(String location) 
    {
        String weatherData = this.APIcall(location);
        return parseWeatherData(weatherData);
    }

    // -------------------- Setting Latitude and Longitude
    public void setLatitude(double lati)
    {
        Latitude = lati;
    }
  
    public void setLongitude(double longi)
    {
        Longitude = longi;
    }

    public void getLatLong(String geoJSON) {
        double lat = 0, longi = 0;
        JSONArray jsonArray = new JSONArray(geoJSON);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            lat = jsonObject.getDouble("lat");
            longi = jsonObject.getDouble("lon");
        }

        this.setLatitude(lat);
        this.setLongitude(longi);
    }

}

// ----------------------------------------Turns obj info into a string
class Display5DayWeather{

    public String display5DaysWeather(ArrayList<WeatherInfo> obj, String location)
    {
        String DisplayInfoGUI = "Five day weather: \n";
        int i = 1 ;

        for (WeatherInfo weather : obj) {
            DisplayInfoGUI += "Day" + i + ":\n"+
                        "\nVisibility: " + weather.getVisibility() + "m\n" 
                        + "Humidity: " + weather.getHumidity() + "%\n" 
                        + "Feels like: " + weather.getFeelsLike() + "°C\n"
                        + "Weather Description: " + weather.getWeatherDescr() + "\n"
                        + "Temperature: " + weather.getTemperature() + "°C\n"
                        + "Rain Volume (past 1h): " + weather.getRainVol1h() + "mm\n"
                        + "Rain Volume (past 3h): " + weather.getRainVol3h() + "mm\n\n";

            i++;
        }

        return DisplayInfoGUI;
    }

}