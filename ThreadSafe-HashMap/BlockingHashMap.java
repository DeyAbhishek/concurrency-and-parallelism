import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class BlockingHashMap<K, V> implements Map<K, V> {
Map<K, V> delegate = new HashMap<K,V>();
@Override
synchronized public void clear() {
delegate.clear();
}
@Override
synchronized public boolean containsKey(Object arg0) {
return delegate.containsKey(arg0);
}
@Override
synchronized public boolean containsValue(Object arg0) {
return delegate.containsValue(arg0);
}
@Override
synchronized public Set<java.util.Map.Entry<K, V>> entrySet() {
return Collections.unmodifiableSet(delegate.entrySet());
}
@Override
synchronized public V get(Object arg0) {
try{
while (!delegate.containsKey(arg0)) wait();
} catch (InterruptedException e) {
return null;
}
return delegate.get(arg0);
}
@Override
synchronized public boolean isEmpty() {
return delegate.isEmpty();
}
@Override
synchronized public boolean equals(Object obj){
return delegate.equals(obj);
}
@Override
synchronized public int hashCode(){
return delegate.hashCode();
}
@Override
synchronized public Set<K> keySet() {
return Collections.unmodifiableSet(delegate.keySet());
}
@Override
synchronized public V put(K arg0, V arg1) {
V ret = delegate.put(arg0,arg1);
notifyAll();
return ret;
}
@Override
synchronized public void putAll(Map<? extends K, ? extends V> arg0) {
notifyAll();
}
@Override
synchronized public V remove(Object arg0) {
return delegate.remove(arg0);
}
@Override
synchronized public int size() {
return delegate.size();
}
@Override
synchronized public Collection<V> values() {
return Collections.unmodifiableCollection(delegate.values());
}
}
