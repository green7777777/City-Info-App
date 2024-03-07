package web1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONObject;

import java.util.Currency;
import java.util.Locale;

public class Controller {
    public Text TTemperatureMin;
    public Text TTemperatureAvg;
    public Text TTemperatureMax;
    public Text TConditions;
    public Text THumidity;
    public Text TPressure;
    public Button BSearch;
    public TextField TFCountryName;
    public TextField TFCityName;
    public TextField TFCurrencyCode;
    public Text TContryToCurrency;
    public Text TContryToCurrencyRate;
    public Text TContryToPLN;
    public Text TContryToPLNRate;
    public WebView WV;

    private Service service;
    private String cityName;
    private String currencyCode;

    @FXML
    public void BSearchAction() {
        this.service = new Service(TFCountryName.getText());
        this.cityName = TFCityName.getText();
        this.currencyCode = TFCurrencyCode.getText();
        System.out.println(TFCountryName.getText() + "\t\t" + TFCityName.getText() + "\t\t" + TFCurrencyCode.getText());
        TContryToCurrency.setText(Currency.getInstance(new Locale("en-us", service.getCountryShortName())).getCurrencyCode() + " to " + currencyCode);
        TContryToCurrencyRate.setText("1 : " + service.getRateFor(currencyCode));
        TContryToPLN.setText(Currency.getInstance(new Locale("en-us", service.getCountryShortName())).getCurrencyCode() + " to PLN");
        TContryToPLNRate.setText("1 : " + service.getNBPRate());

        JSONObject weather = new JSONObject(service.getWeather(cityName));
        TTemperatureMin.setText("Min: " + weather.getJSONObject("main").getDouble("temp_min") + " C");
        TTemperatureAvg.setText("Average: " + weather.getJSONObject("main").getDouble("temp") + " C");
        TTemperatureMax.setText("Max: " + weather.getJSONObject("main").getDouble("temp_max") + " C");
        TConditions.setText("Conditions: " + new JSONObject(weather.getJSONArray("weather").get(0).toString()).getString("description"));
        THumidity.setText("Humidity: " + weather.getJSONObject("main").getInt("humidity") + "%");
        TPressure.setText("Pressure: " + weather.getJSONObject("main").getInt("pressure") + "hPa");

        WebEngine webEngine = WV.getEngine();
        webEngine.load("https://en.wikipedia.org/wiki/"+cityName);
    }
}
