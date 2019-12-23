package entity;

import java.util.Date;

/**
 * create table success_kill(
 *     `order_id` bigint not null AUTO_INCREMENT comment '编号',
 *     `seckill_id` bigint not null comment '秒杀商品ID',
 *     `user_phone` bigint not null comment '下单用户手机',
 *     `state` int not null default 0 comment '0-下单成功，-1--下单失败，',
 *     `create_time` timestamp not null comment '下单时间',
 *     `is_delete` smallint default 0 comment '是否删除',
 *     primary key(`seckill_id`, `user_phone`),
 *     unique (`order_id`)
 * )engine=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 comment='秒杀成功明细表';
 */
public class SuccessKill {
    private long orderId;
    private Seckill seckill;
    private long userPhone;
    private int state;
    private Date createTime;

    @Override
    public String toString() {
        return "SuccessKill{" +
                "orderId=" + orderId +
                ", seckill=" + seckill +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
