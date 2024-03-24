import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;


/*  TO DO:
    1. Add multiple locations to check weather with longitude and latitude. []
    2. Add multiple locations to check weather with city/country name. [X]
    3. Show current weather conditions. [X]
    4. Show basic information like “Feels like, minimum and maximum temperature” etc [X]
    5. Show sunrise and sunset time. [X]
    6. Show weather forecast for 5 days. []
    7. Add timestamp for weather records []
    8. Implement Cache Management []
    9. Generate Notification for poor weather conditions. [X]
    10. Show Air Pollution data. [in progress]
    11. Generate Notification for poor air quality.  [X]
    12. Show data about polluting gasses. [in progress]
*/

class WeatherAPI extends API
{
    private static final String API_KEY = "42c9e7c4ab03739b4d29f144eaef927b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    // return API info in json format
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

                // Info from API call
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
   
    @Override 
    public void setLatitude(double lati)
    {
        Latitude = lati;
    }

    @Override 
    public void setLongitude(double longi)
    {
        Longitude = longi;
    }
}


public class DisplayWeather extends JFrame implements AppHomepage{
    
    private JTextField locationField;
    private JTextArea weatherOutputArea;
    private JButton getWeatherButton;
    
    public DisplayWeather() {
       
        // Set window for GUI
        setTitle("Weather App");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Components (Location form)
        displayLocationForm();

        // Button's action listener
        getWeatherButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                displayInfo();
                displaySunTimes();
            }
        });
    }

    // GUI functions
    public void displayInfo() 
    {
        WeatherAPI weather_api = new WeatherAPI();

        String location = locationField.getText();
        String weatherData = weather_api.APIcall(location);

        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(weatherData);

        WeatherInfo obj = getFormData(jsonObject, weatherData);
        // Display elements from API return on console
        System.out.println("Visibility: " + obj.getVisibility() + "m");
        System.out.println("Rain Volume (past 1h): " + obj.getRainVol1h() + "mm");
        System.out.println("Rain Volume (past 3h): " + obj.getRainVol3h() + "mm");
        System.out.println("Humidity: " + obj.getHumidity() + "%");
        System.out.println("Feels like: " + obj.getFeelsLike() + " C");
        System.out.println("Weather Description: " +obj.getWeatherDescr());
        System.out.println("Temperature: " + obj.getTemperature() + " C");

        // Display elements from API return on GUI
        String DisplayInfoGUI = "Visibility: " + obj.getVisibility() + "m\n" 
                            + "Humidity: " + obj.getHumidity() + "%\n"
                            + "Feels like: " + obj.getFeelsLike() + " C\n"
                            + "Weather Description: " + obj.getWeatherDescr() + "\n"
                            + "Temperature: " + obj.getTemperature() + " C\n"
                            + "Rain Volume (past 1h): " + obj.getRainVol1h() + "mm\n"
                            + "Rain Volume (past 3h): " + obj.getRainVol3h() + "mm\n";
        weatherOutputArea.append(DisplayInfoGUI);

         // Generating Notification for Poor Weather Conditions
        // 1) Heavy Rainfall
        if (obj.getRainVol1h() >= 30.0) {
            // Display Notification
            JOptionPane.showMessageDialog(this, "Heavy rainfall! Rain volume in the past 1 hour: " + obj.getRainVol1h() + " mm", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 2) Thunderstorms
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        for (int i = 0; i < weatherArray.length(); i++) {
            JSONObject weatherObject = weatherArray.getJSONObject(i);
            String mainWeather = weatherObject.getString("main");
            if (mainWeather.equalsIgnoreCase("thunderstorm")) {
                // Display Thunderstorm Alert
                JOptionPane.showMessageDialog(this, "Thunderstorm alert!", "Weather Alert", JOptionPane.WARNING_MESSAGE);
                break;
            }
        }

        // 3) Check for extreme heat
        if (obj.getTemperature() > 35.0) {
            // Extreme Heat
            JOptionPane.showMessageDialog(this, "Extreme heat warning! Temperature: " + obj.getTemperature() + " C", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 4) Check for extreme cold
        if (obj.getTemperature() < -10.0) {
            // Extreme Cold
            JOptionPane.showMessageDialog(this, "Extreme cold warning! Temperature: " + obj.getTemperature() + " C", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 5) Check for snowfall or blizzard
        if (obj.getWeatherDescr().toLowerCase().contains("snow") || obj.getWeatherDescr().toLowerCase().contains("blizzard")) {
            // Snowfall/Blizzard
            JOptionPane.showMessageDialog(this, "Snowfall/Blizzard alert!", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 6) Check for hurricanes
        if (obj.getWeatherDescr().toLowerCase().contains("hurricane")) {
            // Hurricane
            JOptionPane.showMessageDialog(this, "Hurricane alert!", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void displaySunTimes()
    {
        WeatherAPI weather_api = new WeatherAPI();

        String location = locationField.getText();
        String weatherData = weather_api.APIcall(location);

        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(weatherData);

        // Retrieve sunrise and sunset times from JSON
        long sunriseUnix = jsonObject.getJSONObject("sys").getLong("sunrise");
        long sunsetUnix = jsonObject.getJSONObject("sys").getLong("sunset");

        // SunInfo Object that stores and converts Unix times
       SunInfo sun_info = new SunInfo(sunsetUnix, sunriseUnix);

        // Display sunrise and sunset times
        String sunrise = sun_info.getSunrise_Time();
        String sunset = sun_info.getSunset_Time();
        System.out.println("Sunrise: " + sunrise);
        System.out.println("Sunset: " + sunset);

        // String sunTimesText = "Sunrise: " + sunrise + "\n"
        //                     + "Sunset: " + sunset;

    }

    public void displayLocationForm()
    {
        JLabel locationLabel = new JLabel("Enter Location:");
        locationField = new JTextField(20);
        weatherOutputArea = new JTextArea(10, 20);
        getWeatherButton = new JButton("Get Weather");

        JPanel panel = new JPanel();
        panel.add(locationLabel);
        panel.add(locationField);
        panel.add(getWeatherButton);
        add(panel, "North");
        add(new JScrollPane(weatherOutputArea), "Center");
        
        setVisible(true);
    }

    // make weather info object using the JSONObject
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DisplayWeather();
            }
        });
    }
}
