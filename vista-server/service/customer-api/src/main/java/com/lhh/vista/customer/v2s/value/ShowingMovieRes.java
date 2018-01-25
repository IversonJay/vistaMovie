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
public class ShowingMovieRes {
    private List<Value> value;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        //内部ID
        @JsonProperty(value = "ScheduledFilmId")
        private String scheduledFilmId;
        @JsonProperty(value = "Sessions")
        private List<Session> sessions;

        @Override
        public String toString() {
            return "Value{" +
                    "scheduledFilmId='" + scheduledFilmId + '\'' +
                    ", sessions=" + sessions +
                    '}';
        }
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Session {
        @JsonProperty(value = "Attributes")
        private List<Attributes> attributes;
        @JsonProperty(value = "SessionId")
        private String sessionId;
        @JsonProperty(value = "Showtime")
        private String showtime;
        @JsonProperty(value = "ScreenName")
        private String screenName;
        @JsonProperty(value = "CinemaId")
        private String cinemaId;
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attributes {
        @JsonProperty(value = "Description")
        private String description;
    }
}
