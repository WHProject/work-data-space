package distributed.transaction.twophase;

public abstract class Participator {
	private boolean prepareState = false;

	public boolean isPrepareState() {
		return prepareState;
	}

	public void setPrepareState(boolean prepareState) {
		this.prepareState = prepareState;
	}

	abstract void execute(TransactionManager transactionManager);
}
