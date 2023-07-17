package lesw.howweather.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class WeatherController {

    @GetMapping("/weather")
    public String GetLocalWeather() {
        log.info("GetLocalWeather call");
        return "/home";
    }
}
