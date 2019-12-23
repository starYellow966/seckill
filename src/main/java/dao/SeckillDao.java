package dao;

import entity.Seckill;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    public List<Seckill> queryAll();
    public Seckill queryById(long seckillId);

    /**
     * 库存数-1
     * @param seckillId
     * @return
     */
    public int reduceNumById(long seckillId);

    /**
     * 调用存储过程执行秒杀
     * @param params 保存了参数和返回值。参数：seckillId，phone，killTime；返回值：result
     */
    public void killByProcedure(Map<String, Object> params);
}
