package lesw.howweather.service;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.Entity.Weather;
import lesw.howweather.domain.WeatherProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherService {


    private final WeatherProvider weatherProvider;

    @Autowired
    public WeatherService(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public Weather extractWeather(LocalPosition localPosition) {
        return weatherProvider.extractLocalWeather(localPosition);
    }
}
