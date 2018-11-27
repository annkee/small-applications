package com.annkee.base.vo;

import com.annkee.base.enums.ResultCodeEnum;
import lombok.Data;

/**
 * 最外层返回格式控制,给前端返回的数据格式化
 *
 * @author wangan
 * @date 2017/1/4
 */
@Data
public class ResultVO<T> {
    
    /**
     * 错误码
     */
    private Integer code;
    
    /**
     * 提示信息
     */
    private String message;
    
    /**
     * 具体内容
     */
    private T data;
    
    public ResultVO() {
    }
    
    public ResultVO(ResultCodeEnum resultCodeEnum, T data) {
        this(resultCodeEnum);
        this.data = data;
    }
    
    
    public ResultVO(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
