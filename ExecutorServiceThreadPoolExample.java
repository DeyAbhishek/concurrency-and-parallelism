// Link to compiled code: http://goo.gl/yeVBRf

import java.util.concurrent.*;
public class ExecutorServiceThreadPoolExample {
    public static void main(String args[]) {
       ExecutorService service = Executors.newFixedThreadPool(10);
       for (int i =0; i<20; i++){
           service.submit(new Task(i));
       }
       service.shutdown();
    }
}

final class Task implements Runnable{
    private int taskId;
    public Task(int id){
        this.taskId = id;
    }
    @Override
    public void run() {
        System.out.println("Task ID : " + this.taskId +" performed by " + Thread.currentThread().getName());
    }
}


/*  OUTPUT:
Task ID : 0 performed by pool-1-thread-1                                                                                                                                                                                                                
Task ID : 4 performed by pool-1-thread-5                                                                                                                                                                                                                
Task ID : 3 performed by pool-1-thread-4                                                                                                                                                                                                                
Task ID : 2 performed by pool-1-thread-3                                                                                                                                                                                                                
Task ID : 1 performed by pool-1-thread-2                                                                                                                                                                                                                
Task ID : 5 performed by pool-1-thread-6                                                                                                                                                                                                                
Task ID : 6 performed by pool-1-thread-7                                                                                                                                                                                                                
Task ID : 7 performed by pool-1-thread-8                                                                                                                                                                                                                
Task ID : 9 performed by pool-1-thread-10                                                                                                                                                                                                               
Task ID : 8 performed by pool-1-thread-9                                                                                                                                                                                                                
Task ID : 15 performed by pool-1-thread-3                                                                                                                                                                                                               
Task ID : 14 performed by pool-1-thread-10                                                                                                                                                                                                              
Task ID : 13 performed by pool-1-thread-5                                                                                                                                                                                                               
Task ID : 12 performed by pool-1-thread-4                                                                                                                                                                                                               
Task ID : 10 performed by pool-1-thread-1                                                                                                                                                                                                               
Task ID : 11 performed by pool-1-thread-8                                                                                                                                                                                                               
Task ID : 19 performed by pool-1-thread-7                                                                                                                                                                                                               
Task ID : 18 performed by pool-1-thread-6                                                                                                                                                                                                               
Task ID : 17 performed by pool-1-thread-2                                                                                                                                                                                                               
Task ID : 16 performed by pool-1-thread-9
*/
