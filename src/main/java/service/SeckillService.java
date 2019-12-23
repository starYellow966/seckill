package service;

import dto.ExecutionResult;
import dto.Exposer;
import entity.Seckill;

import java.util.List;

public interface SeckillService {
    /**
     * 获取所有秒杀商品
     * @return
     */
    List<Seckill> getAllSeckill();
    Seckill getSeckillBySeckillId(long seckillId);

    /**
     * 秒杀开启时，输出秒杀接口地址；秒杀未开始时，只输出系统时间和秒杀时间
     * 目的：当秒杀开启前不能让用户猜到秒杀接口地址。
     * 秒杀接口地址：/seckill/{seckillId}/{md5}/execution
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5 通过这个字段判断当前的秒杀接口地址是否被篡改过
     */
    ExecutionResult executeSeckill(long seckillId, long userPhone, String md5);

    /**
     * 执行秒杀操作 By PROCEDURE
     * @param seckillId
     * @param userPhone
     * @param md5 通过这个字段判断当前的秒杀接口地址是否被篡改过
     */
    ExecutionResult executeSeckillByProcedure(long seckillId, long userPhone, String md5);
}
