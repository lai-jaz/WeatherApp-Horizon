import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;  

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class JavafxController{
    // labels
    @FXML
    private TextField locationField;
    @FXML   
    private Button goBtn;
    @FXML
    private Label enteredLoc;
    @FXML
    private Label currentDate;

    // current weather labels
    @FXML
    private Label temp;
    @FXML
    private Label weatherDesc;
    @FXML
    private Label feelsLike;
    @FXML
    private Label visibility;
    @FXML
    private Label humidity;
    @FXML
    private Label rainVol1h;
    @FXML
    private Label rainVol3h;

    // air pollution labels
    @FXML
    private Label aqi;
    @FXML
    private Label co;
    @FXML
    private Label no;
    @FXML
    private Label no2;
    @FXML
    private Label o3;
    @FXML
    private Label so2;
    @FXML
    private Label nh3;
    @FXML
    private Label pm2_5;
    @FXML
    private Label pm10;

    // five day weather labels
    @FXML
    private Label day1;
    @FXML
    private Label day2;
    @FXML
    private Label day3;
    @FXML
    private Label day4;
    @FXML
    private Label day5;
    @FXML
    private HBox fiveDayHBox;

    // other variables
    String location;
    DBManagerInterface dbHandler;
    CacheManagerInterface cachehandler;

    // info
    WeatherInstance weatherInstance;

    public void initialize() {

    }

    public void setDBhandler(DBManagerInterface handler)
    {
        this.dbHandler = handler;
        if (handler instanceof CacheManagerInterface) {
            cachehandler = (CacheManagerInterface) handler;
        }
    }

    public void submitLocation(ActionEvent e) // on button click
    {
        location = locationField.getText();
        DateTime date = new DateTime();
        Location locationObj = new Location(1.0, 1.0, location);

        updateWeatherInstance(locationObj, date);
        setDateAndLocation(location);
        setLabels();
        setFiveDayLabels(weatherInstance.getFiveDayWeather());

    }

    private void updateWeatherInstance(Location locationObj, DateTime date)
    {
        String location = locationObj.getCity();

        WeatherInfo weatherObj = cachehandler.checkTopValueWeather(date.getDate(), location);
        AirPollutionInfo airPolObj = cachehandler.checkTopValueAirPol(date.getDate(), location);
        ArrayList<WeatherInfo> fiveDayObj = cachehandler.checkTopValueFiveDay(date.getDate(), location);
        
        WeatherAPI weather_api = new WeatherAPI();
        AirPollutionAPI airPollution_api = new AirPollutionAPI();
        FiveDayAPI fiveday_api = new FiveDayAPI();
        SunInfo suninfo = weather_api.getSunTimes(location);

        // API CALL if the object returned from cache managing functions is NULL 
        if(weatherObj == null)
        {
            weatherObj = weather_api.getInfo(location);
            airPolObj = airPollution_api.getInfo(location);
            fiveDayObj = fiveday_api.getInfo(location);

            weatherInstance = new WeatherInstance(date, locationObj, weatherObj, suninfo, airPolObj, fiveDayObj);

            dbHandler.saveInfo(weatherInstance);
        }
        else
        {
            weatherInstance = new WeatherInstance(date, locationObj, weatherObj, suninfo, airPolObj, fiveDayObj);
        }

    }

    // Set labels in the Scene
    private void setDateAndLocation(String location) {
        enteredLoc.setText(location);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM YYYY ");

        currentDate.setText(today.format(formatter));
    }

    private void setLabels()
    {
        WeatherInfo weatherObj = weatherInstance.getWeatherInfo();
        AirPollutionInfo airpolObj = weatherInstance.getAirPollutionInfo();

        weatherDesc.setText(weatherObj.getWeatherDescr());
        temp.setText(checkNA(weatherObj.getTemperature()) + "°C");
        feelsLike.setText(checkNA(weatherObj.getFeelsLike()) + "°C");
        visibility.setText(checkNA(weatherObj.getVisibility()) + "m");
        humidity.setText(checkNA(weatherObj.getHumidity()) + "%");
        rainVol1h.setText(checkNA(weatherObj.getRainVol1h()) + "mm");
        rainVol3h.setText(checkNA(weatherObj.getRainVol3h()) + "mm");
        
        aqi.setText(checkNA(airpolObj.getAirQualityIndex()));
        co.setText(checkNA(airpolObj.getCO()) + "μg");
        no.setText(checkNA(airpolObj.getNO()) + "μg");
        no2.setText(checkNA(airpolObj.getNO2()) + "μg");
        o3.setText(checkNA(airpolObj.getOzone()) + "μg");
        so2.setText(checkNA(airpolObj.getSO2()) + "μg");
        nh3.setText(checkNA(airpolObj.getAmmonia()) + "μg");
        pm2_5.setText(checkNA(airpolObj.getpm2_5()) + "μg");
        pm10.setText(checkNA(airpolObj.getpm10()) + "μg");
        
    }

    private void setFiveDayLabels(ArrayList<WeatherInfo> fiveDayWeather)
    {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        day1.setText(today.plusDays(1).format(formatter));
        day2.setText(today.plusDays(2).format(formatter));
        day3.setText(today.plusDays(3).format(formatter));
        day4.setText(today.plusDays(4).format(formatter));
        day5.setText(today.plusDays(5).format(formatter));

        for(int i=0; i<5; i++)
        {
            VBox vboxDay = (VBox) fiveDayHBox.getChildren().get(i);
            vboxDay.getChildren().clear();

            ArrayList<String> dayInfo = new ArrayList<>();
            WeatherInfo oneDay = fiveDayWeather.get(i);
            
            dayInfo.add(checkNA(oneDay.getTemperature()) + "°C");
            dayInfo.add(checkNA(oneDay.getFeelsLike()) + "°C");
            dayInfo.add(checkNA(oneDay.getWeatherDescr()));
            dayInfo.add(checkNA(oneDay.getHumidity()) + "%");
            dayInfo.add(checkNA(oneDay.getVisibility()) + "m");
            dayInfo.add(checkNA(oneDay.getRainVol1h()) + "mm");
            dayInfo.add(checkNA(oneDay.getRainVol3h()) + "mm");

            for (String labelText : dayInfo) 
            {
                Label info = new Label(labelText);
                VBox.setVgrow(info, javafx.scene.layout.Priority.ALWAYS);
                info.setAlignment(Pos.CENTER);
                info.setMaxWidth(Double.MAX_VALUE);

                vboxDay.getChildren().add(info);
            }

            
        }

    }

    // Check if the value is not available
    private String checkNA(double value)
    {
        if(value == -1)
        {
            return "N/A";
        }
        else
            return String.valueOf(value);
    }

    private String checkNA(int value)
    {
        if(value == -1)
        {
            return "N/A";
        }
        else
            return String.valueOf(value);
    }

    private String checkNA(String value)
    {
        if(value=="")
        {
            return "N/A";
        }
        else
            return String.valueOf(value);
    }

}
