package xpdtr.acme.gui.interactions;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import xdptdr.acme.crypto.ECCurves;
import xdptdr.acme.crypto.XKeyPairGenerator;
import xdptdr.acme.jw.KeyPairWithJWK;
import xpdtr.acme.gui.components.MessageUI;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.U;

public class CreateKeyPairInteraction extends UIInteraction {

	private UILogger logger;
	private Consumer<KeyPairWithJWK> consumer;

	public CreateKeyPairInteraction(Interacter interacter, Container container, UILogger logger,
			Consumer<KeyPairWithJWK> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.consumer = consumer;
	}

	@Override
	protected void perform() {

		logger.beginGroup("Key Pair Creation");

		JButton ellipticButton = U.button("Elliptic");
		JButton rsaButton = U.button("RSA");
		JButton cancelButton = U.button("Cancel");

		Runnable disabler = U.disabler(ellipticButton, rsaButton, cancelButton);

		U.clicked(ellipticButton, interacter, () -> {
			disabler.run();
			U.setFont(ellipticButton, Font.BOLD);
			ellipticNext();
		});

		U.clicked(rsaButton, interacter, () -> {
			disabler.run();
			U.setFont(rsaButton, Font.BOLD);
			rsaNext();
		});

		U.clicked(cancelButton, interacter, () -> {
			disabler.run();
			U.setFont(cancelButton, Font.BOLD);
			cancel();
		});

		logger.leading(ellipticButton, rsaButton, cancelButton);

	}

	private void ellipticNext() {

		List<JButton> buttons = new ArrayList<>();

		Runnable disabler = () -> {
			for (JButton btn : buttons) {
				btn.setEnabled(false);
			}
		};

		for (String javaName : ECCurves.javaNames()) {
			JButton button = U.button(ECCurves.displayName(javaName));
			buttons.add(button);
			U.clicked(button, interacter, () -> {
				disabler.run();
				U.setFont(button, Font.BOLD);
				ellipticCreate(javaName);
			});
		}

		JButton cancelButton = U.button("Cancel");
		U.clicked(cancelButton, interacter, () -> {
			disabler.run();
			U.setFont(cancelButton, Font.BOLD);
			cancel();
		});
		buttons.add(cancelButton);

		logger.leading(buttons);

	}

	private void rsaNext() {

		List<Component> components = new ArrayList<>();

		Runnable disabler = () -> {
			for (Component component : components) {
				component.setEnabled(false);
			}
		};

		components.add(MessageUI.render(" Key size : "));

		JButton best = U.button("Best");
		components.add(best);
		U.clicked(best, interacter, () -> {
			disabler.run();
			U.setFont(best, Font.BOLD);
			rsaCreate(2048);
		});

		for (int keySize : new int[] { 1024, 2048, 4096, 8192 }) {
			JButton button = U.button(Integer.toString(keySize));
			components.add(button);
			U.clicked(button, interacter, () -> {
				disabler.run();
				U.setFont(button, Font.BOLD);
				rsaCreate(keySize);
			});
		}

		JButton cancelButton = U.button("Cancel");
		U.clicked(cancelButton, interacter, () -> {
			disabler.run();
			U.setFont(cancelButton, Font.BOLD);
			cancel();
		});
		components.add(cancelButton);

		logger.leading(components);

	}

	private void ellipticCreate(String curve) {

		logger.message("Creating elliptic key pair with curve " + curve);

		U.work(

				interacter,

				() -> KeyPairWithJWK.fromEllipticKeyPair(XKeyPairGenerator.newEllipticKeyPair(curve), curve),

				worker -> {
					KeyPairWithJWK result = null;
					try {
						result = worker.get();
						logger.message("New key pair created");
					} catch (Exception e) {
						logger.exception(e);
					}
					logger.endGroup();
					consumer.accept(result);
				}

		);

	}

	private void rsaCreate(int keysize) {

		logger.message("Creating RSA key pair with key size " + keysize);

		U.work(

				interacter,

				() -> KeyPairWithJWK.fromRSAKeyPair(XKeyPairGenerator.newRSAKeyPair(keysize), keysize),

				worker -> {
					KeyPairWithJWK keyPairWithJWK = null;
					try {
						keyPairWithJWK = worker.get();
						logger.message("New key pair created");
					} catch (Exception e) {
						logger.exception(e);
					}
					logger.endGroup();
					consumer.accept(keyPairWithJWK);
				}

		);

	}

	private void cancel() {
		logger.message("Cancelled");
		logger.endGroup();
		consumer.accept(null);
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger,
			Consumer<KeyPairWithJWK> consumer) {
		new CreateKeyPairInteraction(interacter, container, logger, consumer).start();

	}

}
