package com.phoenix.blockchain.common.dal;

import java.util.List;

import com.google.common.base.Optional;
import com.phoenix.blockchain.core.service.net.model.Node;

/**
 * Created by chengfeng on 2018/7/11.
 */
public interface DbAccess {

    /**
     * 添加节点
     *
     * @param nodes
     */
    void addNodes(List<Node> nodes);

    /**
     * 获取所有节点
     *
     * @return
     */
    List<Node> getNodes();

    /**
     * 删除节点
     */
    void clearNodes();

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
}
