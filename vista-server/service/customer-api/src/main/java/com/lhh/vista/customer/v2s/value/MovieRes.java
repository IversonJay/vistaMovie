package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by liu on 2016/12/2.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieRes {
    private List<MovieRes.Value> value;

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Value {
        @JsonProperty(value = "ID")
        private String id;
        @JsonProperty(value = "Title")
        private String title;
        @JsonProperty(value = "TitleAlt")
        private String titleAlt;
        @JsonProperty(value = "ShortSynopsis")
        private String shortSynopsis;
        @JsonProperty(value = "RatingDescription")
        private String ratingDescription;
        @JsonProperty(value = "Synopsis")
        private String synopsis;
        @JsonProperty(value = "RunTime")
        private Integer runTime;
        @JsonProperty(value = "OpeningDate")
        private String openingDate;
        @JsonProperty(value = "HOFilmCode")
        private String hOFilmCode;
        @JsonProperty(value = "GenreId")
        private String GenreId;
    }
}
