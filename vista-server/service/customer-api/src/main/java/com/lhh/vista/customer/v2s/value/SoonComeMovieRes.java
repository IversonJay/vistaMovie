package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by liu on 2016/12/1.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoonComeMovieRes {
    private List<Value> value;

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        //内部ID
        @JsonProperty(value = "ScheduledFilmId")
        private String scheduledFilmId;
    }
}
