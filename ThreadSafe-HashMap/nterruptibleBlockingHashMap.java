import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class InterruptibleBlockingHashMap<K, V> implements Map<K, V> {
Map<K, V> delegate = new HashMap<K, V>();
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();
@Override
public void clear() {
try {
lock.lock();
delegate.clear();
} finally {
lock.unlock();
}
}
@Override
public boolean containsKey(Object arg0) {
try {
lock.lock();
return delegate.containsKey(arg0);
} finally {
lock.unlock();
}
}
@Override
public boolean containsValue(Object arg0) {
try {
lock.lock();
return delegate.containsValue(arg0);
} finally {
lock.unlock();
}
}
@Override
public Set<java.util.Map.Entry<K, V>> entrySet() {
try {
lock.lock();
return Collections.unmodifiableSet(delegate.entrySet());
} finally {
lock.unlock();
}
}
public V getInterruptably(Object arg0) throws InterruptedException {
try {
lock.lock();
while (!delegate.containsKey(arg0)) condition.await();
return delegate.get(arg0);
} finally {
lock.unlock();
}
}
public V tryGet(Object arg0){
boolean gotLock = false;
try{
gotLock = lock.tryLock();
return delegate.get(arg0);
} finally {
if (gotLock)
lock.unlock();
}
}
public V tryGet(Object arg0, long time, TimeUnit unit) throws InterruptedException{
try{
lock.tryLock(time, unit);
return delegate.get(arg0);
} finally {
lock.unlock();
}
}
@Override
public V get(Object arg0) {
try {
lock.lock();
while (!delegate.containsKey(arg0)) condition.await();
return delegate.get(arg0);
} catch (InterruptedException e) {
return null;
} finally {
lock.unlock();
}
}
@Override
public boolean isEmpty() {
try {
lock.lock();
return delegate.isEmpty();
} finally {
lock.unlock();
}
}
@Override
public boolean equals(Object obj) {
try {
lock.lock();
return delegate.equals(obj);
} finally {
lock.unlock();
}
}
@Override
public int hashCode() {
try {
return delegate.hashCode();
} finally {
lock.unlock();
}
}
@Override
public Set<K> keySet() {
try {
lock.lock();
return Collections.unmodifiableSet(delegate.keySet());
} finally {
lock.unlock();
}
}
@Override
public V put(K arg0, V arg1) {
try {
lock.lock();
V ret = delegate.put(arg0, arg1);
condition.signal();
return ret;
} finally {
lock.unlock();
}
}
@Override
public void putAll(Map<? extends K, ? extends V> arg0) {
try {
lock.lock();
lock.lock();
delegate.putAll(arg0);
condition.signal();
} finally {
lock.unlock();
}
}
@Override
public V remove(Object arg0) {
try {
lock.lock();
return delegate.remove(arg0);
} finally {
lock.unlock();
}
}
@Override
public int size() {
try {
lock.lock();
return delegate.size();
} finally {
lock.unlock();
}
}
@Override
public Collection<V> values() {
try {
lock.lock();
return Collections.unmodifiableCollection(delegate.values());
} finally {
lock.unlock();
}
}
}
