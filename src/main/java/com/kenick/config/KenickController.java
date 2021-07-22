package com.kenick.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/kenick")
public class KenickController {

    private final static Logger LOG = LoggerFactory.getLogger(KenickController.class);

    @Value("${cron.perfectFundInfo}")
    String cronPerfectFundInfo;

    @Value("${storage.type}")
    String storageType;

    @Value("${storage.file.fund}")
    String storageFileFund;

    @Value("${storage.file.history.path}")
    String storageFileHistoryPath;

    @Value("${storage.file.history.enable}")
    Boolean storageFileHistoryEnable;

    @GetMapping("/log")
    public Map<String, Object> home() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("trace level log");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("debug level log");
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("info level log");
        }

        if (LOG.isWarnEnabled()) {
            LOG.warn("warn level log");
        }

        if (LOG.isErrorEnabled()) {
            LOG.error("error level log");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status", "good");
        result.put("name", "abc");
        result.put("password", "abc");
        return result;
    }

    @RequestMapping("/getConfig")
    @ResponseBody
    public String getConfig() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cronPerfectFundInfo", cronPerfectFundInfo);
        jsonObject.put("storageType", storageType);
        jsonObject.put("storageFileHistoryEnable", storageFileHistoryEnable);
        jsonObject.put("storageFileFund", storageFileFund);
        jsonObject.put("storageFileHistoryPath", storageFileHistoryPath);
        return jsonObject.toJSONString();
    }

}
