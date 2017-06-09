package distributed.transaction.twophase;

public class Test {
	public static void main(String[] args) throws Exception {
		TransactionManager transactionManager=new TransactionManager();
		transactionManager.registerParticipator(new ParticipatorA());
		transactionManager.registerParticipator(new ParticipatorB());
		transactionManager.executeTransaction();
	}
}
