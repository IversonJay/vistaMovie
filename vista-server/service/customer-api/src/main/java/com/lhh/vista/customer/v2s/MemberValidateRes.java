package com.lhh.vista.customer.v2s;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/21.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberValidateRes extends StateResult {
    @JsonProperty(value = "LoyaltyMember")
    private LoyaltyMember member;

    //11是积分 13是余额
    public float obtainBalance() {
        try {
            for (Balance balance : member.getBalanceList()) {
                if (balance.getTypeID() == 1) {
                    return balance.getRemaining();
                }
            }
        } catch (Exception e) {
        }
        return 0f;
    }

    public float obtainIntegral() {
        try {
            for (Balance balance : member.getBalanceList()) {
                if (balance.getTypeID() == 2) {
                    return balance.getRemaining();
                }
            }
        } catch (Exception e) {
        }
        return 0f;
    }
    
    public int showClubID() {
    	return member.getClubID();
    }
    
    public String showMemberLevelName() {
    	return member.getMemberLevelName();
    }
    
    public String showCardNumber() {
    	return member.getCardNumber();
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoyaltyMember {
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
        
        @JsonProperty(value = "MemberLevelName")
        private String memberLevelName;
        
        @JsonProperty(value = "PreferredComplex")
        private Integer preferredComplex;
        
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Balance {
        @JsonProperty(value = "BalanceTypeID")
        private Integer typeID;

        @JsonProperty(value = "IsDefault")
        private Boolean isDefault;

        @JsonProperty(value = "Name")
        private String name;

        @JsonProperty(value = "PointsRemaining")
        private Float remaining;
    }
}
