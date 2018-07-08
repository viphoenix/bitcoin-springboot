package com.phoenix.blockchain.core.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by chengfeng on 2018/7/8.\
 *
 * 模型基类
 */
public class BaseDomain implements Serializable {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
