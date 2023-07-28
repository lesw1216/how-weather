package lesw.howweather.Entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Weather {
    private String TMP;
    private String REH;
    private String SKY;
    private String POP;
}
