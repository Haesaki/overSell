package com.example.oversell.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;
    private List<Items> itemsList;
    private List<Integer> itemsCount;

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", createdTime=" + createdTime +
                ", payMethod=" + payMethod +
                ", realPayAmount=" + realPayAmount +
                ", postAmount=" + postAmount +
                ", isComment=" + isComment +
                ", orderStatus=" + orderStatus +
                ", itemsList=" + itemsList +
                ", itemsCount=" + itemsCount +
                '}';
    }

    public boolean correctOrder() {
        if (StringUtils.isBlank(orderId) || itemsList == null || itemsCount == null || itemsList.size() == 0
                || itemsCount.size() == 0 || itemsList.size() != itemsCount.size())
            return false;
        if (createdTime == null)
            createdTime = new Date();
        return true;
    }
}
