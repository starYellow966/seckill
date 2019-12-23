package dao;

import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest{
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void queryAll(){
        List<Seckill> ans = seckillDao.queryAll();
        for(Seckill s: ans){
            System.out.println(s);
        }
    }

    @Test
    public void queryByIdTest(){
        Seckill ans = seckillDao.queryById(1000L);
        System.out.println(ans);
    }

    @Test
    public void reduceNumById(){
        int num = seckillDao.reduceNumById(10000L);
        System.out.println(num);
    }
}
