package dao;

import dao.cache.RedisDao;
import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testGetAndSet(){
        long id = 1000l;
        Seckill seckill = redisDao.getSeckillById(id);
        if (seckill == null){
            System.out.println("redis null");
            seckill = seckillDao.queryById(id);
            if (seckill == null){
                System.out.println("mysql null");
            } else {
                System.out.println(redisDao.putSeckill(seckill));
                System.out.println(redisDao.getSeckillById(id));
            }
        } else {
            System.out.println("Redis not null");
            System.out.println(seckill);
        }
    }
}
