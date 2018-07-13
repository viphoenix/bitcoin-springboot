package com.phoenix.blockchain.core.service.net;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import com.phoenix.blockchain.core.service.net.model.MessagePacket;

/**
 * Created by chengfeng on 2018/7/11.
 *
 * 解码/编码处理器
 */
public class BaseAioHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAioHandler.class);

    /**
     * 解码
     *
     * @param buffer
     * @param channelContext
     * @return
     * @throws AioDecodeException
     */
    public Packet decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {

        int readableLength = buffer.limit() - buffer.position();

        if (readableLength < MessagePacket.HEADER_LENGTH) {
            LOGGER.info("数据包非法");
            return null;
        }

        // 获取消息类型
        byte messageType = buffer.get();

        // 获取消息长度
        int messageLength = buffer.getInt();

        if (messageLength < 0) {
            throw new AioDecodeException("bodyLength [" + messageLength + "] is not right, remote:" + channelContext
                    .getClientNode());

        }

        // 获取数据包,处理拆包粘包的情况
        int avaliablePacketLength = readableLength - messageLength - MessagePacket.HEADER_LENGTH;

        if (avaliablePacketLength < 0) {
            LOGGER.info("半包,无法组装");
            return null;
        }

        MessagePacket packet = new MessagePacket();
        packet.setType(messageType);

        byte[] dst = new byte[messageLength];
        buffer.get(dst);
        packet.setBody(dst);


        return packet;
    }

    /**
     * 编码
     *
     * @param packet
     * @param groupContext
     * @param channelContext
     * @return
     */
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {

        MessagePacket messagePacket = (MessagePacket) packet;

        int bodyLength = 0;
        byte[] body = messagePacket.getBody();
        if (null != body) {
            bodyLength = body.length;
        }

        int packetLength = MessagePacket.HEADER_LENGTH + bodyLength;
        ByteBuffer buffer = ByteBuffer.allocate(packetLength);
        buffer.order(groupContext.getByteOrder());

        buffer.put(messagePacket.getType());
        buffer.putInt(bodyLength);

        if (null != body) {
            buffer.put(body);
        }

        return buffer;
    }

}
