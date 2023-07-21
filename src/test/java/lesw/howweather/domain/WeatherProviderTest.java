package lesw.howweather.domain;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class WeatherProviderTest {

    private final WeatherProvider weatherProvider = new WeatherProvider();

    @Test
    void weatherProvider() {
        String today = "";
        String local = "";
//        Weather weather = weatherProvider.extractLocalWeather();
        Weather weather = weatherProvider.extractLocalWeather(local);
        log.info(weather.toString());

    }
}