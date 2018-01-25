package com.lhh.vista.customer.v2s;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/22.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepeatCheckRes{
    @JsonProperty(value = "ResponseCode")
    private Integer state;
    @JsonProperty(value = "UserNameAlreadyInUse")
    private boolean used;
}
