package xpdtr.acme.gui.interactions;

import java.awt.Container;
import java.util.function.Consumer;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xdptdr.acme.v2.account.AcmeAccount;
import xpdtr.acme.gui.components.AccountCreationUI;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.Promise;

public class NewAccountInteraction extends UIInteraction {

	private AcmeSession session;
	private UILogger logger;
	private Consumer<AcmeResponse<AcmeAccount>> consumer;
	private Container destination;

	public NewAccountInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<AcmeResponse<AcmeAccount>> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;
	}

	public void perform() {
		logger.beginGroup("New Account");
		destination = logger.getDestination();
		new AccountCreationUI(this::proceed, this::cancel).renderInput(destination);
	}

	private void proceed(String contact) {
		interacter.perform(() -> {
			logger.message("Calling account create");
			send(session, contact).then(this::handleResponse);
		});
	}

	private Promise<AcmeResponse<AcmeAccount>> send(AcmeSession session, String contact) {
		return new Promise<>(promise -> {
			promise.done(Acme2.newAccount(session, contact));
		});
	}

	private void cancel() {
		interacter.perform(() -> {
			logger.message("Account creation cancelled");
			logger.endGroup();
			consumer.accept(null);
		});
	}

	private void handleResponse(AcmeResponse<AcmeAccount> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
				consumer.accept(null);
			} else {
				logger.message(response.getResponseText(), true);
				logger.endGroup();
				consumer.accept(response);
			}
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<AcmeResponse<AcmeAccount>> consumer) {
		new NewAccountInteraction(interacter, container, logger, session, consumer).start();
	}

}
