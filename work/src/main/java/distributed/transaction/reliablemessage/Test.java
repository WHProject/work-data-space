package distributed.transaction.reliablemessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
	public static void main(String[] args) throws Exception {
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		ParticipatorA participatorA=new ParticipatorA();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				participatorA.execute();
			}
		});
		
		ParticipatorB participatorB=new ParticipatorB();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				participatorB.execute();
			}
		});
	}
}
