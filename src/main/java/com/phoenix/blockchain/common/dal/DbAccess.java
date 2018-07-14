package com.phoenix.blockchain.common.dal;

import java.util.List;

import com.google.common.base.Optional;

/**
 * Created by chengfeng on 2018/7/11.
 */
public interface DbAccess {

    /**
     * 读取数据
     *
     * @param key
     */
    Optional<Object> get(String key);

    /**
     * 增加数据
     *
     * @param key
     * @param value
     */
    void put(String key, Object value);

    /**
     * 按前缀查找
     *
     * @param prefix
     * @param <T>
     * @return
     */
    <T> List<T> selectByPrefix(String prefix);

    /**
     * 关闭数据库连接
     */
    void closeDB();

    /**
     * 删除key
     */
    void delete(String key);
}
