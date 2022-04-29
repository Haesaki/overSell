package com.example.oversell;

import com.example.oversell.mapper.ItemsMapper;
import com.example.oversell.pojo.Items;
import com.example.oversell.pojo.Order;
import com.example.oversell.service.OrderService;
import org.junit.jupiter.api.Assertions;
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
    private OrderService orderService;

    @Autowired
    private ItemsMapper itemsMapper;

    @Test
    public void preTestRandom() throws Exception {
        Random random = new Random();
        int max = 0, min = Integer.MAX_VALUE;
        for (int i = 0; i < 100000; i++) {
            int r = random.nextInt(100); // 随机生成 0 - 99的随机数
            max = Math.max(max, r);
            min = Math.min(min, r);
        }
        System.out.println("max: " + max);
        System.out.println("min: " + min);
    }

    @Test
    public void preTestMysql() throws Exception {
        String itemId = "test";
        Integer original = itemsMapper.selectStockById(itemId);
        Integer leftSellCount = 1000;
        itemsMapper.updateStockById(itemId, leftSellCount);
        Assertions.assertEquals(leftSellCount, itemsMapper.selectStockById(itemId));
        itemsMapper.updateStockById(itemId, original);
    }

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
                    List<String> itemsList = new LinkedList<>();
                    itemsList.add("test");
                    List<Integer> itemsCount = new LinkedList<>();
                    itemsCount.add(new Random().nextInt(10));
                    Order placeO = new Order(UUID.randomUUID().toString(), new Date(), 1,
                            0, 0, 0, 0, itemsList, itemsCount);
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