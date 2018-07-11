package xpdtr.acme.gui.interactions;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeDirectoryInfos2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.Promise;

public class NonceInteraction extends UIInteraction {

	private AcmeSession session;
	private UILogger logger;
	private Runnable consumer;

	public NonceInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;
	}

	public void perform() {
		logger.beginGroup("New nonce");
		logger.message("Getting new nonce... ");
		send(session.getInfos(), session).then(this::handleResponse);
	}

	private Promise<AcmeResponse<String>> send(AcmeDirectoryInfos2 infos, AcmeSession session) {
		return new Promise<>(promise -> {
			promise.done(Acme2.nonce(session));
		});
	}

	private void handleResponse(AcmeResponse<String> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
			} else {
				logger.message(response.getContent());
			}
			logger.endGroup();
			consumer.run();
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable consumer) {
		new NonceInteraction(interacter, container, logger, session, consumer).start();
	}

}
