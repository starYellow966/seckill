-- 秒杀执行的存储过程
DELIMITER $$
-- 定义存储过程
-- 参数：seckillId，userPhone，killTime
-- 返回值：-2：未知错误；-1：重复秒杀；0--seckill有问题；1--执行成功
CREATE PROCEDURE `seckill`.`execute_seckill`(in v_seckill_id bigint, in v_phone bigint, in v_kill_time timestamp,out r_result int)
    BEGIN
        DECLARE effect_count int default 0; -- 定义一个变量
        START TRANSACTION;
        INSERT IGNORE INTO success_kill(seckill_id, user_phone, create_time, state) value (v_seckill_id, v_phone, v_kill_time, 0);
        select row_count() into effect_count; -- 将上一条除select的DML语句执行结果赋值到effect_count
        IF (effect_count = 0 ) THEN -- 说明重复秒杀
            set r_result = -1;
        ELSEIF (effect_count = 1) THEN -- insert成功，可以执行减库存
            update seckill
            set number=number-1
            where seckill_id=v_seckill_id and number>0 and is_delete=0 and end_time>v_kill_time and start_time<v_kill_time;

            select row_count() into effect_count;
            IF (effect_count = 0) THEN -- update执行成功，但没有找到seckill
                rollback;
                set r_result = 0;
            ELSEIF (effect_count = 1) THEN -- 秒杀成功
                COMMIT;
                set r_result = 1;
            ELSE
                ROLLBACK ;
                set r_result = -2;
            END IF;
        ELSE -- 说明insert语句执行异常
            set r_result = -2;
        END IF;
    END
$$


--调用存储过程

DELIMITER ;
set @r_result = -3;
call execute_seckill(1002,13794578067,now(),@r_result);
select @r_result; -- 获取结果