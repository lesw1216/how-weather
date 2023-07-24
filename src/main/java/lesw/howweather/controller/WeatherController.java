package lesw.howweather.controller;

import lesw.howweather.Entity.Weather;
import lesw.howweather.domain.WeatherProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WeatherController {

    private final WeatherProvider weatherProvider;

    @Autowired
    public WeatherController(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    @GetMapping("/api/weather")
    public ResponseEntity<Weather> GetLocalWeather(@RequestParam("city") String city,
                                                   @RequestParam("district") String district,
                                                   @RequestParam("dong") String dong) {
        log.info("GetLocalWeather call");
        log.info(city + " " + district + " " + dong);
        Weather extractedWeather = weatherProvider.extractLocalWeather("지역을 입력하세요.");
        return new ResponseEntity<Weather>(extractedWeather, HttpStatus.OK);
    }
}
