package com.phoenix.blockchain.common.util;

import java.text.MessageFormat;

import org.slf4j.Logger;

/**
 * Created by chengfeng on 2018/7/11.
 *
 * 日志打印工具类
 */
public class LogUtils {
    /**
     * INFO
     *
     * @param logger
     * @param logInfo
     */
    public static void info(Logger logger, String logInfo) {
        if (logger.isInfoEnabled()) {
            logger.info(logInfo);
        }
    }

    /**
     * INFO
     *
     * @param logger
     * @param temp
     * @param obj
     */
    public static void info(Logger logger, String temp, Object... obj) {
        if (logger.isInfoEnabled()) {
            logger.info(getLogString(temp, obj));
        }
    }

    /**
     * DEBUG
     *
     * @param logger
     * @param temp
     * @param obj
     */
    public static void debug(Logger logger, String temp, Object... obj) {
        if (logger.isDebugEnabled()) {
            logger.debug(getLogString(temp, obj));
        }
    }

    /**
     * WARN
     *
     * @param logger
     * @param temp
     * @param obj
     */
    public static void warn(Logger logger, String temp, Object... obj) {
        warn(logger, null, temp, obj);
    }

    /**
     * WARN
     *
     * @param logger
     * @param e
     * @param template
     * @param obj
     */
    public static void warn(Logger logger, Throwable e, String template, Object... obj) {
        warn(logger, e, getLogString(template, obj));
    }

    /**
     * WARN
     *
     * @param logger
     * @param e
     * @param message
     */
    public static void warn(Logger logger, Throwable e, String message) {
        if (e == null) {
            logger.warn(getLogString(message));
        } else {
            logger.warn(getLogString(message), e);
        }
    }

    /**
     * 日志生成
     *
     * @param template
     * @param values
     * @return
     */
    private static String getLogString(String template, Object... values) {
        return MessageFormat.format(template, values);
    }

}
