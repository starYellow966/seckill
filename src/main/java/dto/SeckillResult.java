package dto;

/**
 * 前端与后端交互的数据封装
 * @param <T>
 */
public class SeckillResult<T> {

    /**
     * 请求处理是否成功
     */
    private boolean success;

    private T data;

    private String errInfo;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String errInfo) {
        this.success = success;
        this.errInfo = errInfo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }
}
