package xpdtr.acme.gui.interactions;

import java.util.function.Consumer;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.interactions.Interacter;
import xpdtr.acme.gui.interactions.UIInteraction;
import xpdtr.acme.gui.utils.Promise;

public class AccountDeactivationInteraction extends UIInteraction {

	private UILogger logger;
	private AcmeSession session;
	private Consumer<Boolean> consumer;

	public AccountDeactivationInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<Boolean> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;

	}

	@Override
	protected void perform() {
		logger.beginGroup("Account deactivation");
		logger.message("Deactivating account...");
		send(session).then(this::handleResponse);
	}

	private Promise<AcmeResponse<Boolean>> send(AcmeSession session) {
		return new Promise<>(promise -> {
			promise.done(Acme2.deactivateAccount(session));
		});
	}

	public void handleResponse(AcmeResponse<Boolean> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
				logger.endGroup();
				consumer.accept(false);
			} else {
				logger.message("Success");
				logger.endGroup();
				consumer.accept(response.getContent());
			}
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<Boolean> consumer) {
		new AccountDeactivationInteraction(interacter, container, logger, session, consumer).start();

	}

}
