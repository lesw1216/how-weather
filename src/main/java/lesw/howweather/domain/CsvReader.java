package lesw.howweather.domain;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvReader {
    public List<List<String>> readCSVFile(String filePath) {
//        Path path = Paths.get(filePath);
//        System.out.println(path.toAbsolutePath().toString());
//        log.info(path.toString());
        List<List<String>> list = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            ClassPathResource resource = new ClassPathResource("static/csv/local.csv");
            InputStream inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while((line = bufferedReader.readLine()) != null) {
                List<String> stringList = new ArrayList<>();
                String[] stringArray = line.split(",");

                stringList = Arrays.asList(stringArray);
                list.add(stringList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
