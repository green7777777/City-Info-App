package web1;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {
    private String countryShortName;

    public Service(String countryFullName) {
        Map<String, String> countries = new HashMap<>();
        Locale locale = new Locale("en-us", "uk");
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("en-us", iso);
            countries.put(l.getDisplayCountry(locale), iso);
        }
        this.countryShortName = countries.get(countryFullName);
    }

    public String getWeather(String cityName) {
        StringBuilder weatherJson = new StringBuilder();
        String weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?";
        try {
            URL url = new URL(weatherApiUrl + "q=" + cityName + "," + countryShortName + "&appid=" + "" + "&units=metric"); //In order to work there should be an api token in empty brackets, deleted for safety reasons
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) weatherJson.append(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherJson.toString();
    }

    public double getRateFor(String currencyName) {
        StringBuilder currencyJson = new StringBuilder();
        String currencyApiUrl = "https://api.exchangerate.host/latest?base=";
        try {
            URL url = new URL(currencyApiUrl + Currency.getInstance(new Locale("en-us", countryShortName)).getCurrencyCode() + "&symbols=" + currencyName);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) currencyJson.append(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(currencyJson.toString());
        return json.getJSONObject("rates").getDouble(currencyName);
    }

    public double getNBPRate() {
        StringBuilder currencyJson = new StringBuilder();
        if (Currency.getInstance(new Locale("en-us", countryShortName)).getCurrencyCode().equals("PLN")) return 1.0;
        try {
            currencyJson = getNBPRateJson("A");
        } catch (IOException e1) {
            try {
                currencyJson = getNBPRateJson("B");
            } catch (IOException e2) {
                try {
                    currencyJson = getNBPRateJson("C");
                } catch (IOException e3) {
                    System.out.println("Taka waluta nie istnieje");
                }
            }
        }
        JSONObject json = (JSONObject) new JSONObject(currencyJson.toString()).getJSONArray("rates").get(0);
        return json.getDouble("mid");
    }

    private StringBuilder getNBPRateJson(String table) throws IOException {
        StringBuilder currencyJson = new StringBuilder();
        String currencyApiUrl = "http://api.nbp.pl/api/exchangerates/rates";
        URL url = new URL(currencyApiUrl + "/" + table + "/" + Currency.getInstance(new Locale("en-us", countryShortName)).getCurrencyCode() + "?format=json");
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String tmp;
        while ((tmp = bufferedReader.readLine()) != null) currencyJson.append(tmp);
        return currencyJson;
    }

    public String getCountryShortName() {
        return countryShortName;
    }
}
