package com.example.oversell.service.impl;

import com.example.oversell.mapper.ItemsMapper;
import com.example.oversell.pojo.Items;
import com.example.oversell.pojo.Order;
import com.example.oversell.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private ItemsMapper itemsMapper;

    private Lock lock = new ReentrantLock();

    @Override
    public int placeOrder(Order order) {
        if (!order.correctOrder())
            return 0;
        return 1;
    }

    @Override
    public synchronized int placeOrderByOldWay(Order order) {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        platformTransactionManager.commit(transactionStatus);

        if (!order.correctOrder())
            return 0;
        List<String> itemsList = order.getItemIdList();
        List<Integer> countList = order.getItemsCount();
        int size = itemsList.size();
        for (int i = 0; i < size; i++) {

        }
        return 1;
    }
}
