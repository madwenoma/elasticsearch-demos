package com.lee.learn.disruptor;

/**
 * @description disruptor代码样例。每10ms向disruptor中插入一个元素，消费者读取数据，并打印到终端
 */

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

public class DisruptorMain {


    static class ElementHandler implements WorkHandler<Element> {

//        @Override
//        public void onEvent(Element element, long l, boolean b) throws Exception {
//            long threadId = Thread.currentThread().getId();
//            System.out.println("Element: " + threadId + " " + element.get());
//        }

        @Override
        public void onEvent(Element element) {
//            System.out.println(element.get());
            if (element.get() == 999998) {
                System.out.println("999998 end:" + System.currentTimeMillis());
            }
        }
    }
    public static void main(String[] args) throws Exception {

        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread");
            }
        };
        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };
        // 处理Event的handler
//        EventHandler<Element> handler = new ElementHandler();
        // 阻塞策略
        SleepingWaitStrategy strategy = new SleepingWaitStrategy();
        // 指定RingBuffer的大小
        int bufferSize = 1024 * 1024;
        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor(factory, bufferSize, threadFactory,
                ProducerType.SINGLE, strategy);
        // 设置EventHandler
        ElementHandler[] handlers = new ElementHandler[10];
        for (int i = 0; i < 10; i++) {
            handlers[i] = new ElementHandler();
        }
//        disruptor.handleEventsWith(handlers);
        disruptor.handleEventsWithWorkerPool(handlers);
        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        long start = System.currentTimeMillis();
        System.out.println("start " + start);
        for (int l = 0; l < 999999; l++) {
            // 获取下一个可用位置的下标
            long sequence = ringBuffer.next();
            try {
                // 返回可用位置的元素
                Element event = ringBuffer.get(sequence);
                // 设置该位置元素的值
                event.set(l);
            } finally {
                ringBuffer.publish(sequence);
            }
//            Thread.sleep(1);
        }

        disruptor.shutdown();

        System.in.read();
    }

    // 队列中的元素
    static class Element {
        private int value;

        public int get() {
            return value;
        }

        public void set(int value) {
            this.value = value;
        }

    }        // 生产者的线程工厂


}