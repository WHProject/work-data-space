package distributed.transaction.reliablemessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageService {
	private static List<String> messageList = new ArrayList<String>(100);

	private static BlockingQueue<String> failedQueue = new ArrayBlockingQueue<String>(100);

	static {
		ExecutorService executorService = Executors.newCachedThreadPool();
		MessageService messageService = new MessageService();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						messageService.sendMessage(failedQueue.take());
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void sendMessage(String message) {
		persistenceMessage(message);
		if (!MessageQueue.receiveMessage(this, message)) {
			try {
				failedQueue.put(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Message send:" + message);
		}
	}

	public void confirm(String message) {
		messageList.remove(message);
	}

	private void persistenceMessage(String message) {
		messageList.add(message);
		System.out.println("Message persistenced");
	}

}
