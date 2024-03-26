import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DB2WeatherInstance implements DBManagerInterface{
    public void saveInfo(WeatherInstance obj) {
        saveCurrentWeather(obj);
        saveFiveDayWeather(obj);
        // saveAirPollution(obj);
    }

    private void saveCurrentWeather(WeatherInstance obj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("CurrentWeather.txt", true))) {
            String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    obj.getDate(), obj.getLocation(), obj.getWeatherInfo().getVisibility(),
                    obj.getWeatherInfo().getHumidity(), obj.getWeatherInfo().getFeelsLike(),
                    obj.getWeatherInfo().getWeatherDescr(), obj.getWeatherInfo().getRainVol1h(),
                    obj.getWeatherInfo().getRainVol3h());
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFiveDayWeather(WeatherInstance obj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("FiveDayWeather.txt", true))) {
            ArrayList<WeatherInfo> fiveDayInfo = obj.getFiveDayWeather();
            int i = 1;
            for (WeatherInfo info : fiveDayInfo) {
                String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        obj.getDate(), i++, obj.getLocation(), info.getVisibility(),
                        info.getHumidity(), info.getFeelsLike(), info.getWeatherDescr(),
                        info.getRainVol1h(), info.getRainVol3h());
                writer.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAirPollution(WeatherInstance obj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AirPollution.txt", true))) {
            AirPollutionInfo airPollutionInfo = obj.getAirPollutionInfo();
            String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    obj.getDate(), obj.getLocation(), airPollutionInfo.getAirQualityIndex(),
                    airPollutionInfo.getCO(), airPollutionInfo.getNO(),
                    airPollutionInfo.getNO2(), airPollutionInfo.getOzone(),
                    airPollutionInfo.getSO2(), airPollutionInfo.getAmmonia(),
                    airPollutionInfo.getpm10(), airPollutionInfo.getpm2_5());
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WeatherInstance getInfo(){
        WeatherInstance obj = new WeatherInstance();
        return obj;
    }
    public void settimestamp(){

    }
}
