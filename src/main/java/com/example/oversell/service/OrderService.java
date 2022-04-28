package com.example.oversell.service;

import com.example.oversell.pojo.Order;

public interface OrderService {
    int placeOrder(Order order);

    int placeOrderByOldWay(Order order);
}
