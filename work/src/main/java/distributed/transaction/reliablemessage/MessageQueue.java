package distributed.transaction.reliablemessage;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageQueue {
	public static BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(100);

	public static boolean receiveMessage(MessageService messageService, String message) {
		try {
			int i = new Random().nextInt(100) + 1;
			if (i % 2 == 0) {
				throw new RuntimeException("ReceiveMessage failed.");
			}

			messageQueue.put(message);
			messageService.confirm(message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
}
