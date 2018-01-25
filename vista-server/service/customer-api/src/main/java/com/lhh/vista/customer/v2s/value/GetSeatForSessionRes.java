package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by soap on 2016/12/13.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetSeatForSessionRes {
    @JsonProperty(value = "ResponseCode")
    private Integer responseCode;

    @JsonProperty(value = "SeatLayoutData")
    private SeatLayoutData seatLayoutData;
}