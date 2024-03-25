//to read user input from the console
import java.util.Scanner;
import java.util.ArrayList;

public class ConsoleBasedUI implements HomepageInfo {
    private Scanner scanner;
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
        weatherAPI = new WeatherAPI();
        airPollutionAPI = new AirPollutionAPI();
        fiveDayAPI = new FiveDayAPI();
    }

    public void displayMenu()
    {
        boolean valid = true;
        while (valid)
         {
            System.out.println("--- Weather App ---");
            System.out.println("--- MENU ---");
            System.out.println("1) Get Current Weather");
            System.out.println("2) Get Air Pollution Data");
            System.out.println("3) Get 5-Day Forecast");
            System.out.println("4) Exit");
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
                case 1:
                    getCurrentWeather();
                    break;
                case 2:
                    getAirPollutionData();
                    break;
                case 3:
                    getFiveDayForecast();
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
    
    private void getCurrentWeather() {
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        WeatherInfo weatherInfo = weatherAPI.getInfo(location);
        DisplayWeather displayWeather = new DisplayWeather();
        if (weatherInfo != null) {
            System.out.println("Current Weather:");
            System.out.println(displayWeather.displayWeather(weatherInfo, location));
        } 
        else
         {
            System.out.println("Current Weather Information Unavailable. Your loss :)");
        }
    }

    private void getAirPollutionData() {
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        AirPollutionInfo airPollutionInfo = airPollutionAPI.getInfo(location);
        DisplayAirPollution displayAirPol = new DisplayAirPollution();
        if (airPollutionInfo != null)
         {
            System.out.println("Air Pollution Data:");
            System.out.println(displayAirPol.displayAirPol(airPollutionInfo, location));
        } else {
            System.out.println("Air Pollution Information Unavailable. You should know your country!");
        }
    }

    private void getFiveDayForecast() {
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.println("5-Day Forecast:");
        ArrayList<WeatherInfo> fivedayweather = fiveDayAPI.getInfo(location);
        Display5DayWeather display5DayWeather = new Display5DayWeather();

        if (fivedayweather  != null && !fivedayweather .isEmpty()) {
            System.out.println(display5DayWeather.display5DaysWeather(fivedayweather, location));
        
        } else 
    
        System.out.println("Five Day Forecast Unavailable.");
       
    }

    public static void main(String[] args) {
        ConsoleBasedUI consoleUI = new ConsoleBasedUI();
        consoleUI.displayMenu();
    }
    
}
