import org.json.JSONObject;

interface HomepageInfo
{
    public void displayInfo();
    public void displaySunTimes();
    public void displayLocationForm();
    public WeatherInfo getFormData(JSONObject jsonObject, String weatherData);
}

interface AppHomepage extends HomepageInfo
{}