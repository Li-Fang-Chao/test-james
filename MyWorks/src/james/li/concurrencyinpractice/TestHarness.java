package james.li.concurrencyinpractice;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestHarness {

	public Duration timeTasks(int nThreads, final Runnable task) throws InterruptedException {

		return timeTasks(nThreads, task, 0);

	}

	public Duration timeTasks(int nThreads, final Runnable task, long timeoutInSeconds) throws InterruptedException {

		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThreads);

		for (int i = 0; i < nThreads; i++) {
			Thread t = new Thread() {
				public void run() {
					try {
						startGate.await();
						try {
							if (timeoutInSeconds > 0) {
								timedRun(task, timeoutInSeconds);
							} else {
								task.run();
							}

						} finally {
							endGate.countDown();
						}
					} catch (InterruptedException ignored) {
					}
				}

				/**
				 * Run the task with the give timeout value
				 * 
				 * @param task
				 * @param timeoutInSeconds
				 * @throws InterruptedException
				 */
				private void timedRun(final Runnable task, long timeoutInSeconds) throws InterruptedException {
					ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
					Future<?> future = singleExecutor.submit(task);

					try {
						future.get(timeoutInSeconds, TimeUnit.SECONDS);
					} catch (ExecutionException executionException) {
						executionException.printStackTrace();
					} catch (TimeoutException timeoutException) {
						future.cancel(true);
					} finally {
						singleExecutor.shutdown();
					}
				}
			};
			t.start();

		}
		long start = System.nanoTime();
		startGate.countDown();
		endGate.await();
		long end = System.nanoTime();
		return Duration.ofNanos(end - start);

	}
}