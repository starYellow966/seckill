package dao;

import entity.SuccessKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDaoTest {
    @Autowired
    private SuccessKillDao successKillDao;

    @Test
    public void insertSuccessKillTest(){
        int num = successKillDao.insertSuccessKill(1000L, 12314L, new Date());
        System.out.println(num);
    }

    @Test
    public void queryBySeckillTest(){
        SuccessKill successKill = successKillDao.queryBySeckill(1000L, 12314L);
        System.out.println(successKill);
    }
}
