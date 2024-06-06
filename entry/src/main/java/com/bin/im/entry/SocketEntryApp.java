package com.bin.im.entry;

import com.bin.im.common.mini.yaml.YamlLoader;
import com.bin.im.common.mini.yaml.YamlNode;
import com.bin.im.entry.config.EntryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


public class SocketEntryApp {

    private static Logger logger = LoggerFactory.getLogger(SocketEntryApp.class);

    /**
     * machine.id.bit : 3
     * machine.id : 4
     * increase.id.bit : 8
     */
    public static void main(String[] args) throws Exception {
        String filePath = "entry.json";
        InputStream inputStream =
                SocketEntryApp.class.getResourceAsStream(filePath);
        YamlNode load = YamlLoader.load(inputStream);

    }


}
