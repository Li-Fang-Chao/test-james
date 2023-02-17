package james.li.concurrencyinpractice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 
 * @author jamli
 *
 */
public class Task003 {

	public static void main(String[] args) throws InterruptedException {

		Map<Object, Object> map = new HashMap<Object, Object>();

		System.out.println("validating HashMap");
		validateMapEntrySize(map);

		Map<Object, Object> threadSafeMap = new ThreadSafeMapDecorator();
		System.out.println("validating ThreadSafeMapDecorator");
		validateMapEntrySize(threadSafeMap);
	}

	/**
	 * validate the map is thread safe by adding an initial value to the map, and
	 * check that entry is always there when adding new values to the map
	 * 
	 * @param map
	 * @throws InterruptedException
	 */
	private static void validateMapEntrySize(Map<Object, Object> map) throws InterruptedException {

		/**
		 * Add initial value to the map
		 */
		map.put("initialValue", "Task003");

		/**
		 * Adding 100,000 entries to the map
		 */
		Runnable addValuesToMap = () -> {
			IntStream.range(0, 100000).forEach(number -> {
				map.put(number, number);
			});
		};

		new Thread(addValuesToMap).start();

		/**
		 * check that the initial value is always there for HashMap this will fail
		 * because by adding the new entries to the map, HashMap will expand the size
		 * and lost the initial entry when this is in process is in progress
		 */
		int i = 0;
		while (i++ < 1000000) {
			if (map.get("initialValue") == null) {
				System.out.println("initialValue is lost at the check " + i + ". Current Map Size " + map.size());
			}
		}
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
