package james.li;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class will use a MessageDigest instance to digest the same string 3 times. However, each time a new MessageDigest instance will be created to make it threadsafe
 * @author jamli
 *
 */
public class TerminatingThreads {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {Runnable digestMyName = () -> {
		byte[] digestedName;
		try {
			/**
			 * Initialize the digestor inside the runnable, so each runnable will have it's own digestor
			 */
			digestedName = MessageDigest.getInstance("SHA-256").digest("James".getBytes());
			System.out.println(Arrays.toString(digestedName));
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
	
	blockUntilAllThreadsDone(executorService);
	
	System.out.println("Program is terminated");
	
	}

	/**
	 * Use awaitTermination to wait until all threads execution are done
	 * @param executorService
	 * @throws InterruptedException
	 */
	private static void blockUntilAllThreadsDone(ExecutorService executorService) throws InterruptedException {
		executorService.awaitTermination(60, TimeUnit.SECONDS);		
	}

}
