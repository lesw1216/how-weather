package lesw.howweather.domain;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.Entity.Weather;
import lesw.howweather.domain.weather.NormalRestAPI;
import lesw.howweather.domain.weather.UltraSrtFcstRestAPI;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Component
public class WeatherProvider {
    private final String serviceKey = "lFeu%2Fb%2BZnfLhhQ1gX%2Ban4fh8lxJu2Td4pbDBTAgj5qkEAP28wtzj%2FGLUb%2BWt8v%2BljhjjHpM598E7Qp%2BPsv%2Fr8g%3D%3D";

    /**
     * Get Weather of local
     * @apiNote
     * @param local 현재 지역
     * @return Weather
     */
    public Weather extractLocalWeather(LocalPosition localPosition) {
        Weather weather = new Weather();

        // 초단기 실황 Call Back URL
        String UltraSrtNcstUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String UltraSrtFcsturl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";


        NormalRestAPI restAPI = new NormalRestAPI();
        weather = restAPI.requestAPI(serviceKey, UltraSrtNcstUrl, localPosition, weather);

        UltraSrtFcstRestAPI UltraSrtFcstRestAPI = new UltraSrtFcstRestAPI();
        weather = UltraSrtFcstRestAPI.requestAPI(serviceKey, UltraSrtFcsturl, localPosition, weather);
        return weather;
    }
}
