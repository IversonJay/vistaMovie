package com.lhh.vista.customer.v2s;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.lhh.vista.common.util.DateTool;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liu on 2017/1/4.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMemberTransactionRes extends StateResult {


    @JsonProperty(value = "MemberTransactionDetails")
    private MemberTransactionDetails memberTransactionDetails;


    public List<ShowItem> obtainYueList() {
        List<ShowItem> items = new ArrayList<>();
        for (GetMemberTransactionRes.Item item : getMemberTransactionDetails().getTransactions()) {
            ShowItem showItem = new ShowItem();
            if (item.getTotalPointsEarnedByBalanceType().size() > 0) {
                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsEarnedByBalanceType()) {
                    if (c.getPoints() != 0 && c.getBalanceTypeId() == 1) {
                        showItem.setS(c.getPoints());
                        showItem.setT(1);
                    }
                }
            }

            if (item.getTotalPointsRedeemedByBalanceType().size() > 0) {
                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsRedeemedByBalanceType()) {
                    if (c.getPoints() != 0 && c.getBalanceTypeId() == 1) {
                        showItem.setS(c.getPoints());
                        showItem.setT(-1);
                    }
                }
            }


            if (item.getTotalPointsBalanceByBalanceType().size() > 0) {
                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsBalanceByBalanceType()) {
                    if (c.getBalanceTypeId() == 1 && showItem.getS() != 0) {
                        showItem.setN(c.getPoints());
                    }
                }
            }
            if (showItem.getS() != 0) {
                showItem.setD(item.getTransactionDate());
                items.add(showItem);
            } else {
                System.out.println(showItem);
                System.out.println(item);
            }
        }
        return items;
    }

    public List<ShowItem> obtainJifenList() {
        List<ShowItem> items = new ArrayList<>();
        for (GetMemberTransactionRes.Item item : getMemberTransactionDetails().getTransactions()) {
            ShowItem showItem = new ShowItem();
            if (item.getTotalPointsEarnedByBalanceType().size() > 0) {
                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsEarnedByBalanceType()) {
                    if (c.getPoints() != 0 && c.getBalanceTypeId() == 2) {
                        showItem.setS(c.getPoints());
                        showItem.setT(1);
                    }
                }
            }

            if (item.getTotalPointsRedeemedByBalanceType().size() > 0) {
                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsRedeemedByBalanceType()) {
                    if (c.getPoints() != 0 && c.getBalanceTypeId() == 2) {
                        showItem.setS(c.getPoints());
                        showItem.setT(-1);
                    }
                }
            }


            if (item.getTotalPointsBalanceByBalanceType().size() > 0) {
                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsBalanceByBalanceType()) {
                    if (c.getBalanceTypeId() == 2 && showItem.getS() != 0) {
                        showItem.setN(c.getPoints());
                    }
                }
            }
            if (showItem.getS() != 0) {
                showItem.setD(item.getTransactionDate());
                items.add(showItem);
            }
        }
        return items;
    }

    @Getter
    @Setter
    public static class ShowItem {
        private Integer s = 0;
        private Integer t = 0;
        private Integer n = 0;

        private String d;

        public ShowItem() {
        }

        @Override
        public String toString() {
            String xx = "";
            if (t > 0) {
                xx = xx + "增加:" + s;
            } else {
                xx = xx + "消耗:" + s;
            }
            xx = xx + "   当前:" + n;
            return xx;
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MemberTransactionDetails {
        @JsonProperty(value = "MemberTransactions")
        private List<Item> transactions;
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        @JsonProperty(value = "CinemaName")
        private String cinemaName;

        //@JsonProperty(value = "ConcessionsValue")
        //private Integer concessionsValue;

        @JsonProperty(value = "LineItems")
        private List<LineItem> lineItems;

        @JsonProperty(value = "TotalPointsBalanceByBalanceType")
        private List<TotalPointsChange> totalPointsBalanceByBalanceType;

        @JsonProperty(value = "TotalPointsEarnedByBalanceType")
        private List<TotalPointsChange> totalPointsEarnedByBalanceType;

        @JsonProperty(value = "TotalPointsRedeemedByBalanceType")
        private List<TotalPointsChange> totalPointsRedeemedByBalanceType;

        @JsonProperty(value = "TotalBoxOfficeValue")
        private Integer totalBoxOfficeValue;

        @JsonProperty(value = "TotalConcessionsValue")
        private Integer totalConcessionsValue;

        @JsonProperty(value = "TransactionDate")
        private String transactionDate;

        private long transactionDateTime;

        public void setTransactionDate(String transactionDate) {
            if (!Strings.isNullOrEmpty(transactionDate)) {
                long time = -1l;
                try {
                    if (transactionDate.indexOf("+") > -1) {
                        time = Long.parseLong(transactionDate.substring(transactionDate.indexOf("/Date(") + 6, transactionDate.indexOf("+")));
                    } else {
                        time = Long.parseLong(transactionDate.substring(transactionDate.indexOf("/Date(") + 6, transactionDate.indexOf(")/")));
                    }
                } catch (Exception e) {
                }

                if (time > 0) {
                    this.transactionDate = DateTool.dateTimeToString(new Date(time));
                    this.transactionDateTime = time;
                }
            }
        }

        @Override
        public String toString() {
            return "Item:" + lineItems +
                    "\ntotalPointsBalanceByBalanceType=" + totalPointsBalanceByBalanceType +
                    "\n totalPointsEarnedByBalanceType=" + totalPointsEarnedByBalanceType +
                    "\n totalPointsRedeemedByBalanceType=" + totalPointsRedeemedByBalanceType +
                    '}';
        }
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LineItem {
        @JsonProperty(value = "BoxOfficeValue")
        private Integer boxOfficeValue;

        @JsonProperty(value = "ItemName")
        private String itemName;


        @JsonProperty(value = "ConcessionsValue")
        private Integer concessionsValue;
        @JsonProperty(value = "PointsAdjustments")
        private List<PointsAdjustments> pointsAdjustments;

        @JsonProperty(value = "TotalPointsEarnedByBalanceType")
        private List<TotalPointsChange> totalPointsEarnedByBalanceType;

        @JsonProperty(value = "TotalPointsRedeemedByBalanceType")
        private List<TotalPointsChange> totalPointsRedeemedByBalanceType;

        @Override
        public String toString() {
            return "LineItem{" +
                    "concessionsValue=" + concessionsValue +
                    "itemName=" + itemName +
                    ", pointsAdjustments=" + pointsAdjustments +
                    ", totalPointsEarnedByBalanceType=" + totalPointsEarnedByBalanceType +
                    ", totalPointsRedeemedByBalanceType=" + totalPointsRedeemedByBalanceType +
                    '}';
        }
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PointsAdjustments {
        @JsonProperty(value = "BalanceTypeId")
        private Integer balanceTypeId;
        @JsonProperty(value = "PointsEarned")
        private Integer pointsEarned;
        @JsonProperty(value = "PointsRedeemed")
        private Integer pointsRedeemed;


    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TotalPointsChange {
        @JsonProperty(value = "BalanceTypeId")
        private Integer balanceTypeId;
        @JsonProperty(value = "Points")
        private Integer points;

        @Override
        public String toString() {
            return "TotalPointsChange{" +
                    "balanceTypeId=" + balanceTypeId +
                    ", points=" + points +
                    '}';
        }
    }

}
