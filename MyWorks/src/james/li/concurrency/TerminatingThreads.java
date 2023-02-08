package james.li.concurrency;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will use a MessageDigest instance to digest the same string 3
 * times. However, each time a new MessageDigest instance will be created to
 * make it threadsafe
 * 
 * @author jamli
 *
 */
public class TerminatingThreads {

	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {

		CountDownLatch countDown = new CountDownLatch(3);
		
		Runnable digestMyName = () -> {
			byte[] digestedName;
			try {
				/**
				 * Initialize the digestor inside the runnable, so each runnable will have it's
				 * own digestor
				 */
				digestedName = MessageDigest.getInstance("SHA-256").digest("James".getBytes());
				System.out.println(Arrays.toString(digestedName));
				countDown.countDown();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};

		ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(digestMyName);
		executorService.execute(digestMyName);
		executorService.execute(digestMyName);
		executorService.shutdown();

		/**
		 * Solution 1 - use awaitTermination
		 */
		// executorService.awaitTermination(60, TimeUnit.SECONDS);
		
		/**
		 * Solution 2 - use CountDownLatch
		 */
		countDown.await();

		System.out.println("Program is terminated");

	}

}
