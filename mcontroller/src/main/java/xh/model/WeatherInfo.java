package xh.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xiehuang
 * @date 2022/05/06 3:36
 */
@Data
public class WeatherInfo {
    @JsonProperty("weatherinfo")
    private Weather weather;
}
