package xpdtr.acme.gui.interactions;

import java.awt.Container;
import java.util.function.Consumer;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeOrder;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.OrderCreationUI;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.Promise;

public class NewOrderInteraction extends UIInteraction {

	private UILogger logger;
	private AcmeSession session;
	private Container destination;
	private Consumer<AcmeOrder> consumer;

	public NewOrderInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<AcmeOrder> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;
	}

	@Override
	protected void perform() {
		logger.beginGroup("New Order");
		destination = logger.getDestination();
		logger.message("Creating new order");
		new OrderCreationUI(this::proceed, this::cancel).renderInput(destination);

	}

	private void proceed(String site) {
		send(site).then(this::handleResponse);
	}

	private Promise<AcmeResponse<AcmeOrder>> send(String site) {
		return new Promise<>(promise -> {
			promise.done(Acme2.newOrder(session, site));
		});

	}

	private void cancel() {
		logger.message("Cancelled");
		consumer.accept(null);
	}

	private void handleResponse(AcmeResponse<AcmeOrder> response) {
		interacter.perform(() -> {
			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
				consumer.accept(null);
			} else {
				logger.message(response.getResponseText(), true);
				consumer.accept(response.getContent());
			}
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<AcmeOrder> consumer) {
		new NewOrderInteraction(interacter, container, logger, session, consumer).start();

	}

}
