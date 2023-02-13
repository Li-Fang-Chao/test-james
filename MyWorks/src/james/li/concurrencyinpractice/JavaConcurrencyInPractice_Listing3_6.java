package james.li.concurrencyinpractice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author jamli
 *
 */
public class JavaConcurrencyInPractice_Listing3_6 {

	public static void main(String[] args) throws InterruptedException {

		UnsafeFinalStates unsafeFinalStates = new UnsafeFinalStates();
		UnsafeStates unsafeStates = new UnsafeStates();
		UnsafeImmutableStates unmodifiableState = new UnsafeImmutableStates();

		CountDownLatch latch = new CountDownLatch(1);

		Runnable countDown = () -> {
			for (String state : unsafeFinalStates.getStates()) {
				System.out.println(state);

			}

			for (String state : unsafeStates.getStates()) {
				System.out.println(state);

			}
			
			for (String state : unmodifiableState.getStates()) {
				System.out.println(state);

			}
			latch.countDown();
		};

		unsafeStates.getStates()[0] = "AK->X1";
		unsafeFinalStates.getStates()[0] = "LN->X1";
		unmodifiableState.getStates()[0] = "SD->X1";
		try {
			/**
			 * this line will throw an exception, telling you the values can't be changed with UnsupportedOperationException
			 */
			unmodifiableState.getStatesAsList().set(0, "SD1->X1");
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}

		ExecutorService executorService = Executors.newFixedThreadPool(1);
		executorService.execute(countDown);
		executorService.shutdown();

		latch.await();
	}

}

/**
 * states are made public in this class, values can be changed
 * @author jamli
 *
 */
class UnsafeStates {
	private String[] states = new String[] { "AK", "AL" };

	public String[] getStates() {
		return states;
	}

}

/**
 * even declared as final, still can be changed
 * @author jamli
 *
 */
class UnsafeFinalStates {
	private final String[] states = new String[] { "LN", "CC" };

	public String[] getStates() {
		return states;
	}
}

/**
 * return the states as a immutable list
 * @author jamli
 *
 */
class UnsafeImmutableStates {
	private final String[] states = new String[] { "SD", "HN" };

	public String[] getStates() {
		return Arrays.copyOf(states, states.length);
	}
	
	public List<String> getStatesAsList() {
		return Collections.unmodifiableList(Arrays.asList(states));
	}
}