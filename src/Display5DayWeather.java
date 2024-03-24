import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;


class FiveDayAPI extends API
{//56b4ca79beae47bbe777752bd2dc564d
    private static final String API_KEY = "9f0a94c33290f74d0a36ae700a156ab2";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s";

    // return API info in json format
    @Override 
    public String APIcall(String location)
    {
        try {
            String geocode = this.APIGeoCode(location); // get latitude and longitude  from the geolocation
            this.getLatLong(geocode);


            String apiUrl = String.format(API_URL, Latitude, Longitude, API_KEY);
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

    public String APIGeoCode(String location)
    {
        String API_Geo = "http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&appid=" + API_KEY;
        try {
            //String apiUrl = String.format(API_URL, location, API_KEY);
            URL url = new URL(API_Geo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Info from API call
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

    public void getLatLong(String geoJSON)
    {
        double lat = 0, longi = 0;
        JSONArray jsonArray = new JSONArray(geoJSON);
        for (int i = 0; i < jsonArray.length(); i++) {
            // Access each object within the array
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            for (String key : jsonObject.keySet()) {
                lat = jsonObject.getDouble("lat");
                longi = jsonObject.getDouble("lon");
            }
        }

        this.setLatitude(lat);
        this.setLongitude(longi);
    }
    
}


class Display5DayWeather extends JFrame implements AppHomepage {
    
    private JTextField locationField;
    private JTextArea weatherOutputArea;
    private JButton getWeatherButton;
    
    public Display5DayWeather() {
       
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
                //SUNTIMES HAVE ALREADY BEEN ADDED IN THE DISPLAYiNFO() FUNCTION. IMPLEMENTED AGAIN JUST BECUASE OF THE INTERFACE
                displaySunTimes();
            }
        });
    }

    // GUI functions
    public void displayInfo() 
    {
        FiveDayAPI fiveDayAPI = new FiveDayAPI();
        String location = locationField.getText();
        String weatherData = fiveDayAPI.APIcall(location);
        weatherOutputArea.setText(parseWeatherData(weatherData));
    }

    private String parseWeatherData(String weatherData) {
        StringBuilder parsedData = new StringBuilder();
    
        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(weatherData);
        JSONArray forecastList = jsonObject.getJSONArray("list");
    
        // Store weather information for each day
        for (int i = 0; i < forecastList.length(); i++) {
            JSONObject forecastItem = forecastList.getJSONObject(i);
            String dateTime = forecastItem.getString("dt_txt");
    
            // Check if the time is 12:00:00
            if (dateTime.endsWith("12:00:00")) {
                // Extract weather details
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
    
                // Append data to StringBuilder
                parsedData.append("Date: ").append(dateTime).append("\n")
                          .append("Temperature: ").append(temperature).append(" C\n")
                          .append("Feels like: ").append(feelsLike).append(" C\n")
                          .append("Humidity: ").append(humidity).append("%\n")
                          .append("Visibility: ").append(visibility).append("m\n")
                          .append("Rain Volume (past 1h): ").append(rainVol1h).append("mm\n")
                          .append("Rain Volume (past 3h): ").append(rainVol3h).append("mm\n")
                          .append("Weather Description: ").append(description).append("\n");
    
                // Display sunrise and sunset times for each day
                long sunriseUnix = jsonObject.getJSONObject("city").getLong("sunrise");
                long sunsetUnix = jsonObject.getJSONObject("city").getLong("sunset");
                String sunrise = convertUnixTime(sunriseUnix);
                String sunset = convertUnixTime(sunsetUnix);
                parsedData.append("Sunrise: ").append(sunrise).append("\n")
                          .append("Sunset: ").append(sunset).append("\n\n");
            }
        }
    
        return parsedData.toString();
    }
    
    

    private String convertUnixTime(long unixTime) {
        java.util.Date time = new java.util.Date(unixTime * 1000);
        return new java.text.SimpleDateFormat("HH:mm:ss").format(time);
    }

    public void displaySunTimes()
    {
        FiveDayAPI weather_api = new FiveDayAPI();

        String location = locationField.getText();
        String weatherData = weather_api.APIcall(location);

        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(weatherData);

        // Retrieve sunrise and sunset times from JSON
        long sunriseUnix = jsonObject.getJSONObject("city").getLong("sunrise");
        long sunsetUnix = jsonObject.getJSONObject("city").getLong("sunset");

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
        if (weatherData.contains("\"rain\"")) {
            if (weatherData.contains("\"rain\": {\"1h\":")) {
                rainVol1h = jsonObject.getJSONObject("rain").getDouble("1h");
            }
            if (weatherData.contains("\"rain\"") && weatherData.contains("\"3h\":")) {
                rainVol3h = jsonObject.getJSONObject("rain").getDouble("3h");
            }
        }

        return new WeatherInfo(temperature, rainVol1h, rainVol3h, visibility, humidity, feelsLike, description);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Display5DayWeather();
            }
        });
    }
}