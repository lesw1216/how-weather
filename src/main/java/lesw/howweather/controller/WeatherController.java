package lesw.howweather.controller;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WeatherController {

    @GetMapping("/weather")
    public ResponseEntity<Weather> GetLocalWeather() {
        log.info("GetLocalWeather call");
        return new ResponseEntity<Weather>(new Weather(), HttpStatus.OK);
    }
}
