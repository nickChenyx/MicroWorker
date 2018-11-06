package com.nickchen.microworker.queue;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储任务消息的队列
 * 目标：
 * 1、并发安全
 * 2、能够根据任务不同区分队列
 * 3、有任务上下文信息，可以用来定制任务状态
 * 4、待续
 *
 * TODO workerPool 中 XQueue 泛型
 *
 * @author  nickChen
 * @date 2018年10月11日
 */
public class MsgQueue {

    /**
     * String: 任务名
     * ConcurrentLinkedDeque: 任务队列
     */
    private ConcurrentHashMap<String, XQueue<?>> workerPool = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, QueueContext> workPoolContext = new ConcurrentHashMap<>();

    public static final String TASK_OVER = "QC-Task-Over";

    public XQueue getQueue(String taskName) {
        return workerPool.get(taskName);
    }

    public Optional<XQueue> getQueueOptional(String taskName) {
        return Optional.ofNullable(workerPool.get(taskName));
    }

    /**
     * 使用默认的 {@link SimpleXQueue} 作为任务队列
     *
     * @param taskName taskName
     * @return
     */
    public boolean registerTask(String taskName) {
        return registerTask(taskName, new SimpleXQueue());
    }

    /**
     * 如果 task 已经存在，返回 false
     * @param taskName taskName
     * @return
     */
    public boolean registerTask(String taskName, XQueue queue) {
        XQueue xQueue = workerPool.putIfAbsent(taskName, queue);
        boolean queueNotExists = xQueue == null;
        if (queueNotExists) {
            workPoolContext.put(taskName, new QueueContext());
        }
        return queueNotExists;
    }

    /**
     * 给任务设置上下文信息
     * @param taskName
     * @param key
     * @param value
     */
    public void setQueueContextValue(String taskName, String key, String value) {
        workPoolContext.get(taskName).set(key, value);
    }

    /**
     * 获取任务上下文信息
     * @param taskName
     * @param key
     * @return
     */
    public Object getQueueContextValue(String taskName, String key) {
        return workPoolContext.get(taskName).get(key);
    }

    /**
     * 返回任务上下文
     * @param taskName
     * @return
     */
    public QueueContext getQueueContext(String taskName) {
        return workPoolContext.get(taskName);
    }

    /**
     * 设置任务结束上下文
     * @param taskName
     */
    public void setTaskOver(String taskName) {
        workPoolContext.get(taskName).set(TASK_OVER, true);
    }

    /**
     * 判断任务是否结束
     * @param taskName
     * @return
     */
    public boolean isTaskOver(String taskName) {
        return Boolean.TRUE.equals(workPoolContext.get(taskName).get(TASK_OVER));
    }
}
