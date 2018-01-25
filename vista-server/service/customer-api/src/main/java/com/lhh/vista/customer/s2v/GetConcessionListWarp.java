package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2017/1/1.
 */
@Setter
@Getter
public class GetConcessionListWarp {
    @JsonProperty(value = "UserSessionId")
    private String sessionId;
    @JsonProperty(value = "CinemaId")
    private String cinemaId ;

    @JsonProperty(value = "OptionalClientClass")
    private String clientClass;
    @JsonProperty(value = "OptionalClientId")
    private String clientId;
    @JsonProperty(value = "OptionalClientName")
    private String clientName;
}
