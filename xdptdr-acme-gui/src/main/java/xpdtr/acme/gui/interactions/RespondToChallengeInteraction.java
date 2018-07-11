package xpdtr.acme.gui.interactions;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.interactions.Interacter;
import xpdtr.acme.gui.interactions.UIInteraction;
import xpdtr.acme.gui.utils.Promise;

public class RespondToChallengeInteraction extends UIInteraction {

	private UILogger logger;
	private AcmeSession session;
	private Runnable after;

	public RespondToChallengeInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.after = after;
	}

	@Override
	protected void perform() {
		logger.beginGroup("Respond to challenge");
		logger.message("Responding to challenge...");
		send(session).then(this::handleResponse);
	}

	private Promise<AcmeResponse<Void>> send(AcmeSession session) {
		return new Promise<AcmeResponse<Void>>(promise -> {
			promise.done(Acme2.respondToChallenge(session));
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
		new RespondToChallengeInteraction(interacter, container, logger, session, after).start();
	}

}
