package dao;

import entity.SuccessKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

public interface SuccessKillDao {
    /**
     * 新增一条秒杀记录
     * seckillId和userPhone为联合主键
     * @param seckillId
     * @param userPhone
     * @param createTime
     * @return
     */
    public int insertSuccessKill(@Param("seckillId")long seckillId,
                                 @Param("userPhone") long userPhone,
                                 @Param("createTime")Date createTime);

    public SuccessKill queryBySeckill(@Param("seckillId")long seckillId,
                                        @Param("userPhone") long userPhone);
}
