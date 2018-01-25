package com.lhh.vista.customer.v2s.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Random;

/**
 * Created by soap on 2016/12/14.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatLayoutData {
    @JsonProperty(value = "Areas")
    private List<Area> areas;

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Area {
        @JsonProperty(value = "AreaCategoryCode")
        private String areaCode;//
        @JsonProperty(value = "Description")
        private String desc;//
        @JsonProperty(value = "ColumnCount")
        private Integer columnCount;//
        @JsonProperty(value = "RowCount")
        private Integer rowCount;//
        @JsonProperty(value = "TicketTypeCode")
        private String ticketTypeCode;//
        @JsonProperty(value = "Rows")
        private List<Row> rows;//
    }

    
    @ToString
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Row {
        @JsonProperty(value = "PhysicalName")
        private String physicalName;//区域类别编码
        @JsonProperty(value = "Seats")
        private List<Seat> seats;// 票类名称
        
		public String getPhysicalName() {
			return physicalName;
		}
		public void setPhysicalName(String physicalName) {
			if(null == physicalName || "".equals(physicalName)) {
				physicalName = "";
			}
			this.physicalName = physicalName;
		}
		public List<Seat> getSeats() {
			return seats;
		}
		public void setSeats(List<Seat> seats) {
			this.seats = seats;
		}
        
        
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Seat {
        @JsonProperty(value = "Id")
        private String id;//
		@JsonProperty(value = "Position")
        private Position position;//
        @JsonProperty(value = "SeatStyle")
        private Integer seatStyle;//
        @JsonProperty(value = "SeatsInGroup")
        private List<Position> group;
        @JsonProperty(value = "Status")
        private Integer status;     
    }

    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Position {
        @JsonProperty(value = "AreaNumber")
        private Integer areaNum;
        @JsonProperty(value = "ColumnIndex")
        private Integer col;
        @JsonProperty(value = "RowIndex")
        private Integer row;
    }
}
