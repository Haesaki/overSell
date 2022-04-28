package com.example.oversell;

import com.example.oversell.pojo.Items;
import com.example.oversell.pojo.Order;
import com.example.oversell.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DistributeOrderServiceTest {
    @Autowired
    OrderService orderService;

    @Test
    public void placeOrderTest() throws Exception {
        int threadNum = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum);
        ExecutorService ex = Executors.newFixedThreadPool(threadNum);

        for (int i = 0; i < threadNum; i++) {
            ex.execute(() -> {
                try {
                    cyclicBarrier.await();
                    List<Items> itemsList = new LinkedList<>();
                    itemsList.add(new Items("test", "simulate concurrent environment", 10, 0, 10, 1, new Date(), new Date(), "good"));
                    List<Integer> itemsCount = new LinkedList<>();
                    itemsCount.add(new Random().nextInt());
                    Order placeO = new Order(UUID.randomUUID().toString(), new Date(), 1,
                            0, 0, 0, 0, itemsList, new ArrayList<>());
                    int success = orderService.placeOrder(new Order());
                    if (success == 1)
                        System.out.println("Success" + placeO.toString());
                    else
                        System.out.println("Fail" + placeO.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); // 目的是不让主线程结束 避免数据库连接池关闭
        ex.shutdown();
    }
}