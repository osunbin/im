package com.bin.im.entry.common;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NacosHelper {

    private static Logger logger = LoggerFactory.getLogger(NacosHelper.class);


    private static ConfigService nacosConfig = null;
    private static final Map<String, String> DEF_CONFIG = new HashMap<>();

    private String nacosAddress;

    public void initNacos() {

        if (nacosConfig == null && nacosAddress != null) {
            try {
                nacosConfig = NacosFactory.createConfigService(nacosAddress.replace("nacos://", ""));
            } catch (NacosException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static boolean writeConfig(String group, String key, String content)
            throws Throwable {
        if (nacosConfig == null) {
            logger.error("nacos_client=not_init");
        }
        if (nacosConfig.publishConfig(key, group, content)) {
            return true;
        }
        return false;
    }

    public static String getCmdConfig(String group, String key) {
        try {
            String configKey = group + "_" + key;
            if (null == DEF_CONFIG.get(configKey)) {
                DEF_CONFIG.put(configKey, nacosConfig.getConfig(key, group, 100));
            }
            return DEF_CONFIG.get(configKey);
        } catch (NacosException e) {
            logger.error("err=get_config cmd={}", key);
            return null;
        }
    }

}
