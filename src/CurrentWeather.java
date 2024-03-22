import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;

/*  TO DO in next meet:
    [] Separate the mess into functions/ different files
    [] Re-organize the OutPut in GUI
    [] Make Interface class for Display
    [] For Other API calls: 5day weather, Air Pollution
*/

public class CurrentWeather extends JFrame {
    
    private JTextField locationField;
    private JTextArea weatherOutputArea;
    private JButton getWeatherButton;
    
    private static final String API_KEY = "42c9e7c4ab03739b4d29f144eaef927b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    
    public CurrentWeather() {
       
        setTitle("Weather App");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Components
        JLabel locationLabel = new JLabel("Enter Location:");
        locationField = new JTextField(20);
        weatherOutputArea = new JTextArea(10, 20);
        getWeatherButton = new JButton("Get Weather");
        
        // Button's action listener
        getWeatherButton.addActionListener(new ActionListener() 
        {

            // Button Action
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();
                String weatherData = getWeatherData(location);

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(weatherData);

                // Store elements from API return
                double lon = jsonObject.getJSONObject("coord").getDouble("lon");
                double lat = jsonObject.getJSONObject("coord").getDouble("lat");
                String cityName = jsonObject.getString("name");
                String country = jsonObject.getJSONObject("sys").getString("country");
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String description = weatherObject.getString("description");
                double temperature = jsonObject.getJSONObject("main").getDouble("temp");

                // Display elements from API return
                System.out.println("City: " + cityName);
                System.out.println("Country: " + country);
                System.out.println("Latitude: " + lat);
                System.out.println("Longitude: " + lon);
                System.out.println("Weather Description: " + description);
                System.out.println("Temperature: " + temperature);

                weatherOutputArea.setText(weatherData);
            }
        });
        
        // Frame components
        JPanel panel = new JPanel();
        panel.add(locationLabel);
        panel.add(locationField);
        panel.add(getWeatherButton);
        add(panel, "North");
        add(new JScrollPane(weatherOutputArea), "Center");
        
        setVisible(true);
    }
    
    private String getWeatherData(String location) {
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CurrentWeather();
            }
        });
    }
}
