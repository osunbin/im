package com.bin.im.server;

import com.bin.im.common.mini.yaml.YamlLoader;
import com.bin.im.common.mini.yaml.YamlMapping;
import com.bin.im.common.mini.yaml.YamlNode;

import java.io.InputStream;

/**
 *   未完成：
 *      1)系统群消息 采取前端 定时拉取(主动通知扩散太大了)
 */
public class ImServer {


    public static void main(String[] args) {
        String filePath = "mysql_sharding.json";
        InputStream inputStream =
                ImServer.class.getResourceAsStream(filePath);
        YamlNode yaml = YamlLoader.load(inputStream);

        YamlMapping root = (YamlMapping) yaml;
        String rootName = root.nodeName();

    }
}
