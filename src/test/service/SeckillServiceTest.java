package service;

import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    @Autowired
    SeckillService seckillService;

    @Test
    public void testGetAllSeckill(){
        List<Seckill> seckills = seckillService.getAllSeckill();
        for (Seckill s: seckills){
            System.out.println(s);
        }
    }

    @Test
    public void testGetSeckillBySeckillId(){
        System.out.println(seckillService.getSeckillBySeckillId(1000l));
    }

    @Test
    public void testExportSeckillUrl(){
        System.out.println(seckillService.exportSeckillUrl(1000l) + "\n");
        System.out.println(seckillService.exportSeckillUrl(1001l) + "\n");
        System.out.println(seckillService.exportSeckillUrl(1002l) + "\n");
        System.out.println(seckillService.exportSeckillUrl(1003l) + "\n");
    }

    @Test
    public void testExecuteSeckill(){
        try{
            System.out.println(seckillService.executeSeckill(1002l, 1231242, "7a20c4b510e45ee4f459bec93693842f"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testExecuteSeckillByProcedure(){
        long seckillId = 1002l;
        long userPhone = 1235834304l;
        System.out.println(seckillService.executeSeckillByProcedure(seckillId, userPhone, ""));
        System.out.println(seckillService.executeSeckillByProcedure(seckillId, userPhone, ""));
    }
}
