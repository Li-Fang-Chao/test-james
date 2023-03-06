package james.li.concurrencyinpractice;

import java.util.HashMap;
import java.util.Map;
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
		validateMapThreadSafty(map);

		Map<Object, Object> threadSafeMap = new ImprovedMap(map);
		System.out.println("validating ImprovedMap");
		validateMapThreadSafty(threadSafeMap);
	}

	/**
	 * validate the map is thread safe by adding an initial value to the map, and
	 * check that entry is always there when adding new values to the map
	 * 
	 * @param map
	 * @throws InterruptedException
	 */
	private static void validateMapThreadSafty(Map<Object, Object> map) throws InterruptedException {
		
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
		 * checking that the initial value is always there. For HashMap this will fail
		 * because by adding the new entries to the map, HashMap will expand the size
		 * of the internal buckets and lost the initial entry during this process
		 */
		int i = 0;
		while (i++ < 1000000) {
			if (map.get("initialValue") == null) {
				System.out.println("initialValue is lost at the check " + i + ". Current Map Size " + map.size());
			}
		}
	}

}