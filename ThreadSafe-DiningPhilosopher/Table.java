package philosophers;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.System.out;
public class Table {
int numSeats;
Thread[] phils;
final Lock[] forks;
final Random r = new Random();
static final int MAXMSECS = 1000;
final Logger log;
class Philosopher implements Runnable{
final int seat; //where this philosopher is seated, seats numbered from 0 to numSeats-1
Philosopher(int seat){
this.seat = seat;
}
void think(){
log.thinks(seat);
try{
Thread.sleep(r.nextInt(MAXMSECS));
}
catch(InterruptedException e){
Thread.currentThread().interrupt();
}
}
void eat(){
// Need left fork & right fork
int leftFork = seat;
int rightFork = (seat+1) % numSeats;
// Resource ordering for deadlock avoidance
if (leftFork < rightFork) {
forks[leftFork].lock();
forks[rightFork].lock();
} else {
forks[rightFork].lock();
forks[leftFork].lock();
}
log.eats(seat);
try{
Thread.sleep(r.nextInt(MAXMSECS));
}
catch(InterruptedException e){
Thread.currentThread().interrupt();
} finally {
forks[leftFork].unlock();
forks[rightFork].unlock();
log.thinks(seat);
}
}
public void run(){
while(!Thread.currentThread().isInterrupted()){
think();
if (Thread.currentThread().isInterrupted())
return;
eat();
}
}
}
Table(int numSeats, Logger log) throws InterruptedException{
this.numSeats = numSeats; //set the number of seats around the table. Must be at least 2
this.log = log;
phils = new Thread[numSeats]; //create a Thread for each philosopher
for (int i = 0; i < numSeats; i++)
phils[i] = new Thread(new Philosopher(i));
// Fork i is common between philosopher i and i+1 (i+1 wraps around)
forks = new ReentrantLock[numSeats];
for (int i = 0; i < forks.length; i++)
forks[i] = new ReentrantLock();
}
void startDining(){
for (int i = 0; i < numSeats; i++) phils[i].start();
}
void closeRestaurant() throws InterruptedException{
for (int i = 0; i < numSeats; i++) phils[i].interrupt();
for (int i = 0; i < numSeats; i++) phils[i].join();
}
public static void main(String[] args) throws InterruptedException{
if (args.length < 2){
out.println("usage: java Table numSeats timesToEat");
return;
}
int numPhils = Integer.parseInt(args[0]);
int milliToEat = Integer.parseInt(args[1]);
Logger log = new CheckingLogger();
Table table = new Table(numPhils, log);
table.startDining();
Thread.sleep(milliToEat);
table.closeRestaurant();
System.out.println("restaurant closed. Behavior was " + (log.isCorrect()?"correct":"incorrect"));
}
}
