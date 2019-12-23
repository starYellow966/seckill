package dto;

import entity.SuccessKill;
import enums.SeckillStateEnum;

/**
 * 秒杀执行结果
 */
public class ExecutionResult {

    /**
     * 执行结果的响应码
     */
    private int state;

    /**
     * 执行结果的说明
     */
    private String stateInfo;

    /**
     * 秒杀成功后的订单信息
     */
    private SuccessKill successKill;

    public ExecutionResult(SeckillStateEnum seckillStateEnum, SuccessKill successKill){
        this.state = seckillStateEnum.getCode();
        this.stateInfo = seckillStateEnum.getInfo();
        this.successKill = successKill;
    }

    public ExecutionResult(int state, String stateInfo, SuccessKill successKill) {
        this.state = state;
        this.stateInfo = stateInfo;
        this.successKill = successKill;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKill=" + successKill +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
}
