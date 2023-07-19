package lesw.howweather.domain;

import lesw.howweather.Entity.Weather;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WeatherProvider {
    private final String serviceKey = "lFeu%2Fb%2BZnfLhhQ1gX%2Ban4fh8lxJu2Td4pbDBTAgj5qkEAP28wtzj%2FGLUb%2BWt8v%2BljhjjHpM598E7Qp%2BPsv%2Fr8g%3D%3D";
    private final String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private URL extractURL() {
        String urlBuilder = url + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + serviceKey + /*Service Key*/
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지번호*/
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("10", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*요청자료형식(XML/JSON) Default: XML*/
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("20230718", StandardCharsets.UTF_8) + /*‘21년 6월 28일발표*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("0500", StandardCharsets.UTF_8) + /*05시 발표*/
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("55", StandardCharsets.UTF_8) + /*예보지점의 X 좌표값*/
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("127", StandardCharsets.UTF_8); /*예보지점의 Y 좌표값*/
        try {
            return new URL(urlBuilder);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String requestData(URL requestUrl) {
        try {
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
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

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather parserJson(String jsonResult) {
        JSONParser parser = new JSONParser();
        Object parseObj = null;
        try {
            parseObj = parser.parse(jsonResult);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObj = (JSONObject) parseObj;
        Object responseObj = jsonObj.get("response");
        log.info(responseObj.toString());

        JSONObject responseJson = (JSONObject) responseObj;
        Object bodyObj = responseJson.get("body");
        log.info(bodyObj.toString());

        JSONObject bodyJson = (JSONObject) bodyObj;
        Object itemsObj = bodyJson.get("items");
        log.info(itemsObj.toString());

        JSONObject itemsJson = (JSONObject) itemsObj;
        JSONArray itemArr = (JSONArray) itemsJson.get("item");

        Weather weather = new Weather();
        if (itemArr.size() > 0) {
            for (Object itemObj : itemArr) {
                log.info(itemObj.toString());

                JSONObject itemJson = (JSONObject) itemObj;
                log.info(itemJson.get("category").toString());
                if (itemJson.get("category").toString().equals("TMP")) {
                    weather.setTmp(itemJson.get("fcstValue").toString());
                }
            }
        }

        return weather;
    }

    public Weather extractLocalWeather() {
        URL extractURL = extractURL();
        String resultJson = requestData(extractURL);
        return parserJson(resultJson);
    }
}
