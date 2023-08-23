package lesw.howweather.domain.weather;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalTime;

@Slf4j
public class UltraSrtFcstRestAPI extends NormalRestAPI{

    @Override
    protected String extractTime() {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();

        if (hour > 0 && minute < 30)
            hour -= 1;
        else if (hour == 0 && minute < 30)
            hour = 23;


        if (hour < 10)
            return "0" + hour + "30";

        return hour + "30";
    }

    @Override
    protected Weather getValue(JSONArray itemArr) {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        if (minute >= 30)
            hour += 1;

        String stringTime = String.valueOf(hour);

        if (hour < 10)
            stringTime = "0" + hour;
        stringTime += "00";

        log.info("stringTime = " + stringTime);

        if (!itemArr.isEmpty()) {
            for (Object itemObj : itemArr) {
                log.info("itemArr = " + itemObj.toString());

                JSONObject itemJson = (JSONObject) itemObj;

                String category = itemJson.get("category").toString();
                String fcstTime = itemJson.get("fcstTime").toString();
                String fcstValue = itemJson.get("fcstValue").toString();

                if (category.equals("SKY") && fcstTime.equals(stringTime)) {
                    setWeather.setSKY(fcstValue);
                    switch (fcstValue) {
                        case "1":
                            setWeather.setSKY("맑음");
                            break;
                        case "3":
                            setWeather.setSKY("구름 많음");
                            break;
                        case "4":
                            setWeather.setSKY("흐림");
                            break;
                    }
                }
            }
        }

        log.info(setWeather.toString());
        return setWeather;
    }
}
