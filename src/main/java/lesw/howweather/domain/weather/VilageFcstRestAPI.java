package lesw.howweather.domain.weather;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Slf4j
public class VilageFcstRestAPI extends NormalRestAPI{
    @Override
    protected String extractTime() {
        int hour = LocalTime.now().getHour();
        if (hour <= 2)
            hour = 23;
        else if(hour <= 5)
            hour = 2;
        else if(hour <= 8)
            hour = 5;
        else if(hour <= 11)
            hour = 8;
        else if(hour <= 14)
            hour = 11;
        else if(hour <= 17)
            hour = 14;
        else if(hour <= 20)
            hour = 17;
        else hour = 20;

        String stringHour = String.valueOf(hour);

        if (hour < 10)
            stringHour = "0" + hour;
        stringHour += "00";
        return stringHour;
    }

    @Override
    protected Weather parserJson(String jsonResult) {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();

        String stringTime = String.valueOf(hour);

        if (hour < 10)
            stringTime = "0" + hour;
        stringTime += "00";

        log.info("stringTime = " + stringTime);

        JSONParser parser = new JSONParser();
        Object parseObj = null;
        try {
            parseObj = parser.parse(jsonResult);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Object -> JSONObject
        JSONObject jsonObj = (JSONObject) parseObj;

        // response 객체 꺼내기, Object Type
        Object responseObj = jsonObj.get("response");
        log.info("response = " + responseObj.toString());
        // =======================================================

        // 꺼낸 Object 타입의 responseObj를 JSONObject로 변환
        JSONObject responseJson = (JSONObject) responseObj;

        // body 객체 꺼내기, Object Type
        Object bodyObj = responseJson.get("body");
        log.info("body = " + bodyObj.toString());
        // ========================================================

        // 꺼낸 Object 타입의 bodyObj를 JSONObject로 변환
        JSONObject bodyJson = (JSONObject) bodyObj;

        // bodyJson에서 Items 객체 JSON 꺼내기
        Object itemsObj = bodyJson.get("items");
        log.info("items = " + itemsObj.toString());
        // =========================================================

        // 꺼낸 Object 타입의 itemsObj를 JSONObject로 변환
        JSONObject itemsJson = (JSONObject) itemsObj;
        // item 배열을 꺼내기 위해 JSONArray로 변환
        JSONArray itemArr = (JSONArray) itemsJson.get("item");
        log.info("item = " + itemArr.toString());

        if (!itemArr.isEmpty()) {
            for (Object itemObj : itemArr) {
                log.info("itemArr = " + itemObj.toString());

                JSONObject itemJson = (JSONObject) itemObj;

                String category = itemJson.get("category").toString();
                String fcstTime = itemJson.get("fcstTime").toString();
                String fcstValue = itemJson.get("fcstValue").toString();

                if (category.equals("POP") && fcstTime.equals(stringTime)) {
                    log.info("강수확률: " + fcstValue);
                }
            }
        }

        log.info(setWeather.toString());
        return setWeather;
    }
}
