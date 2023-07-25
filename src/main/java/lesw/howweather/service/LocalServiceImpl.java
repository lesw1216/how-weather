package lesw.howweather.service;

import lesw.howweather.Entity.LocalPosition;
import lesw.howweather.domain.CsvReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LocalServiceImpl {

    private final CsvReader csvReader;

    @Autowired
    public LocalServiceImpl(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public Optional<LocalPosition> extractLocalPosition(String city, String district, String dong) {
        List<List<String>> localLists = csvReader.readCSVFile("static/local.csv");

        for(List<String> list : localLists) {
            if (list.get(0).equals(city)) {
                if(list.get(1).equals(district)) {
                    if(list.get(2).equals(dong)) {
                        return Optional.of(new LocalPosition(list.get(3), list.get(4)));
                    }
                }
            }
        }

        return Optional.empty();
    }
}
