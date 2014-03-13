package philosophers;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class CheckingLogger implements Logger {
private static class LoggedAction {
final public int seat;
final public boolean isEating;
public LoggedAction(int seat, boolean isEating){
this.seat = seat;
this.isEating = isEating;
}
}
private List<LoggedAction> actions = Collections.synchronizedList(new ArrayList<LoggedAction>());
private volatile int maxSeat = -1;
/**
* Error when
* Either neighbor of an eating philosopher is also eating
* Any Philosopher eats twice
*/
@Override
public boolean isCorrect() {
// Play back trace and record what each philosopher was last doing
// Assume, everyone is thinking, since that what they do first.
boolean[] wasEating = new boolean[maxSeat + 1];
for (LoggedAction a : actions){
if (a.isEating) {
if (wasEating[a.seat]) // If I was eating previously, error !
return false;
int lneighbor = a.seat - 1 < 0 ? maxSeat : a.seat - 1;
int rneighbor = (a.seat + 1) % (maxSeat + 1);
if (wasEating[lneighbor]) // Left neighbor was eating !
return false;
if (wasEating[rneighbor]) // Right neighbor was eating !
return false;
wasEating[a.seat] = true;
} else {
wasEating[a.seat] = false;
}
}
// Everthing A OK !
return true;
}
@Override
public void eats(int seat) {
actions.add(new LoggedAction(seat, true));
out.println("Philosopher " + seat + " is eating");
if (maxSeat < seat) maxSeat = seat;
}
@Override
public void thinks(int seat) {
actions.add(new LoggedAction(seat, false));
out.println("Philosopher " + seat + " is thinking");
if (maxSeat < seat) maxSeat = seat;
}
}
