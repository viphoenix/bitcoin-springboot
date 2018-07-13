package com.phoenix.blockchain.common.dal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chengfeng on 2018/7/11.
 */
@Configuration
@ConfigurationProperties(prefix = "rocksdb")
public class RocksDbConfig {

    /**
     * 数据保存路径
     */
    private String dataDir;

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }
}
