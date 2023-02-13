package james.li.concurrencyinpractice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author jamli
 *
 */
public class JavaConcurrencyInPractice_Listing3_6 {

	public static void main(String[] args) throws InterruptedException {

		UnsafeStatesFinal unsafeFinalStates = new UnsafeStatesFinal();
		UnsafeStates unsafeStates = new UnsafeStates();
		ImmutableStates unmodifiableState = new ImmutableStates();

		Runnable printValues = () -> {
			System.out.println("Printing states array");
			for (String state : unsafeStates.getStates()) {
				System.out.println(state);

			}

			System.out.println("Printing final states array");
			for (String state : unsafeFinalStates.getStates()) {
				System.out.println(state);

			}

			System.out.println("Printing immutable states array");
			for (String state : unmodifiableState.getStates()) {
				System.out.println(state);

			}
		};
		
		unsafeStates.getStates()[0] = "Value Changed";
		unsafeFinalStates.getStates()[0] = "Value Changed";
		unmodifiableState.getStates()[0] = "Value Changed";
		try {
			/**
			 * this line will throw an exception, telling you the values can't be changed
			 * with UnsupportedOperationException
			 */
			unmodifiableState.getStatesAsList().set(0, "Value Changed");
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
		
		new Thread(printValues).run();

	}

}

/**
 * states are made public in this class, values can be changed
 * 
 * @author jamli
 *
 */
class UnsafeStates {
	private String[] states = new String[] { "V1", "V2" };

	public String[] getStates() {
		return states;
	}

}

/**
 * even declared as final, still can be changed
 * 
 * @author jamli
 *
 */
class UnsafeStatesFinal {
	private String[] states = new String[] { "V1", "V2" };

	public String[] getStates() {
		return states;
	}
}

/**
 * return the states as a immutable list
 * 
 * @author jamli
 *
 */
class ImmutableStates {
	private String[] states = new String[] { "V1", "V2" };
	public String[] getStates() {
		return Arrays.copyOf(states, states.length);
	}

	public List<String> getStatesAsList() {
		return Collections.unmodifiableList(Arrays.asList(states));
	}
}