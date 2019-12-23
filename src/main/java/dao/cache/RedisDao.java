package dao.cache;

import entity.Seckill;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(RedisDao.class);
    private final JedisPool jedisPool;

    /**
     * protostuff序列化的配置
     */
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);


    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    /**
     * 从缓存中取数据
     *
     * @param seckillId
     * @return
     */
    public Seckill getSeckillById(long seckillId) {
        Jedis jedis = jedisPool.getResource();
        try {
            // 注意: jedis.get只能获取到byte[]，故此需要反序列化
            String key = "seckill-" + seckillId;
            byte[] result = jedis.get(key.getBytes());
            if (result != null) { // 反序列化
                Seckill seckill = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(result, seckill, schema);
                return seckill;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 放入redis，进行缓存
     *
     * @param seckill
     * @return
     */
    public String putSeckill(Seckill seckill) {
        assert seckill != null;
        Jedis jedis = jedisPool.getResource();
        try {
            String key = "seckill-" + seckill.getSeckillId();
            byte[] value = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            int timeout = 60*60; // 缓存1h
            String result = jedis.setex(key.getBytes(), timeout, value);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }
}
