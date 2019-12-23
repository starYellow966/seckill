package service.impl;

import dao.SeckillDao;
import dao.SuccessKillDao;
import dao.cache.RedisDao;
import dto.ExecutionResult;
import dto.Exposer;
import entity.Seckill;
import entity.SuccessKill;
import enums.SeckillStateEnum;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import service.SeckillService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    /**
     * 日志记录
     */
    private static final Log LOG = LogFactory.getLog(SeckillServiceImpl.class);

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SuccessKillDao successKillDao;

    /**
     * 盐值，用于生成md5
     */
    private final String salt = "asdagqvdlfkvnpjp3123213235@$432";

    @Override
    public List<Seckill> getAllSeckill() {
        List<Seckill> ans = seckillDao.queryAll();
        return ans;
    }

    @Override
    public Seckill getSeckillBySeckillId(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 优化:对热门商品信息进行缓存
        Seckill seckill = redisDao.getSeckillById(seckillId); // 先去redis读缓存
        if (seckill == null){
            seckill = seckillDao.queryById(seckillId); // 缓存没有，再去mysql读
            redisDao.putSeckill(seckill);  // 保存到缓存中
        }

        Date now = new Date(); // 当前服务器时间

        // 判断秒杀是否开启
        Exposer exposer = null;
        if (now.getTime() < seckill.getStartTime().getTime() || now.getTime() > seckill.getEndTime().getTime()) { // 时间未到或者秒杀过期了
            exposer = new Exposer(seckill.getSeckillId(), seckill.getStartTime(), seckill.getEndTime(), now);
        } else { // 时间已到
            exposer = new Exposer(seckill.getSeckillId(),
                    seckill.getStartTime(),
                    seckill.getEndTime(),
                    this.generateMD5(seckill.getSeckillId()),
                    now);
        }
        return exposer;
    }

    @Override
    @Transactional
    public ExecutionResult executeSeckill(long seckillId, long userPhone, String md5) {
        // 1.验证数据完整性
        if (seckillId < 0 || md5 == null)
            return new ExecutionResult(SeckillStateEnum.PARAM_ERR, null);
        if (! md5.equals(generateMD5(seckillId)))
            return new ExecutionResult(SeckillStateEnum.DATE_REWRT, null);

        /** 2.业务处理：执行秒杀。包括两步:
        *      a) 增加一条订单记录
        *      b) 减库存
         *  先增后减的好处在于：增加失败说明，重复秒杀，无需回滚[NEW]
        */
        try{
//            Seckill seckill = seckillDao.queryById(seckillId);  // 先查询商品
//            if (seckill.getNumber() <= 0)
//                throw new RuntimeException("商品卖光了");

            int effectNum = successKillDao.insertSuccessKill(seckillId, userPhone, new Date()); // a.增记录
            if (effectNum == 1) { // 增加记录成功，说明可以秒杀
                effectNum = seckillDao.reduceNumById(seckillId);  // b.减库存
                SuccessKill successKill = successKillDao.queryBySeckill(seckillId, userPhone);  // 查询记录
                return new ExecutionResult(SeckillStateEnum.SUCCESS, successKill);
            } else if (effectNum == 0) {  // 说明重复秒杀
                return new ExecutionResult(SeckillStateEnum.REPEAT, null);
            } else {
                throw new RuntimeException("执行秒杀时，增记录操作异常");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExecutionResult executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        // 1.验证数据完整性
//        if (seckillId < 0 || md5 == null)
//            return new ExecutionResult(SeckillStateEnum.PARAM_ERR, null);
//        if (! md5.equals(generateMD5(seckillId)))
//            return new ExecutionResult(SeckillStateEnum.DATE_REWRT, null);

        // 2.业务处理
        Map<String, Object> params = new HashMap<>();
        params.put("seckillId", seckillId);
        params.put("phone",userPhone);
        params.put("killTime", new Date());
        params.put("result", null);
        try {
            // 执行存储过程
            seckillDao.killByProcedure(params);
            Integer result = (Integer) params.get("result");
            if (result == 1){ // 执行秒杀成功
                SuccessKill successKill = successKillDao.queryBySeckill(seckillId, userPhone);  // 查询记录
                return new ExecutionResult(SeckillStateEnum.SUCCESS, successKill);
            } else if (result == -1){
                return new ExecutionResult(SeckillStateEnum.REPEAT, null);
            } else {
                return new ExecutionResult(SeckillStateEnum.INNER_ERR, null);
            }
        } catch (Exception e){
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
            return new ExecutionResult(SeckillStateEnum.INNER_ERR, null);
        }
    }

    private String generateMD5(long seckillId){
        String base = seckillId + ":" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
