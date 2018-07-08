package com.phoenix.blockchain.core.model;

import java.io.Serializable;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * Block model
 */
public class Block extends BaseDomain{

    private static final long serialVersionUID = 6750626743628253616L;

    private BlockHeader header;

    private BlockBody  body;

    public BlockHeader getHeader() {
        return header;
    }

    public void setHeader(BlockHeader header) {
        this.header = header;
    }

    public BlockBody getBody() {
        return body;
    }

    public void setBody(BlockBody body) {
        this.body = body;
    }


}
