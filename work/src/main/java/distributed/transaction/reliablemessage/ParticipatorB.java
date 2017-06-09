package distributed.transaction.reliablemessage;

public class ParticipatorB extends Participator {

	@Override
	public void execute() {
		try {
			if (MessageQueue.messageQueue.take() != null) {
				System.out.println("ParticipatorB committed.");
			}
		} catch (Exception e) {
			System.out.println("ParticipatorB rollback.");
		}
	}

}
