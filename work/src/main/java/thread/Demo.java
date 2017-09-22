package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Demo {  
    public static void main(String[] args) {  
        // 线程操作安全队列，装载数据  
        Queue<String> queue = new ConcurrentLinkedQueue<String>();  
  
        // 消费者线程：不断的消费队列中的数据  
        // 该线程不停的从队列中取出队列中最头部的数据  
        new Thread(new Runnable() {  
            @Override  
            public void run() {  
                while (true) {  
                    // 从队列的头部取出并删除该条数据  
                    String s = queue.poll();  
                    System.out.println(System.currentTimeMillis() + "取出数据:" + s); 
                    if (s != null) {  
                        System.out.println(System.currentTimeMillis() + "取出数据:" + s);  
                    }  
                }  
            }  
  
        }).start();  
  
        // 生产者线程A：不断的生产单个数据并装入队列中  
        // 该线程模拟不停的在队列中装入一个数据  
        new Thread(new Runnable() {  
            private int count = 0;  
            private int number = 0;  
  
            @Override  
            public void run() {  
                while (true) {  
                    number = count++;  
                    System.out.println("装载数据：" + number);  
                    queue.add(String.valueOf(number));  
  
                    try {  
                        Thread.sleep(5000);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
  
        }).start();  
    }  
}  