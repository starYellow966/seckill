create database seckill;
use seckill;

create table seckill(
    `seckill_id` bigint not null AUTO_INCREMENT comment '秒杀商品编号',
    `name` varchar(128) not null comment '商品名称',
    `number` int not null comment '库存',
    `start_time` timestamp not null comment '秒杀开始时间',
    `end_time` timestamp not null comment '秒杀结束时间',
    `is_delete` smallint not null default 0 comment '是否删除',
    primary key(`seckill_id`)
)engine=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 comment='秒杀库存表';

insert into
  seckill(name,number,start_time,end_time)
  values
    ('1元秒杀坚果tNT工作站',100,'2018-06-01 00:00:00','2018-06-02 00:00:00'),
    ('1元秒杀iphonex',100,'2018-06-01 00:00:00','2018-06-02 00:00:00'),
    ('1元秒杀坚果3',100,'2018-06-01 00:00:00','2018-06-02 00:00:00'),
    ('1元秒杀mac',100,'2018-06-01 00:00:00','2018-06-02 00:00:00');

create table success_kill(
    `order_id` bigint not null AUTO_INCREMENT comment '编号',
    `seckill_id` bigint not null comment '秒杀商品ID',
    `user_phone` bigint not null comment '下单用户手机',
    `state` int not null default 0 comment '0-下单成功，-1--下单失败，',
    `create_time` timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '下单时间',
    `is_delete` smallint default 0 comment '是否删除',
    primary key(`seckill_id`, `user_phone`),
    unique (`order_id`)
)engine=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 comment='秒杀成功明细表';