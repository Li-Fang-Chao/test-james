package james.li;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will use a MessageDigest instance to digest the same string 3 times. However, each time a new MessageDigest instance will be created to make it threadsafe
 * @author jamli
 *
 */
public class SafeThread {
	
	public static void main(String[] args) throws NoSuchAlgorithmException {Runnable digestMyName = () -> {
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
	
	}

}
