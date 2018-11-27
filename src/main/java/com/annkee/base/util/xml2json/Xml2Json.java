package com.annkee.base.util.xml2json;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangan
 * @date 2017/11/9
 */
@RestController
@RequestMapping("/xml")
@Slf4j
public class Xml2Json {

    @ResponseBody
    @ApiOperation(value = "xml转json", notes = "xml转json")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Object xml2json(@ApiParam(value = "xml：string") @RequestParam(name = "xmlStr", required = true) String xmlStr) {

        JSONObject xmlJSONObj = null;
        String result = null;
        try {
            xmlJSONObj = XML.toJSONObject(xmlStr);
            //设置缩进
            result = xmlJSONObj.toString(4);
        } catch (JSONException e) {
            log.error("---error: {}", e.getMessage());
        }
        log.info("---result: {}", result);
        return result;
    }
}
