package com.phoenix.blockchain.biz.service.node;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.phoenix.blockchain.biz.service.account.AccountManagerImpl;
import com.phoenix.blockchain.common.dal.RocksDbAccess;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.core.service.net.model.Node;

/**
 * Created by chengfeng on 2018/7/14.
 */
@Component
public class NodeManagerImpl implements NodeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagerImpl.class);

    private static final String NODE_LIST_KEY = "NODES_";

    @Autowired
    private RocksDbAccess rocksDbAccess;

    /**
     * 添加节点
     *
     * @param nodes
     * @return
     */
    @Override
    public void addNodes(List<Node> nodes) {
        try {
            rocksDbAccess.put(NODE_LIST_KEY, nodes);
        } catch (Exception e) {

            LogUtils.warn(LOGGER, e, "保存数据时,序列化异常.");
        }
    }

    /**
     * 获取所有节点
     *
     * @return
     */
    @Override
    public List<Node> getNodes() {
        try {
            Optional<Object> result = rocksDbAccess.get(NODE_LIST_KEY);
            if (result.isPresent()) {
                List<Node> nodes = (List<Node>) result;

                return nodes;
            }
        } catch (Exception e) {
            LogUtils.warn(LOGGER, e, "获取数据时,反序列化异常.");
        }

        return new ArrayList<Node>();

    }

    @Override
    public void clearNodes(){
        rocksDbAccess.delete(NODE_LIST_KEY);
    }
}
