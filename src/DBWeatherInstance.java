import java.sql.*;
import java.util.ArrayList;

public class DBWeatherInstance implements DBManagerInterface {
    
    private Connection con;

    public DBWeatherInstance(String url, String username, String password) throws SQLException {
        this.con = DriverManager.getConnection(url, username, password);
    }

    public void closeConnection() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    public void saveInfo(WeatherInstance obj)
    {
        WeatherInfo weatherinfo = obj.getWeatherInfo();
        AirPollutionInfo airpolinfo = obj.getAirPollutionInfo();
        ArrayList<WeatherInfo> fivedayweather = obj.getFiveDayWeather();
        Location location = obj.getLocation();

        DateTime datetime = obj.getDate();

        String SQLCurrentWeather = "INSERT INTO CurrentWeather (Gen_Time, Location, Visibility, Humidity, FeelsLike, WeatherDesc, RainVol1H, RainVol3H)"+
                                 "Values ('"+ datetime.getTime() +"', '"+ location.getCity() +"',"+ weatherinfo.getVisibility() +","+ weatherinfo.getHumidity()+","+ weatherinfo.getFeelsLike()+", '"+weatherinfo.getWeatherDescr()+"',"+ weatherinfo.getRainVol1h()+ ", "+ weatherinfo.getRainVol3h()+ ") ";
        
        for(int i=0;i<5;i++)
        {
            WeatherInfo day_i = fivedayweather.get(i);
            String SQLFiveDay = "INSERT INTO FiveDayWeather (Gen_Time, Gen_Day, Location, Visibility, Humidity, FeelsLike, WeatherDesc, RainVol1H, RainVol3H)"+ 
            "VALUES ('"+ datetime.getTime() +"',"+ i+1 + ", '"+ location.getCity() +"',"+ day_i.getVisibility() +","+ day_i.getHumidity()+","+ day_i.getFeelsLike()+", '"+day_i.getWeatherDescr()+"',"+ day_i.getRainVol1h()+ ", "+ day_i.getRainVol3h()+ ")";
            
            try{
                Statement stmt = con.createStatement();
                int rowsAffected = stmt.executeUpdate(SQLFiveDay);
                System.out.println(rowsAffected + " row(s) affected.");
            }
            catch(SQLException e)
            {
                System.out.println("Could not insert Five Day weather for Day " + i);
            }
        }
       
        String SQLAirPollution="INSERT INTO AirPollution (Gen_Time, Location, AQI, CarbonMonoxide, NitrogenMonoxide, NitrogenDioxide, Ozone, SulfurDioxide, Ammonia, CoarsePartMatter, FinePartMatter)"+
        "VALUES ('"+ datetime.getTime() +"', '"+ location.getCity() +"' ,"+ airpolinfo.getAirQualityIndex() +" ,"+ airpolinfo.getCO()+" ,"+ airpolinfo.getNO()+ " ,"+airpolinfo.getNO2() + " ,"+ airpolinfo.getOzone()+ " ,"+ airpolinfo.getSO2()+ " ,"+ airpolinfo.getAmmonia()+" , "+ airpolinfo.getpm2_5()+" ,"+ airpolinfo.getpm10()+ ")";
        
        try{
            Statement stmt = con.createStatement();
            int rowsAffected = stmt.executeUpdate(SQLCurrentWeather);
            System.out.println(rowsAffected + " row(s) affected.");
        }
        catch(SQLException e)
        {
            System.out.println("Could not insert Weather");
        }

        try{
            Statement stmt = con.createStatement();
            int rowsAffected = stmt.executeUpdate(SQLAirPollution);
            System.out.println(rowsAffected + " row(s) affected.");
        }
        catch(SQLException e)
        {
            System.out.println("Could not insert Air Pollution");
        }
         
        
        // ResultSet rs = stmt.executeQuery(SQLCurrentWeather);
        // while(rs.next())
        // {
        //     System.out.println(connString);
        // }"
    }
    

    public WeatherInstance getInfo()
    {
        WeatherInstance OBJ = new WeatherInstance();
        return OBJ;
    }

    public void settimestamp()
    {

    }
}
