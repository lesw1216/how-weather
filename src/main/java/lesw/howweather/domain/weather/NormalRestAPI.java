package lesw.howweather.domain.weather;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
public class NormalRestAPI {

    protected Weather setWeather;

    public Weather requestAPI(String serviceKey, String restUrl, LocalPosition userPosition, Weather weather) {
        setWeather = weather;
        String date = extractDate();
        String time = extractTime();
        log.info("date = " + date);
        log.info("time = " + time);

        String urlBuilder = restUrl +
                "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + serviceKey + /*Service Key*/
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지번호*/
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("60", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*요청자료형식(XML/JSON) Default: XML*/
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + /*‘21년 6월 28일발표*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(time, StandardCharsets.UTF_8) + /*05시 발표*/
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(userPosition.getX(), StandardCharsets.UTF_8) + /*예보지점의 X 좌표값*/
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(userPosition.getY(), StandardCharsets.UTF_8); /*예보지점의 Y 좌표값*/
        try {
            URL url = new URL(urlBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            int responseCode = conn.getResponseCode();
            BufferedReader rd;

            log.info("Response Code : " + responseCode);
            if (responseCode >= 200 && responseCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            return parserJson(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String extractTime() {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();

        if (minute < 30)
            hour -= 1;

        if (hour < 10)
            return "0" + hour + "00";

        return hour + "00";
    }
    protected String extractDate() {
        String now = LocalDate.now().toString();
        String[] nowList = now.split("-"); // 2023-07-21, '-' slice
        return nowList[0] + nowList[1] + nowList[2]; // 20230721
    }

    protected Weather parserJson(String jsonResult) {
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

        if (itemArr.size() > 0) {
            for (Object itemObj : itemArr) {
                log.info("itemArr = " + itemObj.toString());

                JSONObject itemJson = (JSONObject) itemObj;

                String category = itemJson.get("category").toString();

                switch (category) {
                    case "T1H":
                        setWeather.setTMP(itemJson.get("obsrValue").toString());
                        break;
                    case "REH":
                        setWeather.setREH(itemJson.get("obsrValue").toString());
                        break;
                }
            }
        }

        return setWeather;
    }
}
