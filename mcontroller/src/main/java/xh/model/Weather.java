package xh.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xiehuang
 * @date 2022/05/07 1:52
 */
@Data
public class Weather {
    private String city;
    @JsonProperty("cityid")
    private String cityId;
    private float temp;
    @JsonProperty("WD")
    private String wd;
    @JsonProperty("WS")
    private String ws;
    @JsonProperty("SD")
    private String sd;
    @JsonProperty("AP")
    private String ap;
    private String njd;
    @JsonProperty("WSE")
    private String wse;
    private String time;
    private float sm;
    private int isRadar;
    @JsonProperty("Radar")
    private String radar;
}
