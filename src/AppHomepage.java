import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

interface HomepageInfo {

    public void displayLocationForm();
    public void displayInfo(WeatherInstance weather_inst, WeatherInfo weather, String location);
}

public class AppHomepage extends JFrame implements HomepageInfo {
    // private JTabbedPane tabbedPane;
    // private DisplayWeather DisplayWeather;
    // private Display5DayWeather display5DayWeatherTab;
    // private DisplayAirPollution displayAirPollutionTab;

    private JTextField locationField;
    private JTextArea weatherOutputArea;
    private JButton getWeatherButton;

    AppHomepage() {
        // // Initialize the tabs
        // DisplayWeather = new DisplayWeather();
        // display5DayWeatherTab = new Display5DayWeather();
        // displayAirPollutionTab = new DisplayAirPollution();

        // // Create the tabbed pane and add the tabs
        // tabbedPane = new JTabbedPane();
        // tabbedPane.addTab("Current Weather", DisplayWeather); // Fixed addTab() method used
        // tabbedPane.addTab("5-Day Weather", display5DayWeatherTab);
        // tabbedPane.addTab("Air Pollution", displayAirPollutionTab);

        // Add the tabbed pane to the main frame (no need to create another JFrame)
         // Set window for GUI
         setTitle("Weather App");
         setSize(1280, 720);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.displayLocationForm();

        getWeatherButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();

                WeatherAPI weather_api = new WeatherAPI();
                WeatherInfo weather = weather_api.getInfo(location);
                SunInfo suninfo = weather_api.getSunTimes(location);

                AirPollutionAPI airPollution_api = new AirPollutionAPI();
                AirPollutionInfo airpol = airPollution_api.getInfo(location);

                FiveDayAPI fiveday_api = new FiveDayAPI();
                fiveday_api.getInfo(location);
                ArrayList<WeatherInfo> fivedayweather = fiveday_api.getInfo(location);

                DateTime date = new DateTime();
                Location locationObj = new Location(1.0, 1.0, location);

                WeatherInstance weather_inst = new WeatherInstance(date, locationObj, weather, suninfo, airpol, fivedayweather);
                
                displayInfo(weather_inst, weather, location);
            }
            });

    }

    public void displayLocationForm() {
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

    public void displayInfo(WeatherInstance weather_inst, WeatherInfo weather, String location) {
        DisplayWeather display_weather = new DisplayWeather();
        DisplayAirPollution display_airpol = new DisplayAirPollution();
        Display5DayWeather display_5Day = new Display5DayWeather();

        String output = display_weather.displayWeather(weather_inst.getWeatherInfo(), location) + "\n";
        output += display_airpol.displayAirPol(weather_inst.getAirPollutionInfo(), location) + "\n";
        output += display_5Day.display5DaysWeather(weather_inst.getFiveDayWeather(), location) + "\n";

            
        // Generating Notification for Poor Weather Conditions
    
        // 1) Heavy Rainfall
        if (weather.getRainVol1h() >= 30.0) {
            // Display Notification
            JOptionPane.showMessageDialog(getWeatherButton, "Heavy rainfall! Rain volume in the past 1 hour: " + weather.getRainVol1h() + " mm", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 2) Thunderstorms
        if (weather.getWeatherDescr().equalsIgnoreCase("thunderstorm")) {
            // Display Thunderstorm Alert
            JOptionPane.showMessageDialog(getWeatherButton, "Thunderstorm alert!", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }


        // 3) Check for extreme heat
        if (weather.getTemperature() > 35.0) {
            // Extreme Heat
            JOptionPane.showMessageDialog(getWeatherButton, "Extreme heat warning! Temperature: " + weather.getTemperature() + " C", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 4) Check for extreme cold
        if (weather.getTemperature() < -10.0) {
            // Extreme Cold
            JOptionPane.showMessageDialog(getWeatherButton, "Extreme cold warning! Temperature: " + weather.getTemperature() + " C", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 5) Check for snowfall or blizzard
        if (weather.getWeatherDescr().toLowerCase().contains("snow") || weather.getWeatherDescr().toLowerCase().contains("blizzard")) {
            // Snowfall/Blizzard
            JOptionPane.showMessageDialog(getWeatherButton, "Snowfall/Blizzard alert!", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        // 6) Check for hurricanes
        if (weather.getWeatherDescr().toLowerCase().contains("hurricane")) {
            // Hurricane
            JOptionPane.showMessageDialog(getWeatherButton, "Hurricane alert!", "Weather Alert", JOptionPane.WARNING_MESSAGE);
        }

        double aqi = weather_inst.getAirPollutionInfo().getAirQualityIndex();
        //Checking the AQI scale for poor or very poor air quality
        if (aqi == 4 || aqi == 5) {
            //Display notification
            JOptionPane.showMessageDialog(this, "Poor air quality detected! AQI: " + aqi, "Air Quality Alert", JOptionPane.WARNING_MESSAGE);
        }
    
        weatherOutputArea.setText(output);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AppHomepage();
            }
        });
    }
}