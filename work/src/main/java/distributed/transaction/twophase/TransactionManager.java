package distributed.transaction.twophase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionManager {
	private BlockingQueue<String> allPreparedQueue = new ArrayBlockingQueue<String>(1);

	private List<Participator> participators = new ArrayList<Participator>();

	private List<Participator> preparedParticipators = new ArrayList<Participator>();

	private static final String SUCCESS_FLAG = "ok";

	private static final String FAILED_FLAG = "failed";

	public boolean registerParticipator(Participator participator) {
		return participators.add(participator);
	}

	public void executeTransaction() {
		TransactionManager transactionManager = this;
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (Participator participator : participators) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					participator.execute(transactionManager);
				}
			});
		}
	}

	public boolean prepare(Participator participator) {
		boolean flag = preparedParticipators.add(participator);
		if (participators.size() > 0 && participators.size() == preparedParticipators.size()) {
			boolean isAllPrepared = true;
			for (Participator p : preparedParticipators) {
				if (!p.isPrepareState()) {
					isAllPrepared = false;
					break;
				}
			}
			allPreparedQueue.add(isAllPrepared ? SUCCESS_FLAG : FAILED_FLAG);
		}
		return flag;
	}

	public boolean commit() {
		try {
			String result = allPreparedQueue.take();
			allPreparedQueue.put(result);
			return SUCCESS_FLAG.equals(result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}
