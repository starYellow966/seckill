package entity;

import java.util.Date;

/**
 *
 create table seckill(
 `seckill_id` bigint not null AUTO_INCREMENT comment '秒杀商品编号',
 `name` varchar(128) not null comment '商品名称',
 `number` int not null comment '库存',
 `start_time` timestamp not null comment '秒杀开始时间',
 `end_time` timestamp not null comment '秒杀结束时间',
 `is_delete` smallint not null default 0 comment '是否删除',
 primary key(`seckill_id`)
 )engine=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 comment='秒杀库存表';
 */
public class Seckill {
    private long seckillId;
    private String name;
    private int number;
    private Date startTime;
    private Date endTime;

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
}
