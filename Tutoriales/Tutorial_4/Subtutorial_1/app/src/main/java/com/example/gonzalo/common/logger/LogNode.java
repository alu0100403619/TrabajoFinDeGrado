package com.example.gonzalo.common.logger;

/**
 * Created by Gonzalo on 15/12/2014.
 */
public interface LogNode {
    public void println(int priority, String tag, String msg, Throwable tr);
}
