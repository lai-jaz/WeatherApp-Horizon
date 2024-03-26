import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

interface HomepageInfo {
    void displayLocationForm();
    void displayInfo(WeatherInstance weather_inst, WeatherInfo weather, String location);
}

public class AppHomepage extends JFrame implements HomepageInfo {
    private JTextField locationField;
    private JTextArea currentWeatherOutputArea;
    private JTextArea fiveDayWeatherOutputArea;
    private JTextArea airPollutionOutputArea;
    private JButton getWeatherButton;
    private JTabbedPane tabbedPane;

    AppHomepage() {

            setTitle("Hor⚡zon");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            getContentPane().setBackground(new Color(240, 240, 240));
            setLayout(new BorderLayout());
        
            // Set program icon
            ImageIcon programIcon = new ImageIcon("4052984.png");
            setIconImage(programIcon.getImage());
        
            tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            JPanel currentWeatherPanel = new JPanel(new BorderLayout());
            JPanel fiveDayWeatherPanel = new JPanel(new BorderLayout());
            JPanel airPollutionPanel = new JPanel(new BorderLayout());
        
            currentWeatherPanel.setBorder(new TitledBorder("Current Weather"));
            fiveDayWeatherPanel.setBorder(new TitledBorder("5-Day Weather Forecast"));
            airPollutionPanel.setBorder(new TitledBorder("Air Quality Index"));
        
            tabbedPane.addTab("Current Weather", currentWeatherPanel);
            tabbedPane.addTab("5-Day Forecast", fiveDayWeatherPanel);
            tabbedPane.addTab("Air Quality", airPollutionPanel);
        
            add(tabbedPane, BorderLayout.CENTER);
        
            displayLocationForm();

    }
    
    public void displayLocationForm() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        // Add logo
        ImageIcon logoIcon = new ImageIcon("4052984.png"); 
        Image scaledImage = logoIcon.getImage().getScaledInstance(32, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledLogoIcon);
        panel.add(logoLabel);
        

        JLabel locationLabel = new JLabel("Enter Location:");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(locationLabel);

        locationField = new JTextField(20);
        locationField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(locationField);

        getWeatherButton = new JButton("Get Weather");
        getWeatherButton.setFont(new Font("Arial", Font.BOLD, 16));
        getWeatherButton.setBackground(new Color(34, 40, 49));
        getWeatherButton.setForeground(Color.WHITE);
        panel.add(getWeatherButton);

        add(panel, BorderLayout.NORTH);

        getWeatherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            try {
            
                DBWeatherInstance handler = new DBWeatherInstance("jdbc:mysql://localhost:3306/Horizon", "root", "12345678");

                String location = locationField.getText();

                WeatherAPI weather_api = new WeatherAPI();
                WeatherInfo weather = weather_api.getInfo(location);
                SunInfo suninfo = weather_api.getSunTimes(location);

                AirPollutionAPI airPollution_api = new AirPollutionAPI();
                AirPollutionInfo airpol = airPollution_api.getInfo(location);

                FiveDayAPI fiveday_api = new FiveDayAPI();
                ArrayList<WeatherInfo> fivedayweather = fiveday_api.getInfo(location);

                DateTime date = new DateTime();
                Location locationObj = new Location(1.0, 1.0, location);

                WeatherInstance weather_inst = new WeatherInstance(date, locationObj, weather, suninfo, airpol, fivedayweather);

                displayInfo(weather_inst, weather, location);

                handler.saveInfo(weather_inst);

            } catch (SQLException er) {
                System.out.println("Could not connect");
                er.printStackTrace();
            }

        

            }
        });

        setVisible(true);
    }

    public void displayInfo(WeatherInstance weather_inst, WeatherInfo weather, String location) {
        DisplayWeather display_weather = new DisplayWeather();
        DisplayAirPollution display_airpol = new DisplayAirPollution();
        Display5DayWeather display_5Day = new Display5DayWeather();
        
        String currentWeatherOutput = display_weather.displayWeather(weather_inst.getWeatherInfo(), location,weather_inst.getSunInfo()) + "\n";
        String fiveDayWeatherOutput = display_5Day.display5DaysWeather(weather_inst.getFiveDayWeather(), location) + "\n";
        String airPollutionOutput = display_airpol.displayAirPol(weather_inst.getAirPollutionInfo(), location) + "\n";
        currentWeatherOutputArea = new JTextArea(currentWeatherOutput);
        fiveDayWeatherOutputArea = new JTextArea(fiveDayWeatherOutput);
        airPollutionOutputArea = new JTextArea(airPollutionOutput);

        Font font = new Font("Arial", Font.PLAIN, 16);
        currentWeatherOutputArea.setFont(font);
        fiveDayWeatherOutputArea.setFont(font);
        airPollutionOutputArea.setFont(font);

        currentWeatherOutputArea.setLineWrap(true);
        currentWeatherOutputArea.setWrapStyleWord(true);
        fiveDayWeatherOutputArea.setLineWrap(true);
        fiveDayWeatherOutputArea.setWrapStyleWord(true);
        airPollutionOutputArea.setLineWrap(true);
        airPollutionOutputArea.setWrapStyleWord(true);

        currentWeatherOutputArea.setEditable(false);
        fiveDayWeatherOutputArea.setEditable(false);
        airPollutionOutputArea.setEditable(false);

        JScrollPane currentWeatherScrollPane = new JScrollPane(currentWeatherOutputArea);
        JScrollPane fiveDayWeatherScrollPane = new JScrollPane(fiveDayWeatherOutputArea);
        JScrollPane airPollutionScrollPane = new JScrollPane(airPollutionOutputArea);

        JPanel currentWeatherPanel = (JPanel) tabbedPane.getComponentAt(0);
        JPanel fiveDayWeatherPanel = (JPanel) tabbedPane.getComponentAt(1);
        JPanel airPollutionPanel = (JPanel) tabbedPane.getComponentAt(2);

        currentWeatherPanel.removeAll();
        currentWeatherPanel.add(currentWeatherScrollPane, BorderLayout.CENTER);

        fiveDayWeatherPanel.removeAll();
        fiveDayWeatherPanel.add(fiveDayWeatherScrollPane, BorderLayout.CENTER);

        airPollutionPanel.removeAll();
        airPollutionPanel.add(airPollutionScrollPane, BorderLayout.CENTER);
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
    
    

        tabbedPane.revalidate();
        tabbedPane.repaint();
        
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AppHomepage();
            }
        });
    }
}