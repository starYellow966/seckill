# 基础实现

框架：SSM

源码：[https://github.com/starYellow966/seckill](https://github.com/starYellow966/seckill)

业务：只关注秒杀这块，所以页面只有两个，分别是秒杀商品列表页和商品详情页

## 业务流程

![秒杀总体的业务流程](https://upload-images.jianshu.io/upload_images/11455432-e16d4b93f51df410?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 效果展示

![秒杀商品列表页](https://upload-images.jianshu.io/upload_images/11455432-0699f2f313775c9d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![秒杀商品详情页-秒杀开始](https://upload-images.jianshu.io/upload_images/11455432-6615e2166f87eb48?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![秒杀商品详情页-秒杀未开始](https://upload-images.jianshu.io/upload_images/11455432-1f22b568b1dac9a6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![秒杀商品详情页-秒杀已结束](https://upload-images.jianshu.io/upload_images/11455432-907126137a4b8376?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## URL设计

|URL|解释|
|--|--|
|`/list`|秒杀商品列表页|
|`/{seckillId}/detail`|秒杀商品详情页|
|`/{seckillId}/exposer`|获取商品的秒杀地址，主要工作内容是生成md5。|
|`/{seckillId}/{md5}/execution`|执行秒杀|
|`/time/now`|获取服务器时间|

PS：
- 只有当处于秒杀时间内时，才会暴露秒杀地址。避免用户提前知道地址后，使用程序进行秒杀，对其他用户不公平，也防止恶意攻击
- md5的生成是不可逆的，所以能有效保护执行秒杀的地址

## 部署架构

当然这里并没有实现

![系统部署架构图](https://upload-images.jianshu.io/upload_images/11455432-ab1dea462de3b21c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 高并发优化

## 瓶颈分析

从业务角度分析，高并发主要发生在秒杀商品详情页。进入秒杀商品详情页以及请求涉及到的操作有：
1. 加载网页资源，包括静态资源和动态资源
2. 获取服务器时间的请求
3. 获取秒杀地址的请求
4. 执行秒杀的请求

**1）加载网页资源**
- 优化思路：缓存

**2）获取服务器时间的请求**
- 分析：该请求的处理是在内存中 new 一个 Date 对象，访问一次内存的大约是 10ns，得出该请求的 QPS = 1亿/s，所以该请求不是瓶颈所在。

**3）获取秒杀地址的请求**
- 分析：该请求的处理主要任务是在内存中生成md5。

**4）执行秒杀的请求**
- 过程：insert增加记录、update减库存。两个步骤组成事务。
- 分析：
  - 如果多个事务对同一商品执行秒杀，由于事务在提交或回滚前都会持有行锁，这就导致多个事务最终变成串行执行。
  - 服务器端和数据库端的分离，使得两个步骤的通信存在了网络延迟，增加行锁的持有时间。
  - 这两个操作结果都存储在服务器端，容易引起GC，也会增加行锁的持有时间。
  -  同时目前回滚的控制是由服务器端完成的，也会增加行锁的持有时间。
  - 另外，一条update其实QPS有4w/s，已经足够了。*【参考并没实践，因为还没学压力测试】*


**结论：**
- 瓶颈在于执行秒杀的请求。核心优化是减少行锁的持有时间
- 还可以优化的点：
  - 对资源进行缓存，如 CDN、Redis 缓存
  - 前端控制按钮只能点击一次

## 优化一：执行秒杀的请求

**优化核心**：减少行锁的持有时间。

### 方案一：简单

**核心思路**：使用 MySQL 的存储过程，减少服务器端和数据库端通信次数，从而降低网络延迟和GC时间。

存储过程缺点：
- 难调试
- 难移植
- 对数据库要求高

### 方案二：分布式

核心思想：
- 将库存中保存在内存中，并使用原子计数器保证操作的原子性
- 将“增加记录”的操作，发送到消息队列中，再启动消费者消费消息并执行“增加记录”的操作

缺点：
- 运维成本高、稳定性不如上一种方案
- 开发成本高，如保证数据一致性、回滚方案等的设计和实现都会比较复杂
- 目前的架构还存在幂等性问题，即重复秒杀问题

![架构图](https://upload-images.jianshu.io/upload_images/11455432-a060342a355dd5ff?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 优化二：缓存

**1）对热门商品信息进行Redis缓存**

具体过程：查询某个商品时，先尝试去 Redis 获取，若失败再尝试去数据库获取。并通过超时时间为维护数据的一致性。

**2）静态资源的缓存**

方案：
- 将一些静态资源（如css，js）分发到 CDN 中
- 将一些基本不变的页面静态化，然后分发到 CDN 中

---
更完整的总结：[【项目总结】慕课Java高并发秒杀的实现和总结 - 简书](https://www.jianshu.com/p/29dddd90395e)