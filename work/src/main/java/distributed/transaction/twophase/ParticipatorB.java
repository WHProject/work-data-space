package distributed.transaction.twophase;

public class ParticipatorB extends Participator {

	@Override
	public void execute(TransactionManager transactionManager) {
		System.out.println("ParticipatorB execute finished,watting commit.");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setPrepareState(true);
		if (transactionManager.prepare(this)) {
			try {
				if (!transactionManager.commit()) {
					throw new RuntimeException("Committed failed.");
				}
				System.out.println("ParticipatorB committed.");
			} catch (Exception e) {
				System.out.println("ParticipatorB rollback.");
			}
		}
	}

}
