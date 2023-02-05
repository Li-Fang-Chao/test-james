package james.li;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will use a MessageDigest instance to digest the same string 3 times. All 3 theads will use the same digestor
 * @author jamli
 *
 */
public class UnsafeThread {
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		

		/**
		 * Initialize the digestor outside of the runnable, so that all of the threads will use the same digestor
		 */
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		Runnable digestMyName = () -> {
			byte[] digestedName = messageDigest.digest("James".getBytes());
			System.out.println(Arrays.toString(digestedName));
		};
		
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(digestMyName);
		executorService.execute(digestMyName);
		executorService.execute(digestMyName);
		executorService.shutdown();
		
	}
	
}
