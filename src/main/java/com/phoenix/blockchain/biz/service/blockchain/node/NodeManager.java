package com.phoenix.blockchain.biz.service.blockchain.node;

import java.util.List;

import com.phoenix.blockchain.core.service.net.model.Node;

/**
 * Created by chengfeng on 2018/7/14.
 */
public interface NodeManager {

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
}
