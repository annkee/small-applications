package com.annkee.common.exception;

import com.annkee.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常配置
 *
 * @author wangan
 * @date 2018/4/1
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalDefaultExceptionHandler {

    /**
     * baseException 处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public ResultVO handleException(BaseException e) {
        log.error("handleException: e={}", e.getMessage());

        return new ResultVO(e.getResultCodeConstant());
    }


}
