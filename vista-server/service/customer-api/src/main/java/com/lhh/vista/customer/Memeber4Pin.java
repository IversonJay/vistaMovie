package com.lhh.vista.customer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lhh.vista.customer.v2s.MemberValidateRes.Balance;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Memeber4Pin {
    @JsonProperty(value = "Address1")
    private String address;

    @JsonProperty(value = "UserName")
    private String userName;

    @JsonProperty(value = "MemberPassword")
    private String pass;
    @JsonProperty(value = "FirstName")
    private String firstName;

    @JsonProperty(value = "LastName")
    private String lastName;

    @JsonProperty(value = "ClubID")
    private Integer clubID;

    @JsonProperty(value = "ClubName")
    private String clubName;

    @JsonProperty(value = "MemberId")
    private String memberId;

    @JsonProperty(value = "BalanceList")
    private List<Balance> balanceList;

    @JsonProperty(value = "CardNumber")
    private String cardNumber;

    @JsonProperty(value = "CardList")
    private List<String> cardList;

    @JsonProperty(value = "ExpiryDate")
    private String expiryDate;

    @JsonProperty(value = "Pin")
    private String pin;
}