package com.nickchen.microworker.queue;

/**
 * 对外暴露的队列接口
 *
 * @author  nickChen
 * @date 2018年10月11日
 */
public interface XQueue<T> {

    /**
     * 向队列尾插入数据
     * 如果插入失败返回 false
     * @return
     */
    boolean push(T t);

    /**
     * 抛出队首，若队列长度为 0，抛出 null
     * @return
     */
    T pop();

    /**
     * 获取队列长度，不确保 size 的准确性
     * @return
     */
    long size();
}
