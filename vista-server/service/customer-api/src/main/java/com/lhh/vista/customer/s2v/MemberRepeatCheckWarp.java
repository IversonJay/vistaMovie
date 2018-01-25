package com.lhh.vista.customer.s2v;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
public class MemberRepeatCheckWarp {

    @JsonProperty(value = "UserName")
    private String userName;
    @JsonProperty(value = "ClubID")
    private Integer clubID=1;

}
