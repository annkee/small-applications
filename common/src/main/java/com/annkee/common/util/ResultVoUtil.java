package com.annkee.common.util;

import com.annkee.common.enums.ResultCodeEnum;
import com.annkee.common.vo.ResultVO;

/**
 * @author wangan
 * @date 2017/1/5
 */
public class ResultVoUtil {

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(200);
        resultVO.setMessage("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }
    public static ResultVO error(ResultCodeEnum resultCodeEnum) {
        ResultVO resultVO = new ResultVO(resultCodeEnum);
        return resultVO;
    }
}
