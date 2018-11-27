package com.annkee.base.exception;

import com.annkee.base.enums.ResultCodeEnum;
import lombok.Data;

/**
 * 异常处理
 *
 * @author wangan
 * @date 2017/1/5
 */
@Data
public class BaseException extends RuntimeException {

    private ResultCodeEnum resultCodeEnum;

    public BaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.resultCodeEnum = resultCodeEnum;
    }

    public ResultCodeEnum getResultCodeConstant() {
        return resultCodeEnum;
    }


}
