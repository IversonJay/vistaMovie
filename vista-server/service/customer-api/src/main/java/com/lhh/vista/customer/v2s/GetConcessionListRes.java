package com.lhh.vista.customer.v2s;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by liu on 2017/1/1.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class GetConcessionListRes extends StateResult {
    @JsonProperty(value = "ConcessionItems")
    private List<Concession> items;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ToString
    public static class Concession {
        @JsonProperty(value = "Id")
        private String id;
        @JsonProperty(value = "HOPK")
        private String HOPK;
        @JsonProperty(value = "Description")
        private String description;
        @JsonProperty(value = "PriceInCents")
        private Integer price;
        @JsonProperty(value = "HeadOfficeItemCode")
        private String headOfficeItemCode;//这个参数给抽奖用的
        @JsonProperty(value = "RecognitionSequenceNumber")
        private Integer recognitionSequenceNumber;//这个参数给抽奖用的

    }
}
