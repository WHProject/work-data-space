package distributed.transaction.reliablemessage;

public class ParticipatorA extends Participator {

	private MessageService messageService = new MessageService();

	@Override
	public void execute() {
		System.out.println("ParticipatorA execute finished,send message.");
		try {
			messageService.sendMessage("ParticipatorA");
			System.out.println("ParticipatorA committed.");
		} catch (Exception e) {
			System.out.println("ParticipatorA rollback.");
		}
	}

}
