package lesw.howweather.domain;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class WeatherProviderTest {

    private final WeatherProvider weatherProvider = new WeatherProvider();

    @Test
    void weatherProvider() {
        Weather weather = weatherProvider.extractLocalWeather();
        log.info(weather.toString());
    }
}