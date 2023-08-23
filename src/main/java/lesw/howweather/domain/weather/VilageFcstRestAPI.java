package lesw.howweather.domain.weather;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    protected Weather getValue(JSONArray itemArr) {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();

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

                if (category.equals("POP") && fcstTime.equals(stringTime)) {
                    setWeather.setPOP(fcstValue);
                    break;
                }
            }
        }

        log.info(setWeather.toString());
        return setWeather;
    }

    public Weather getTmnRestAPI(String serviceKey, String restUrl, LocalPosition userPosition, Weather weather) {
        String date = extractDate();
        int hour = LocalTime.now().getHour();
        String strTime = "";
        if(hour < 2) {
            strTime = "2300";
            LocalDate localDate = LocalDate.now().minusDays(1);
            String[] split = localDate.toString().split("-");
            date = split[0] + split[1] + split[2];
        }
        else
            strTime = "0200";


        String urlBuilder = restUrl +
                "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + serviceKey + /*Service Key*/
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지번호*/
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*요청자료형식(XML/JSON) Default: XML*/
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + /*‘21년 6월 28일발표*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(strTime, StandardCharsets.UTF_8) + /*05시 발표*/
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

            return getTmnValue(parserJson(sb.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather getTmnValue(JSONArray itemArr) {

        if (!itemArr.isEmpty()) {
            for (Object itemObj : itemArr) {
                log.info("itemArr = " + itemObj.toString());

                JSONObject itemJson = (JSONObject) itemObj;

                String category = itemJson.get("category").toString();
                String fcstTime = itemJson.get("fcstTime").toString();
                String fcstValue = itemJson.get("fcstValue").toString();

                if (category.equals("TMN") && fcstTime.equals("0600")) {
                    setWeather.setTMN(fcstValue);
                    break;
                }
            }
        }

        log.info(setWeather.toString());
        return setWeather;
    }

    public Weather getTmxRestAPI(String serviceKey, String restUrl, LocalPosition userPosition, Weather weather) {
        String date = extractDate();
        int hour = LocalTime.now().getHour();
        String strTime = "";
        if(hour < 2) {
            strTime = "2300";
            LocalDate localDate = LocalDate.now().minusDays(1);
            String[] split = localDate.toString().split("-");
            date = split[0] + split[1] + split[2];
        }
        else if(hour < 5)
            strTime = "0200";
        else if(hour < 8)
            strTime = "0500";
        else if(hour < 11)
            strTime = "0800";
        else
            strTime = "1100";

        String urlBuilder = restUrl +
                "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + serviceKey + /*Service Key*/
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지번호*/
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*요청자료형식(XML/JSON) Default: XML*/
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + /*‘21년 6월 28일발표*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(strTime, StandardCharsets.UTF_8) + /*05시 발표*/
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

            return getTmxValue(parserJson(sb.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather getTmxValue(JSONArray itemArr) {

        if (!itemArr.isEmpty()) {
            for (Object itemObj : itemArr) {
                log.info("itemArr = " + itemObj.toString());

                JSONObject itemJson = (JSONObject) itemObj;

                String category = itemJson.get("category").toString();
                String fcstTime = itemJson.get("fcstTime").toString();
                String fcstValue = itemJson.get("fcstValue").toString();

                if (category.equals("TMX") && fcstTime.equals("1500")) {
                    setWeather.setTMX(fcstValue);
                    break;
                }
            }
        }

        log.info(setWeather.toString());
        return setWeather;
    }
}
