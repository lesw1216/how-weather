package lesw.howweather.domain.weather;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalDate;
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

//    @Override
//    protected String extractDate() {
//        LocalTime localTime = LocalTime.now();
//        int hour = localTime.getHour();
//        int minute = localTime.getMinute();
//
//        log.info("hour = " + hour);
//        log.info("minute = " + minute);
//
//        String date = LocalDate.now().toString();
//        if (hour == 0 && minute < 30) {
//            date = LocalDate.now().minusDays(1).toString();
//        }
//
//        String[] nowList = date.split("-"); // 2023-07-21, '-' slice
//        return nowList[0] + nowList[1] + nowList[2]; // 20230721
//    }

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
