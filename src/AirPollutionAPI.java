import javax.swing.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;


public class AirPollutionAPI extends API
{
    private static final String API_KEY = "42c9e7c4ab03739b4d29f144eaef927b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s";
    private double Latitude;
    private double Longitude;


    @Override 
    public String APIcall(String location)
    {
        try {
            String geocode = this.APIGeoCode(location);
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
                return "Connection error. Response code: " + responseCode;
            }
        } 
        catch (Exception e) {
            return "Error: Unable to fetch air pollution data. Exception: " + e.getMessage();
        }
    }

    public String APIGeoCode(String location)
    {
        String API_Geo = "http://api.openweathermap.org/geo/1.0/direct?q=" + location + "&appid=" + API_KEY;
        try {
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
                return "Connection error. Response code: " + responseCode;
            }
        } 
        catch (Exception e) {
            return "Error: Unable to fetch location. Exception: " + e.getMessage();
        }
    }

    public AirPollutionInfo getInfo(String location)
    {
        String weatherData = this.APIcall(location);

        if(weatherData.charAt(0)!='{')
        {
            weatherData = '{' + weatherData + "}";
        }

        JSONObject jsonObject = new JSONObject(weatherData);
        AirPollutionInfo obj = getFormData(jsonObject, weatherData);
        return obj;
    }

    public AirPollutionInfo getFormData(JSONObject jsonObject, String weatherData)
    {
        double AQI = tryExtractDouble(jsonObject, "aqi");
        double CO = tryExtractDouble(jsonObject, "co");
        double NO = tryExtractDouble(jsonObject, "no");
        double NO2 = tryExtractDouble(jsonObject, "no2");
        double Oz = tryExtractDouble(jsonObject, "o3");
        double SO2 = tryExtractDouble(jsonObject, "so2");
        double ammonia = tryExtractDouble(jsonObject, "nh3");
        double pm2_5 = tryExtractDouble(jsonObject, "pm2_5");
        double pm10 = tryExtractDouble(jsonObject, "pm10");

        AirPollutionInfo obj = new AirPollutionInfo(AQI, CO, NO, NO2, Oz, SO2, ammonia, pm2_5, pm10);
        return obj;
    }

    //----------------------------------exception handling for double value
    private double tryExtractDouble(JSONObject jsonObject, String key)
    {
        double defaultvalue = -1.0;
        try {
            return jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble(key);
        } catch (Exception e) {
            return defaultvalue;
        }
        
    }

    public void setLatitude(double lati)
    {
        Latitude = lati;
    }
 
    public void setLongitude(double longi)
    {
        Longitude = longi;
    }

    public void getLatLong(String geoJSON) {
        double lat = 0, longi = 0;
        JSONArray jsonArray = new JSONArray(geoJSON);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            lat = jsonObject.getDouble("lat");
            longi = jsonObject.getDouble("lon");
        }

        this.setLatitude(lat);
        this.setLongitude(longi);
    }

}

class DisplayAirPollution extends JFrame{
    
    public String checkNA(double value)
    {
        if(value == -1)
        {
            return "N/A";
        }
        else
            return String.valueOf(value);
    }

    public String displayAirPol(AirPollutionInfo obj, String location)
    {
        double Aqi = obj.getAirQualityIndex();
        double co = obj.getCO();
        double no =  obj.getNO();
        double no2 = obj.getNO2();
        double oz = obj.getOzone();
        double so2 = obj.getSO2();
        double ammonia = obj.getAmmonia();
        double pm2_5 = obj.getpm2_5();
        double pm10 = obj.getpm10();

        String DisplayInfoGUI = "Air Pollution data for " + location + "\n"
                                + "AQI: " + checkNA(Aqi) 
                                + "\nCarbon Monoxide: " + checkNA(co) + " μg/m3"
                                + "\nNitrogen Monoxide: " + checkNA(no) + " μg/m3"
                                + "\nNitrogen Dioxide: " + checkNA(no2) + " μg/m3"
                                + "\nOzone: " + checkNA(oz) + " μg/m3"
                                + "\nSulfur Dioxide: " + checkNA(so2) + " μg/m3"
                                + "\nAmmonia: " + checkNA(ammonia) + " μg/m3"
                                + "\nCoarse particulate matter: " + checkNA(pm10) + " μg/m3"
                                + "\nFine particles matter: " + checkNA(pm2_5) + " μg/m3\n";

        return DisplayInfoGUI;
    }

}
