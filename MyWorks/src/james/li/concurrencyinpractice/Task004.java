package james.li.concurrencyinpractice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author jamli
 *
 */
public class Task004 {

	public static void main(String[] args) throws InterruptedException {

		
		TestHarness tester = new TestHarness();
		
		System.out.println("Running 10 job with ImprovedMap " + tester.timeTasks(10, addValuesToMap(new ImprovedMap(new HashMap()))));
		System.out.println("Running 100 job with ImprovedMap " + tester.timeTasks(100, addValuesToMap(new ImprovedMap(new HashMap()))));
		System.out.println("Running 1000 job with ImprovedMap " + tester.timeTasks(1000, addValuesToMap(new ImprovedMap(new HashMap()))));
		
		System.out.println("Running 10 job with ConcurrentHashMap " + tester.timeTasks(10, addValuesToMap(new ConcurrentHashMap())));
		System.out.println("Running 100 job with ConcurrentHashMap " + tester.timeTasks(100, addValuesToMap(new ConcurrentHashMap())));
		System.out.println("Running 1000 job with ConcurrentHashMap " + tester.timeTasks(1000, addValuesToMap(new ConcurrentHashMap())));
		
		System.out.println("Running 10 job with Collections.synchronizedMap(new HashMap()) " + tester.timeTasks(10, addValuesToMap(Collections.synchronizedMap(new HashMap()))));
		System.out.println("Running 100 job with Collections.synchronizedMap(new HashMap()) " + tester.timeTasks(100, addValuesToMap(Collections.synchronizedMap(new HashMap()))));
		System.out.println("Running 1000 job with Collections.synchronizedMap(new HashMap()) " + tester.timeTasks(1000, addValuesToMap(Collections.synchronizedMap(new HashMap()))));
	}

	/**
	 * Add 10,000 entries to the given map
	 * @param amap
	 * @return the Runnable that runs this task
	 */
	private static Runnable addValuesToMap(Map amap) {
		Runnable addValuesToMap =() -> {
			int i = 0;
			while(i++ < 10000) {
				amap.put(i, UUID.randomUUID().toString());
			}
		};
		return addValuesToMap;
	}

}