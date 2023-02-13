package james.li.javaconcurrencyutil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
 * https://www.javatpoint.com/completablefuture-in-java TODO
 * https://juejin.cn/post/6970558076642394142
 * 
 * @author jamli
 *
 */
public class CompletableFutureTest {

	public static void main(String args[]) throws InterruptedException, ExecutionException {
		CompletableFuture<String> orgFuture = CompletableFuture.supplyAsync(
                ()->{
                    System.out.println("先执行第一个CompletableFuture方法任务");
                    return null;
                }
        );

        CompletableFuture<?> thenRunFuture = orgFuture.thenRun(() -> {
            System.out.println("接着执行第二个任务");
        });

        System.out.println(thenRunFuture.get());
	}

}
