package com.ronin.common;

import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *
 * 说明：系统中所有中断业务流程的错误或异常，都以此异常抛出，统一抛给控制层统一封装
 * 推荐：规范定义错误码与异常描述，有助于后期按照错误码就能定位问题来源；最好制定一份可供查阅的系统错误码字典。
 *
 * @author 应颂浩
 */
public class ServiceException extends BaseException {

    private static final long serialVersionUID = -6873174195938670725L;

    public ServiceException(String message) {
        this(-1, message);
    }

    public ServiceException(int code, String message) {
        super(code, message);
    }

    public ServiceException(ErrorResult errorResult) {
        super(errorResult);
    }

    @Override
    protected HttpStatus httpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
