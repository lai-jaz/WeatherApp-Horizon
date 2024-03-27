import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;


/*  TO DO:
    1. Add multiple locations to check weather with longitude and latitude. [X]
    7. Add timestamp for weather records [X]
    8. Implement Cache Management [x]

    DONE:
    2. Add multiple locations to check weather with city/country name. [X]
    3. Show current weather conditions. [X]
    4. Show basic information like “Feels like, minimum and maximum temperature” etc [X]
    5. Show sunrise and sunset time. [X]
    6. Show weather forecast for 5 days. [X]
    9. Generate Notification for poor weather conditions. [X]
    10. Show Air Pollution data. [X]
    11. Generate Notification for poor air quality.  [X]
    12. Show data about polluting gasses. [X]
*/

public class WeatherAPI extends API
{
    private static final String API_KEY = "42c9e7c4ab03739b4d29f144eaef927b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    
    @Override 
    public String APIcall(String location)
    {
        try {
            String apiUrl = String.format(API_URL, location, API_KEY);
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //---------------------------Info from API call
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

    public WeatherInfo getInfo(String location)
    {
        String weatherData = this.APIcall(location);

        JSONObject jsonObject = new JSONObject(weatherData);
        WeatherInfo obj = getFormData(jsonObject, weatherData);
        return obj;
    }

    //---------------------------- Make WeatherInfo Object
    public WeatherInfo getFormData(JSONObject jsonObject, String weatherData)
    {
        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
        double feelsLike = jsonObject.getJSONObject("main").getDouble("feels_like");
        double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
        int visibility = jsonObject.getInt("visibility");
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        String description = weatherObject.getString("description");

        double rainVol1h = 0.0;
        double rainVol3h = 0.0;
        if(weatherData.contains("\"rain\""))
        {
            if(weatherData.contains("\"rain\": {\"1h\":"))
            {
                rainVol1h = jsonObject.getJSONObject("rain").getInt("1h");
            }
            if(weatherData.contains("\"rain\"") && weatherData.contains("\"3h\":"))
            {
                rainVol3h = jsonObject.getJSONObject("rain").getInt("3h");
            }
        }

        WeatherInfo obj = new WeatherInfo(temperature, rainVol1h, rainVol3h, visibility, humidity, feelsLike, description);
        return obj;
    }

    public SunInfo getSunTimes(String location)
    {
        String weatherData = this.APIcall(location);

        JSONObject jsonObject = new JSONObject(weatherData);
        long sunriseUnix = jsonObject.getJSONObject("sys").getLong("sunrise");
        long sunsetUnix = jsonObject.getJSONObject("sys").getLong("sunset");

       SunInfo sun_info = new SunInfo(sunsetUnix, sunriseUnix);
       return sun_info; 
    }

}

// ----------------------------------------Turns obj info into a string
class DisplayWeather{
    
    public String displayWeather(WeatherInfo obj, String location, SunInfo obj2)
    {
        String DisplayInfoGUI = "Weather for " + location
                            + "\nVisibility: " + obj.getVisibility() + "m\n" 
                            + "Humidity: " + obj.getHumidity() + "%\n"
                            + "Feels like: " + obj.getFeelsLike() + "°C\n"
                            + "Weather Description: " + obj.getWeatherDescr() + "\n"
                            + "Temperature: " + obj.getTemperature() + "°C\n"
                            + "Rain Volume (past 1h): " + obj.getRainVol1h() + "mm\n"
                            + "Rain Volume (past 3h): " + obj.getRainVol3h() + "mm\n"
                            + "Sunrise Time: " + obj2.getSunrise_Time() + "\n"
                            + "Sunset Time: " + obj2.getSunset_Time() + "\n";


        return DisplayInfoGUI;
    }
       
}