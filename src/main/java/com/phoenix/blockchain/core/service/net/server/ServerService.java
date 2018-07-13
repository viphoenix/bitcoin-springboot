package com.phoenix.blockchain.core.service.net.server;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;

import com.phoenix.blockchain.core.service.net.config.TioConfig;

/**
 * Created by chengfeng on 2018/7/11.
 * 服务端启动类
 */
@Component
public class ServerService {

    @Resource
    private ServerGroupContext serverGroupContext;
    @Autowired
    private TioConfig tioConfig;

    @PostConstruct
    public void start() throws IOException {
        AioServer aioServer = new AioServer(serverGroupContext);
        //本机启动服务
        aioServer.start(tioConfig.getServerIp(), tioConfig.getServerPort());
    }
}
