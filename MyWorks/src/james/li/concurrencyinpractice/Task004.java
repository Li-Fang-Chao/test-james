package james.li.concurrencyinpractice;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * 
 * @author jamli
 *
 */
public class Task004 {

	public static void main(String[] args) throws InterruptedException {

		Map map = new HashMap();
		System.out.println("validating HashMap");
		validateMapEntrySize(map);
		Map<Object, Object> threadSafeMap = new ThreadSafeMapDecorator();
		System.out.println("validating ThreadSafeMapDecorator");
        validateMapEntrySize(threadSafeMap);
	}

	private static void validateMapEntrySize(Map<Object, Object> map) {
		CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
			try {
				IntStream.range(0,50000).forEach(number -> map.put(UUID.randomUUID().toString(), number));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
			try {
				IntStream.range(0,50000).forEach(number -> map.put(UUID.randomUUID().toString(), number));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        List<CompletableFuture<Void>> futures = List.of(future1, future2);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        System.out.println ("Map size: " + map.size());
	}
	
	

}

class ThreadSafeMapDecorator implements Map<Object, Object> {

	private Map<Object, Object> innerMap = new HashMap<Object, Object>();

	@Override
	public int size() {
		return innerMap.size();
	}

	@Override
	public synchronized boolean isEmpty() {
		return innerMap.isEmpty();
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return innerMap.containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return innerMap.containsValue(value);
	}

	@Override
	public synchronized Object get(Object key) {
		return innerMap.get(key);
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		return innerMap.put(key, value);
	}

	@Override
	public synchronized Object remove(Object key) {
		return innerMap.remove(key);
	}

	@Override
	public synchronized void putAll(Map m) {
		innerMap.putAll(m);
	}

	@Override
	public synchronized void clear() {
		innerMap.clear();
	}

	@Override
	public synchronized Set keySet() {
		return innerMap.keySet();
	}

	@Override
	public synchronized Collection values() {
		return innerMap.values();
	}

	@Override
	public synchronized Set entrySet() {
		return innerMap.entrySet();
	}

}
