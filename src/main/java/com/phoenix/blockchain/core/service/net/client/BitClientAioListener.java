package com.phoenix.blockchain.core.service.net.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.core.service.net.config.TioConfig;

/**
 * Created by chengfeng on 2018/7/11.
 */
@Component
public class BitClientAioListener implements ClientAioListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitClientAioListener.class);


    @Autowired
    private TioConfig tioConfig;


    @Override
    public void onAfterClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove)
            throws Exception {

        LogUtils.info(LOGGER, "连接关闭.client: {0}, server: {1}", channelContext.getClientNode(), channelContext.getServerNode());
        Aio.unbindGroup(channelContext);
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)
            throws Exception {

        if (isConnected || isReconnect) {
            LogUtils.info(LOGGER, "连接成功：client: {0}, server: {1}", channelContext.getClientNode(), channelContext
                    .getServerNode());
            Aio.bindGroup(channelContext, tioConfig.getClientGroupName());
        } else {
            LogUtils.info(LOGGER, "连接失败：client: {0}, server: {1}", channelContext.getClientNode(), channelContext.getServerNode());
        }
    }

    @Override
    public void onAfterReceived(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {

    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {

    }
}
