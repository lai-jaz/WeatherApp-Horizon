//to read user input from the console
import java.util.Scanner;
import java.util.ArrayList;
import java.sql.*;

public class ConsoleBasedUI implements HomepageInfo {
    private Scanner scanner;
    private DBWeatherInstance dbHandler;
    
    //TextFile Database Object
    //private DB2WeatherInstance dbHandler;
    
    //objects of weather api class
    private WeatherAPI weatherAPI;
    private AirPollutionAPI airPollutionAPI;
    private FiveDayAPI fiveDayAPI;

    public void displayLocationForm()
    {

    }

    public void displayInfo(WeatherInstance weather_inst, WeatherInfo weather, String location)
    {

    }

    //constructor
    public ConsoleBasedUI()
    {
        scanner = new Scanner(System.in);
        try {
            dbHandler = new DBWeatherInstance();

        } catch (SQLException e) {
            System.out.println("Could not connect");
        }
        
        //Text File Database
        // dbHandler = new DB2WeatherInstance();
        
        weatherAPI = new WeatherAPI();
        airPollutionAPI = new AirPollutionAPI();
        fiveDayAPI = new FiveDayAPI();
    }

    public void displayMenu()
    {
        boolean valid = true;
        String location = null;

        SunInfo sunTimes = null;
        WeatherInstance weatherinstance = null;
        
        //Get input from the user
        System.out.println("Enter Location: ");
        location=scanner.nextLine();

        sunTimes = weatherAPI.getSunTimes(location); 
        DateTime datetime = new DateTime();
        Location loc = new Location(0, 0, location);

        WeatherInfo currentWeather = dbHandler.checkTopValueWeather(datetime.getDate(), loc.getCity());
        AirPollutionInfo airPollutionInfo= dbHandler.checkTopValueAirPol(datetime.getDate(), loc.getCity());
        ArrayList<WeatherInfo> fiveDayWeather=dbHandler.checkTopValueFiveDay(datetime.getDate(), loc.getCity());

        // API CALL ifdatetime.getDate(), loc.getLocation() the object returned from cache managing functions is NULL 
                if(currentWeather == null)
                {
                    currentWeather = weatherAPI.getInfo(location);
                    airPollutionInfo = airPollutionAPI.getInfo(location);
                    fiveDayWeather = fiveDayAPI.getInfo(location);
                   
                    weatherinstance = new WeatherInstance(datetime, loc, currentWeather, sunTimes, airPollutionInfo, fiveDayWeather);

                    saveWeatherInstance(weatherinstance);
                }
                else{
                    weatherinstance = new WeatherInstance(datetime, loc, currentWeather, sunTimes, airPollutionInfo, fiveDayWeather);
                }

        currentWeather = weatherAPI.getInfo(location);
        airPollutionInfo = airPollutionAPI.getInfo(location);
        fiveDayWeather = fiveDayAPI.getInfo(location);

        while (valid)
        {
            System.out.println("--- Weather App ---");
            System.out.println("--- MENU ---");
            System.out.println("1) Show Current Weather");
            System.out.println("2) Show Air Pollution Data");
            System.out.println("3) Show 5-Day Forecast");
            System.out.println("4) Exit");
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
                case 1:
                    getCurrentWeather(weatherinstance);
                    break;
                case 2:
                    getAirPollutionData(weatherinstance);
                    break;
                case 3:
                    getFiveDayForecast(weatherinstance);
                    break;
                case 4:
                    valid = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter again.");
                    break;
            }
        }
        scanner.close();
    }
    
    private void saveWeatherInstance(WeatherInstance weatherInstance) {
       dbHandler.saveInfo(weatherInstance);
    }


    
    private void getCurrentWeather(WeatherInstance weatherInstance) 
    {
         DisplayWeather displayWeather = new DisplayWeather();
        if (weatherInstance.getWeatherInfo()!= null) {
            System.out.println("Current Weather:");
            System.out.println(displayWeather.displayWeather(weatherInstance.getWeatherInfo() , weatherInstance.getLocation().getCity(),weatherInstance.getSunInfo()));

             // Displaying Notifications for Poor Weather Conditions
            if (weatherInstance.getWeatherInfo().getRainVol1h() >= 30.0) {
            System.out.println("Heavy rainfall! Rain volume in the past 1 hour: " + weatherInstance.getWeatherInfo().getRainVol1h() + " mm");
            }
            if (weatherInstance.getWeatherInfo().getWeatherDescr().toLowerCase().contains("thunderstorm")) {
            System.out.println("Thunderstorm alert!");
            }
            if (weatherInstance.getWeatherInfo().getTemperature() > 35.0) {
            System.out.println("Extreme heat warning! Temperature: " + weatherInstance.getWeatherInfo().getTemperature() + " C");
            }
            if (weatherInstance.getWeatherInfo().getTemperature() < -10.0) {
            System.out.println("Extreme cold warning! Temperature: " + weatherInstance.getWeatherInfo().getTemperature() + " C");
            }
            if (weatherInstance.getWeatherInfo().getWeatherDescr().toLowerCase().contains("snow") || weatherInstance.getWeatherInfo().getWeatherDescr().toLowerCase().contains("blizzard")) {
            System.out.println("Snowfall/Blizzard alert!");
            }
            if (weatherInstance.getWeatherInfo().getWeatherDescr().toLowerCase().contains("hurricane")) {
            System.out.println("Hurricane alert!");
            }
        } 
        else
         {
            System.out.println("Current Weather Information Unavailable.");
        }
    }

    private void getAirPollutionData(WeatherInstance weatherInstance) 
    {
        DisplayAirPollution displayAirPol = new DisplayAirPollution();
        if ( weatherInstance != null)
        {
            System.out.println("Air Pollution Data:");
            System.out.println(displayAirPol.displayAirPol(weatherInstance.getAirPollutionInfo() , weatherInstance.getLocation().getCity()));
            double aqi = weatherInstance.getAirPollutionInfo().getAirQualityIndex();

            // Display Notofication for poor/very poor air quality
            if (aqi == 4 || aqi == 5) {
            System.out.println("Poor air quality detected! AQI: " + aqi);
            }
        } else {
            System.out.println("Air Pollution Information Unavailable.");
        }
    }

    private void getFiveDayForecast(WeatherInstance weatherInstance) {
      
        ArrayList<WeatherInfo> fivedayweather = weatherInstance.getFiveDayWeather();
        Display5DayWeather display5DayWeather = new Display5DayWeather();

        if (fivedayweather != null && !fivedayweather.isEmpty()) 
        {
            System.out.println(display5DayWeather.display5DaysWeather(fivedayweather, weatherInstance.getLocation().getCity()));
        } 
        else 
          System.out.println("Five Day Forecast Unavailable.");
       
    }

    public static void main(String[] args) {
        ConsoleBasedUI consoleUI = new ConsoleBasedUI();
        consoleUI.displayMenu();
    }
    
}
