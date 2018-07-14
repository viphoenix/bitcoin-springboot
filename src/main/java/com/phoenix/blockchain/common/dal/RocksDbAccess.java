package com.phoenix.blockchain.common.dal;

import static com.phoenix.blockchain.common.util.ByteUtils.prefixJudge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.rocksdb.Options;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Optional;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.service.net.model.Node;
import com.phoenix.blockchain.core.service.net.config.TioConfig;

/**
 * Created by chengfeng on 2018/7/11.
 */
@Component
public class RocksDbAccess implements DbAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocksDbAccess.class);

    private RocksDB rocksDB;

    @Autowired
    private TioConfig tioConfig;

    @Autowired
    private RocksDbConfig rocksDbConfig;

    public RocksDbAccess() {

    }

    /**
     * 初始化RocksDB
     */
    @PostConstruct
    public void initRocksDB() {

        try {
            //如果数据库路径不存在，则创建路径
            File directory = new File(System.getProperty("user.dir")+"/"+ rocksDbConfig.getDataDir());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), rocksDbConfig.getDataDir());
        } catch (RocksDBException e) {
            LogUtils.warn(LOGGER, e, "初始化db异常.");

        }
    }

    @Override
    public Optional<Object> get(String key) {
        try {
            // 避免同步时出现NPE
            byte[] res = rocksDB.get(key.getBytes());

            if (null != res) {
                return Optional.of(SerializeUtils.unSerialize(res));

            }
        } catch (RocksDBException e) {
            LogUtils.warn(LOGGER, e, "读取数据异常. key: {0}.", key);
        }

        return Optional.absent();
    }

    @Override
    public void put(String key, Object value) {
        try {
            rocksDB.put(key.getBytes(), SerializeUtils.serialize(value));
        } catch (RocksDBException e) {
            LogUtils.warn(LOGGER, e, "保存数据异常. data: {0}.", value);
        }
    }

    @Override
    public <T> List<T> selectByPrefix(String prefix) {
        ArrayList<T> ts = new ArrayList<>();
        RocksIterator iterator = rocksDB.newIterator(new ReadOptions());
        byte[] key = prefix.getBytes();
        for (iterator.seek(key); iterator.isValid() && prefixJudge(iterator.key(), key); iterator.next()) {

            ts.add((T) SerializeUtils.unSerialize(iterator.value()));
        }
        return ts;
    }

    @Override
    public void closeDB() {
        if (null != rocksDB) {
            rocksDB.close();
        }
    }

    @Override
    public void delete(String key) {
        try {
            rocksDB.delete(key.getBytes());
        } catch (Exception e) {
            LogUtils.warn(LOGGER, e, "删除数据异常. key: {0}.", key);

        }
    }
}
