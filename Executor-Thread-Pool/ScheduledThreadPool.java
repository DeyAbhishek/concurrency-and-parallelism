// Link to compiled code: http://goo.gl/Pk3idY

import java.util.concurrent.*;
public class ScheduledThreadPool {

 

    public static void main(String args[]) {

       ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(5);

ScheduledFuture scheduledFuture =
    scheduledExecutorService.schedule(new Callable() {
        public Object call() throws Exception {
            System.out.println("Executed!");
            return "Called!";
        }
    },
    5,
    TimeUnit.SECONDS);
  try{
    System.out.println("result = " + scheduledFuture.get());  // Will be printed after 5 seconds
  }catch(Exception ex){}
  finally{
      scheduledExecutorService.shutdown();
      }
    }

   

}

