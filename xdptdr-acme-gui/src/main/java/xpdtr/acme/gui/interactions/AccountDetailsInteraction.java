package xpdtr.acme.gui.interactions;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.Promise;

public class AccountDetailsInteraction extends UIInteraction {

	private UILogger logger;
	private AcmeSession session;
	private Runnable after;

	public AccountDetailsInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.after = after;
	}

	@Override
	protected void perform() {
		logger.beginGroup("Account details");
		logger.message("Getting account details...");
		send(session).then(this::handleResponse);
	}

	private Promise<AcmeResponse<Void>> send(AcmeSession session) {
		return new Promise<>(promise -> {
			promise.done(Acme2.accountDetails(session));
		});
	}

	private void handleResponse(AcmeResponse<Void> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
			} else {
				logger.message(response.getResponseText(), true);
			}
			logger.endGroup();
			after.run();
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		new AccountDetailsInteraction(interacter, container, logger, session, after).start();
	}

}
