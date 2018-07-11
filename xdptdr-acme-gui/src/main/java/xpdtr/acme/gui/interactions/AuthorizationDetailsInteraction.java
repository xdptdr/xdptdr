package xpdtr.acme.gui.interactions;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeAuthorization;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.layout.SameWidthLayout;
import xpdtr.acme.gui.utils.Promise;
import xpdtr.acme.gui.utils.U;

public class AuthorizationDetailsInteraction extends UIInteraction {

	private UILogger logger;
	private AcmeSession session;
	private Container destination;
	private List<Component> buttons = new ArrayList<>();
	private Consumer<AcmeAuthorization> consumer;

	public AuthorizationDetailsInteraction(Interacter interacter, JPanel container, UILogger logger,
			AcmeSession session, Consumer<AcmeAuthorization> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;
	}

	@Override
	protected void perform() {
		logger.beginGroup("Authorization details");
		destination = logger.getDestination();

		JPanel authPanel = new JPanel();
		authPanel.setLayout(new SameWidthLayout(5));

		for (String auth : session.getOrder().getAuthorizations()) {
			JButton button = new JButton(auth);
			buttons.add(button);
			authPanel.add(button);
			U.clicked(button, (e) -> {
				select(auth);
			});
		}
		destination.add(authPanel);

		JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton cancel = new JButton("Cancel");
		U.clicked(cancel, this::cancel);
		buttons.add(cancel);
		cancelPanel.add(cancel);
		destination.add(cancelPanel);

	}

	private void select(String authorizationUrl) {
		interacter.perform(() -> {
			disable();
			getAuthorizationDetails(authorizationUrl);
		});
	}

	private void cancel(ActionEvent e) {
		interacter.perform(() -> {
			disable();
			logger.message("Cancelled");
			logger.endGroup();
			consumer.accept(null);
		});
	}

	private void disable() {
		for (Component c : buttons) {
			c.setEnabled(false);
		}
	}

	private void getAuthorizationDetails(String url) {
		send(url).then(this::handleResponse);
	}

	private Promise<AcmeResponse<AcmeAuthorization>> send(String url) {
		return new Promise<>(promise -> {
			promise.done(Acme2.getAuthorization(session, url));
		});
	}

	private void handleResponse(AcmeResponse<AcmeAuthorization> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
				logger.endGroup();
				consumer.accept(null);
			} else {
				logger.message(response.getResponseText(), true);
				logger.endGroup();
				consumer.accept(response.getContent());
			}
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<AcmeAuthorization> consumer) {
		new AuthorizationDetailsInteraction(interacter, container, logger, session, consumer).start();

	}

}
