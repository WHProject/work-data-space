package distributed.transaction.twophase;

public class ParticipatorA extends Participator {

	@Override
	public void execute(TransactionManager transactionManager) {
		System.out.println("ParticipatorA execute finished,watting commit.");
		this.setPrepareState(true);
		if (transactionManager.prepare(this)) {
			try {
				if (!transactionManager.commit()) {
					throw new RuntimeException("Committed failed.");
				}
				System.out.println("ParticipatorA committed.");
			} catch (Exception e) {
				System.out.println("ParticipatorA rollback.");
			}
		}
	}

}
