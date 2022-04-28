# 超卖问题

超卖问题的解决demo

模拟解决的demo 就不使用前端来进行操作 直接使用后端测试来进行 多线程并发来模拟买入操作。

大幅简化了流程，只考虑下订单时候的过程，不考虑后面的支付过程，也就不考虑购买商品的价格因素

## 解决思路

### 单体架构

PlatformTransactionManager: 主要是进行事务管理的操作, 如果不加上这个在函数内的事务操作,会导致当函数结束后,但是数据库的事务没有结束,这就导致别的线程可以获取到创建订单的这个函数的锁,并可能从数据库中获取到上一个线程获取到的结果,而不是上一个线程操作后的结果,导致超卖现象的产生. 需要手动控制事务的结束. 
1. 方法锁: 利用在service的更新函数上面上一个锁,以及需要控制该函数在数据库提交后才能结束
   ```java
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Override
    public synchronized int placeOrderByOldWay1(Order order){
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        ...
        ...
        platformTransactionManager.commit(transactionStatus);
   }
   ```
   问题:单体架构适用

2. 块锁:
   ```java
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Override
    public synchronized int placeOrderByOldWay2(Order order){
        synchronize(this){
            TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
            ...
            // 商品数量的修改
            // 如果不满足条件直接退出 无需进行下面的步骤
            ...
            platformTransactionManager.commit(transactionStatus);
        }
        // 订单的一些操作
        ...
        ...
   }
   ```
   问题:单体架构适用
3. ReentrantLock锁解决问题
   和同步代块步骤差不多
   ```java
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    private Lock lock = new ReentrantLock();
    @Override
    public synchronized int placeOrderByOldWay2(Order order){
        lock.lock();
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        ...
        // 商品数量的修改
        // 如果不满足条件直接退出 无需进行下面的步骤
        ...
        platformTransactionManager.commit(transactionStatus);
        lock.unlock();
        // 订单的一些操作
        ...
        ...
   }
   ```
### 分布式结构

4. 在数据库里面加上版本号 每次更新数据库里面的数据时候

    1. 先去查询数据库里面的商品数目和版本号
    2. 更新的时候利用版本号对更新操作进行限制

## SQL

items 建表SQL语句

```mysql
-- auto-generated definition
create table items
(
    id            varchar(64) not null comment '商品主键id' primary key,
    item_name     varchar(32) not null comment '商品名称',
    cat_id        int         not null comment '分类外键id',
    root_cat_id   int         not null comment '一级分类外键id',
    sell_counts   int         not null comment '累计销售',
    on_off_status int         not null comment '上下架状态 1:上架 2:下架',
    content       text        not null comment '商品内容',
    created_time  datetime    not null comment '创建时间',
    updated_time  datetime    not null comment '更新时间'
)
```