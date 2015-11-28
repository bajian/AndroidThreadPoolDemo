package com.example.bajian.androidthreadpooldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 本线程池demo学习自多个博客
 * http://www.trinea.cn/android/java-android-thread-pool/
 * http://www.trinea.cn/android/database-performance/
 * http://blog.csdn.net/caiwenfeng_for_23/article/details/47057837
 */
public class MainActivity extends AppCompatActivity {

    private Runnable r;
    private ScheduledExecutorService scheduledThreadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    /**
     * 创建一个 可缓存&无限大 线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。示例代码如下：
     * 次例子可以通过id清晰看出 线程复用 情况
     * @param v
     */
    public void newCachedThreadPool(View v){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    System.out.println(index);
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(index * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * @param v
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * 定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()
     */
    public void newFixedThreadPool(View v){
        int num=Runtime.getRuntime().availableProcessors();
        System.out.println("Runtime.getRuntime().availableProcessors()->"+num);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void stop(View v){
        if (scheduledThreadPool!=null)
            scheduledThreadPool.shutdownNow();
        //        scheduledThreadPool.shutdown();
//        scheduledThreadPool.shutdownNow();
    }
    /**
     * Timer与ScheduledExecutorService间的抉择 http://www.cnblogs.com/androidez/archive/2013/03/12/2955842.html
     *创建一个定长线程池，支持定时及周期性任务执行。
     * 其实当执行任务的时间大于我们指定的间隔时间时，它并不会在指定间隔时开辟一个新的线程并发执行这个任务。而是等待该线程执行完毕
     *  @param v
     */
    public void newScheduledThreadPool(View v){

        scheduledThreadPool = Executors.newScheduledThreadPool(5);

        //延迟3秒执行。
/*
        scheduledThreadPool.schedule(new Runnable() {

            @Override
            public void run() {
                System.out.println("delay 3 seconds");
            }
        }, 3, TimeUnit.SECONDS);
*/

        r=new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);//其实当执行任务的时间大于我们指定的间隔时间时，它并不会在指定间隔时开辟一个新的线程并发执行这个任务。而是等待该线程执行完毕
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }
                System.out.println("delay 1 seconds, and excute every 3 seconds");
            }
        };
//        表示延迟1秒后每3秒执行一次。
//        ScheduledExecutorService比Timer更安全，功能更强大，后面会有一个demo对比。
//        在执行任务时，如果指定的计划执行时间scheduledExecutionTime<= systemCurrentTime，则task会首先按执行一次；然后按照执行时间、系统当前时间和period参数计算出过期该执行的次数，计算按照： (systemCurrentTime-scheduledExecutionTime)/period，再次执行计算出的次数；最后按period参数固定重复执行
        scheduledThreadPool.scheduleAtFixedRate(r, 1, 3, TimeUnit.SECONDS);
/*        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("excute every 2 seconds");
            }
        }, 1, 2, TimeUnit.SECONDS);*/

    }

    /**
     *创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     * Android中单线程可用于数据库操作，文件操作，应用批量安装，应用批量删除等不适合并发但可能IO阻塞性及影响UI线程响应的操作
     *  @param v
     */
    public void newSingleThreadExecutor(View v){

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
