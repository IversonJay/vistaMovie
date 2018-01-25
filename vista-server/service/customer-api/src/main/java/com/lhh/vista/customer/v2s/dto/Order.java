package com.lhh.vista.customer.v2s.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by liu on 2017/1/5.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @JsonProperty(value = "BookingFeeValueCents")
    private int servicePrice;

    @JsonProperty(value = "CinemaId")
    private String cinemaId;
    @JsonProperty(value = "TotalOrderCount")
    private int totalOrderCount;

    @JsonProperty(value = "TotalValueCents")
    private int totalPrice;

    @JsonProperty(value = "Concessions")
    private List<OrderConcession> concessions;
}
