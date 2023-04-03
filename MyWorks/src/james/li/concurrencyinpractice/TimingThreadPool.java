package james.li.concurrencyinpractice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class TimingThreadPool extends ThreadPoolExecutor {
	
	

	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	
	private final Logger log = Logger.getLogger("TimingThreadPool");
	private final AtomicLong numTasks = new AtomicLong();
	private final AtomicLong totalTime = new AtomicLong();
	
	public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean shouldStartAllCoreThreads) {
		
		super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
		
		if(shouldStartAllCoreThreads) {
			int prestartAllCoreThreads = prestartAllCoreThreads();
			log.fine(String.format("Started %s threads", prestartAllCoreThreads));

		}
			
	}

	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		log.fine(String.format("Thread %s: start %s", t, r));
		startTime.set(System.nanoTime());
	}

	protected void afterExecute(Runnable r, Throwable t) {
		try {
			long endTime = System.nanoTime();
			long taskTime = endTime - startTime.get();
			numTasks.incrementAndGet();
			totalTime.addAndGet(taskTime);
			log.fine(String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
		} finally {
			super.afterExecute(r, t);
		}
	}

	protected void terminated() {
		try {
			log.info(String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
		} finally {
			super.terminated();
		}
	}
}
