package com.lee.learn.disruptor;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.*;

public class QueuePerfom {

    public int produceLoop = 999999;
    public int queueSize = 10000;
    private int threadNum = 10;

    private CountDownLatch pLatch = new CountDownLatch(1);
    private CountDownLatch cLatch = new CountDownLatch(threadNum);

    class Producer implements Runnable {
        private BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            long threadId = Thread.currentThread().getId();
            try {
                for (int i = 0; i < produceLoop; i++) {
                    if (i == 99998) {
                        System.out.println(i);
                    }
                    this.queue.put(startTime + "-" + threadId + " something-" + i);
                }
                System.out.println("==put end");
                this.queue.put("end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            pLatch.countDown();
        }
    }

    class Consumer implements Runnable {
        private BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            long threadId = Thread.currentThread().getId();
//            while (!queue.isEmpty()) {
            while (true) {
                try {
                    String something = this.queue.take();
                    if (something.equals("end")) {
                        System.out.println(threadId + " consumed: " + something);
                        System.out.println(System.currentTimeMillis());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
//            System.out.println("empty");


        }
    }

    /**
     * 63
     * 859
     * 938
     */
    @Test
    public void count() {
        long arrQueueStart = Long.valueOf("1532145272640");
        long arrQueueend = Long.valueOf("1532145273875");
        System.out.println(arrQueueend - arrQueueStart);
        long linkedQueueStart = Long.valueOf("1532145293359");
        long linkedQueueend = Long.valueOf("1532145294640");
        System.out.println(linkedQueueend - linkedQueueStart);
        long disruptorQueueStart = Long.valueOf("1532146176468");
        long disruptorQueueend = Long.valueOf("1532146176750");
        System.out.println(disruptorQueueend - disruptorQueueStart);
    }

    public void testByQeueu(BlockingQueue<String> blockingQueue) throws InterruptedException {
        Producer p = new Producer(blockingQueue);
        ExecutorService producerPool = Executors.newFixedThreadPool(threadNum);
        long begin = System.currentTimeMillis();
        System.out.println(begin);
        for (int i = 0; i < 1; i++) {
            producerPool.submit(p);
        }
//        pLatch.await();
        ExecutorService consumerPool = Executors.newFixedThreadPool(threadNum);
        Consumer c = new Consumer(blockingQueue);
        for (int i = 0; i < threadNum; i++) {
            consumerPool.submit(c);
        }
        cLatch.await();
        System.out.println("cost time " + (System.currentTimeMillis() - begin) + "ms");
    }

    @Test
    public void testArrayListQueue() throws IOException, InterruptedException {
        ArrayBlockingQueue<String> arrQueue = new ArrayBlockingQueue<>(queueSize);
        testByQeueu(arrQueue);
//        System.in.read();
    }

    @Test
    public void testLinkedQueue() throws IOException, InterruptedException {
        LinkedBlockingQueue<String> linkQueue = new LinkedBlockingQueue<>(queueSize);
        testByQeueu(linkQueue);

    }

}
