import javax.swing.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;


class AirPollutionAPI extends API
{
    private static final String API_KEY = "42c9e7c4ab03739b4d29f144eaef927b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s";

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

    public AirPollutionInfo getInfo(String location)
    {
        String weatherData = this.APIcall(location);

        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(weatherData);
        AirPollutionInfo obj = getFormData(jsonObject, weatherData);
        return obj;
    }

    // make weather info object using the JSONObject
    public AirPollutionInfo getFormData(JSONObject jsonObject, String weatherData)
    {
        double AQI = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("aqi");
        double CO = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("co");
        double NO = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("no");
        double NO2 = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("no2");
        double Oz = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("o3");
        double SO2 = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("so2");
        double ammonia = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("nh3");
        double pm2_5 = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("pm2_5");
        double pm10 = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("components").getDouble("pm10");

        AirPollutionInfo obj = new AirPollutionInfo(AQI, CO, NO, NO2, Oz, SO2, ammonia, pm2_5, pm10);
        return obj;
    }

    @Override 
    public void setLatitude(double lati)
    {
        Latitude = lati;
    }

    @Override 
    public void setLongitude(double longi)
    {
        Longitude = longi;
    }

    public void getLatLong(String geoJSON) {
    double lat = 0, longi = 0;
    JSONArray jsonArray = new JSONArray(geoJSON);
    for (int i = 0; i < jsonArray.length(); i++) {
        // Access each object within the array
        JSONObject jsonObject = jsonArray.getJSONObject(i);

        // Remove the inner loop that iterates over keys
        lat = jsonObject.getDouble("lat");
        longi = jsonObject.getDouble("lon");
    }

    this.setLatitude(lat);
    this.setLongitude(longi);
}

}

public class DisplayAirPollution extends JFrame{
    
    public DisplayAirPollution() {
       
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

            // Display elements from API return on GUI
        String DisplayInfoGUI = "Air Pollution data for " + location + "\n"
                                + "AQI: " + Aqi 
                                + "\nCarbon Monoxide: " + co + " μg/m3"
                                + "\nNitrogen Monoxide: " + no + " μg/m3"
                                + "\nNitrogen Dioxide: " + no2 + " μg/m3"
                                + "\nOzone: " + oz + " μg/m3"
                                + "\nSulfur Dioxide: " + so2 + " μg/m3"
                                + "\nAmmonia: " + ammonia + " μg/m3"
                                + "\nCoarse particulate matter: " + pm10 + " μg/m3"
                                + "\nFine particles matter: " + pm2_5 + " μg/m3\n";

        return DisplayInfoGUI;
    }

}
