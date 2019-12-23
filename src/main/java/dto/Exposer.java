package dto;

import java.util.Date;

/**
 * 当前端请求秒杀地址时，通过这个类进行数据封装。
 * 规则：
 *  1）当秒杀开启时，即时间到了，返回秒杀接口地址（/seckill/{seckillId}/{md5}/execution），即返回 md5。
 *  2）当秒杀未开启时，仅返回服务器时间和秒杀时间
 */
public class Exposer {
    /**
     * 表示秒杀是否开启
     */
    private boolean exposer;
    private long seckillId;
    private Date startTime;
    private Date endTime;

    private String md5;

    /**
     * 服务器当前时间。
     * TODO：如果传输延迟较高，前端设计的倒计时有误差
     */
    private Date sysTime;

    /**
     * 秒杀开始时，调用这个构造函数
     * @param seckillId
     * @param startTime
     * @param endTime
     */
    public Exposer(long seckillId, Date startTime, Date endTime, String md5, Date sysTime) {
        this.exposer = true;
        this.seckillId = seckillId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.md5 = md5;
        this.sysTime = sysTime;
    }

    /**
     * 秒杀未开始时，调用这个构造函数
     * @param seckillId
     * @param startTime
     * @param endTime
     */
    public Exposer(long seckillId, Date startTime, Date endTime, Date sysTime) {
        this.exposer = false;
        this.seckillId = seckillId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sysTime = sysTime;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "is_ready=" + exposer +
                ", seckillId=" + seckillId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", md5='" + md5 + '\'' +
                ", sysTime=" + sysTime +
                '}';
    }

    public boolean isExposer() {
        return exposer;
    }

    public void setExposer(boolean exposer) {
        this.exposer = exposer;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getSysTime() {
        return sysTime;
    }

    public void setSysTime(Date sysTime) {
        this.sysTime = sysTime;
    }
}
