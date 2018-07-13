package com.phoenix.blockchain.core.service.net.config;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.phoenix.blockchain.core.service.net.model.Node;

/**
 * Created by chengfeng on 2018/7/11.
 */
@Configuration
@ConfigurationProperties(prefix = "tio")
public class TioConfig {
    /**
     * 心跳包时间间隔
     */
    @NotNull
    private int heartTimeout;
    /**
     * 客户端分组名称
     */
    @NotNull
    private String clientGroupName;
    /**
     * 服务端分组上下文名称
     */
    @NotNull
    private String serverGroupContextName;
    /**
     * 服务端监听端口
     */
    @NotNull
    private int serverPort;
    /**
     * 服务端绑定的 ip
     */
    private String serverIp;

    private List<Node> nodes;

    public int getHeartTimeout() {
        return heartTimeout;
    }

    public void setHeartTimeout(int heartTimeout) {
        this.heartTimeout = heartTimeout;
    }

    public String getClientGroupName() {
        return clientGroupName;
    }

    public void setClientGroupName(String clientGroupName) {
        this.clientGroupName = clientGroupName;
    }

    public String getServerGroupContextName() {
        return serverGroupContextName;
    }

    public void setServerGroupContextName(String serverGroupContextName) {
        this.serverGroupContextName = serverGroupContextName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
