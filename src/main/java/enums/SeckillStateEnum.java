package enums;

public enum SeckillStateEnum {
    PARAM_ERR(-3, "前端传入参数不完整"),
    INNER_ERR(-2, "秒杀失败，服务器错误"),
    DATE_REWRT(-1, "秒杀地址数据被篡改"),
    END(0, "秒杀已结束"),
    SUCCESS(1, "秒杀成功"),
    REPEAT(2,"已秒杀过了，不可重复秒杀");


    private int code;
    private String info;

    SeckillStateEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public static String indexOf(int code){
        for (SeckillStateEnum s: SeckillStateEnum.values()){
            if (s.getCode() == code)
                return s.getInfo();
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
