package lesw.howweather.domain;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WeatherProviderTest {

    private final WeatherProvider weatherProvider = new WeatherProvider();

    @Test
    void weatherProvider() {
        Weather weather = weatherProvider.extractLocalWeather();
        log.info(weather.toString());
    }
}