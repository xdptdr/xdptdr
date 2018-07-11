package xpdtr.acme.gui.interactions;

import java.awt.Container;
import java.util.function.Consumer;

import javax.swing.JPanel;

import com.fasterxml.jackson.databind.ObjectMapper;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeDirectoryInfos2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.Promise;

public class DirectoryInteraction extends UIInteraction {

	private AcmeSession session;
	private UILogger logger;
	private Consumer<AcmeDirectoryInfos2> consumer;

	public DirectoryInteraction(Interacter interacter, Container container, UILogger logger, AcmeSession session,
			Consumer<AcmeDirectoryInfos2> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;
	}

	@Override
	public void perform() {
		logger.beginGroup("Getting Directory");
		logger.message("Getting directory infos... ");
		send(session.getUrl(), session.getOm(), session).then(this::handleResponse);
	}

	private Promise<AcmeResponse<AcmeDirectoryInfos2>> send(String url, ObjectMapper om, AcmeSession session) {
		return new Promise<>(promise -> {
			promise.done(Acme2.directory(url, om, session));
		});
	}

	private void handleResponse(AcmeResponse<AcmeDirectoryInfos2> response) {
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
			Consumer<AcmeDirectoryInfos2> consumer) {
		new DirectoryInteraction(interacter, container, logger, session, consumer).start();
	}

}
