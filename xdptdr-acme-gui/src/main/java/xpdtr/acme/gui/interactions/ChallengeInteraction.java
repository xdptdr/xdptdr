package xpdtr.acme.gui.interactions;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import xdptdr.acme.jw.JWBase64;
import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeChallenge;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.layout.SameWidthLayout;
import xpdtr.acme.gui.utils.Promise;
import xpdtr.acme.gui.utils.U;

public class ChallengeInteraction extends UIInteraction {

	private enum ChallengeType {
		HTTP, DNS, TLS
	};

	private AcmeSession session;
	private Consumer<AcmeChallenge> consumer;
	private UILogger logger;
	private Container destination;
	private List<Component> buttons = new ArrayList<>();

	public ChallengeInteraction(Interacter interacter, Container container, UILogger logger, AcmeSession session,
			Consumer<AcmeChallenge> finished) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = finished;
	}

	@Override
	public void perform() {

		destination = logger.beginGroup("Challenge");

		logger.message("Which challenge ?");

		List<String> urls = new ArrayList<>();

		Map<String, String> titles = new HashMap<>();

		for (AcmeChallenge challenge : session.getAuthorization().getChallenges()) {

			String url = challenge.getUrl();
			ChallengeType prefix = getChallengeType(challenge);
			if (prefix != null) {
				titles.put(url, "[" + prefix + "] " + url);
			} else {
				titles.put(url, url);
			}

			urls.add(url);

		}
		Collections.sort(urls);

		JPanel choices = new JPanel(new SameWidthLayout(5));
		for (String url : urls) {
			JButton button = new JButton(titles.get(url));
			U.clicked(button, (e) -> {
				select(url);
			});
			choices.add(button);
			buttons.add(button);
		}

		JButton cancelButton = new JButton("Cancel");
		buttons.add(cancelButton);

		U.clicked(cancelButton, this::cancel);

		destination.add(choices);

		logger.leading(cancelButton);
	}

	private ChallengeType getChallengeType(AcmeChallenge challenge) {
		switch (challenge.getType()) {
		case "http-01":
			return ChallengeType.HTTP;
		case "dns-01":
			return ChallengeType.DNS;
		case "tls-alpn-01":
			return ChallengeType.TLS;
		default:
			return null;
		}
	}

	private void select(String url) {
		disable();
		next(url);
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
		for (Component button : buttons) {
			button.setEnabled(false);
		}
	}

	private void next(String url) {
		logger.message("Sending " + url + "...");
		send(url).then(this::handleResponse);
	}

	private Promise<AcmeResponse<AcmeChallenge>> send(String url) {
		return new Promise<>(promise -> {
			promise.done(Acme2.challenge(session, url));
		});
	}

	private void handleResponse(AcmeResponse<AcmeChallenge> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
				logger.endGroup();
				consumer.accept(null);
			} else {
				logger.message(response.getResponseText(), true);
				AcmeChallenge challenge = response.getContent();
				if (getChallengeType(challenge) == ChallengeType.HTTP) {
					String url = session.getAuthorization().getIdentifier().getValue();
					logger.message(
							"To respond to this challenge, make the following URL respond with the following content:");
					logger.message("URL : http://" + url + "/.well-known/acme-challenge/" + challenge.getToken());
					try {
						logger.message("Token : " + challenge.getToken() + "." + otherPart());
					} catch (Exception exception) {
						logger.exception(exception);
					}
				}
				pollOrLeave(challenge);
				logger.endGroup();
				consumer.accept(challenge);
			}
		});
	}

	private String otherPart() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		Map<String, String> jwk = session.getKeyPairWithJWK().getPublicJwk();
		String crv = jwk.get("crv");
		String kty = jwk.get("kty");
		String x = jwk.get("x");
		String y = jwk.get("y");
// 
		String s = "{\"crv\":\"" + crv + "\",\"kty\":\"" + kty + "\",\"x\":\"" + x + "\",\"y\":\"" + y + "\"}";
		return JWBase64.encode(md.digest(s.getBytes()));
	}

	private void pollOrLeave(AcmeChallenge challenge) {
		logger.leading(U.button("Poll", () -> {
			poll(challenge);
		}));
		logger.leading(U.button("Leave", () -> {
			leave(challenge);
		}));
	}

	private void poll(AcmeChallenge challenge) {
		interacter.perform(() -> {

			logger.message("Polling...");

			youpi(challenge).then(response -> {
				yapla(response, challenge);
			});

		});
	}

	private Promise<AcmeResponse<Void>> youpi(AcmeChallenge challenge) {
		return new Promise<AcmeResponse<Void>>(promise -> {
			promise.done(Acme2.respondToChallenge(session, challenge));
		});
	}

	private void yapla(AcmeResponse<Void> response, AcmeChallenge challenge) {
		interacter.perform(() -> {
			logger.message("Done");
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
			} else {
				logger.message(response.getResponseText());
			}
			pollOrLeave(challenge);
		});
	}

	public void leave(AcmeChallenge challenge) {
		interacter.perform(() -> {
			logger.message("Leaving");
			;
			logger.endGroup();
			consumer.accept(challenge);
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<AcmeChallenge> consumer) {
		new ChallengeInteraction(interacter, container, logger, session, consumer).start();

	}
}
