package lesw.howweather.controller;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.Entity.Weather;
import lesw.howweather.domain.WeatherProvider;
import lesw.howweather.service.LocalServiceImpl;
import lesw.howweather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class WeatherController {

    private final LocalServiceImpl localService;
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(LocalServiceImpl localService, WeatherService weatherService) {
        this.localService = localService;
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather")
    public ResponseEntity<Weather> GetLocalWeather(@RequestParam("city") String city,
                                                   @RequestParam("district") String district,
                                                   @RequestParam("dong") String dong) {
        log.info("GetLocalWeather call");
        log.info(city + " " + district + " " + dong);
        Optional<LocalPosition> optionalLocalPosition = localService.extractLocalPosition(city, district, dong);
        if(optionalLocalPosition.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Weather extractedWeather = weatherService.extractWeather(optionalLocalPosition.get());

        return new ResponseEntity<>(extractedWeather, HttpStatus.OK);
    }
}
