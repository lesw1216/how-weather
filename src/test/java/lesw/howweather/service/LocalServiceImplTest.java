package lesw.howweather.service;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.domain.CsvReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@Slf4j
class LocalServiceImplTest {

    CsvReader readCSVFile = new CsvReader();
    LocalServiceImpl localService = new LocalServiceImpl(readCSVFile);

    @Test
    void readCsv() {
        List<List<String>> lists = readCSVFile.readCSVFile("/csv/local.csv");

        for (List<String> list : lists) {
            log.info(list.toString());
        }
    }

    @Test
    void service() {
        Optional<LocalPosition> localPosition =
                localService.extractLocalPosition("인천광역시", "부평구", "삼산2동");

        if(localPosition.isEmpty()) {
            log.info("비어 있음");
        } else {
            LocalPosition position = localPosition.get();
            log.info(position.toString());
        }
    }
}