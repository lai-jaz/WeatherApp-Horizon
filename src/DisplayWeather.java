import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;


/*  TO DO in next meet:
    [] Separate the mess into functions/ different files (according to class diagram)
    [] Re-organize the OutPut in GUI
    [] Make Interface class for Display
    [] For Other API calls: 5day weather, Air Pollution
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
    //Useless info
    @Override 
    public void setLatitude(double lati)
    {
        Latitude = lati;
    }
    //Useless info
    @Override 
    public void setLongitude(double longi)
    {
        Longitude = longi;
    }
}

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

        // Display elements from API return on console
        String DisplayInfoGUI = "Visibility: " + obj.getVisibility() + "m\n" 
                            + "Humidity: " + obj.getHumidity() + "%\n"
                            + "Feels like: " + obj.getFeelsLike() + " C\n"
                            + "Weather Description: " + obj.getWeatherDescr() + "\n"
                            + "Temperature: " + obj.getTemperature() + " C\n"
                            + "Rain Volume (past 1h): " + obj.getRainVol1h() + "mm\n"
                            + "Rain Volume (past 3h): " + obj.getRainVol3h() + "mm\n";
        weatherOutputArea.append(DisplayInfoGUI);
    }

    private String convertUnixTime(long unixTime) {
        java.util.Date time = new java.util.Date(unixTime * 1000);
        return new java.text.SimpleDateFormat("HH:mm:ss").format(time);
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

        // Convert Unix time to simple format
        String sunrise = convertUnixTime(sunriseUnix);
        String sunset = convertUnixTime(sunsetUnix);

        // Display sunrise and sunset times
        System.out.println("Sunrise: " + sunrise);
        System.out.println("Sunset: " + sunset);

        String sunTimesText = "Sunrise: " + sunrise + "\n"
                            + "Sunset: " + sunset;

        weatherOutputArea.append(sunTimesText);
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
