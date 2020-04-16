package task;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件扫描
 *
 * @author haozhang
 * @date 2020/03/01
 */
public class FileScanner {

    /**
     * 一种快捷创建方式
     */
     private ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * 计数器，不传入数值默认初始化为0
     */
    private volatile AtomicInteger count = new AtomicInteger();

    /**
     * 第二种实现：await()阻塞等待
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private ScanCallback callback;

    public FileScanner(ScanCallback callback) {
        this.callback = callback;
    }

    /**
     * 扫描文件目录
     * 最开始不知道有多少子文件夹，不知道应该启动多少个线程
     * @param path
     */
    public void scan(String path) {
        //启动根目录扫描任务，计数器++i操作
        count.incrementAndGet();
        doScan(new File(path));
    }

    /**
     * 扫描文件夹
     * @param dir 待处理的文件夹
     */
    private void doScan(File dir) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //文件保存操作
                    callback.callback(dir);

                    //下一级文件和文件夹
                    File[] children = dir.listFiles();

                    if (children != null) {
                        for (File child : children) {
                            //如果是文件夹，递归处理
                            if (child.isDirectory()) {

                                //启动子文件夹扫描任务，计数器++i操作
                                count.incrementAndGet();
                                System.out.println("当前任务数" + count.get());
                                doScan(child);
                            }
                        }
                    }
                } finally {
                    //保证线程计数不管是否出现异常，都能够进行-1操作
                    int r = count.decrementAndGet();
                    if (r == 0) {
                        //第二种实现
                        latch.countDown();
                    }
                }
            }
        });
    }


    /**
     * 等待扫描任务结束（scan方法）
     * 多线程的任务等待：thread.start();
     * 1.join()：需要使用线程类的引用对象
     * 2.wait()线程之间的等待，
     */
    public void waitFinish() throws InterruptedException {
        try {
            //第二种实现
            latch.await();
        } finally {
            //阻塞等待直到任务完成，完成后需要关闭线程池
            System.out.println("关闭线程池！");
            pool.shutdownNow();
        }
    }
}
