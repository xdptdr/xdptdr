package xpdtr.acme.gui.components;

import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import xdptdr.acme.AcmeUrls;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.interactions.Interacter;
import xpdtr.acme.gui.interactions.UIInteraction;
import xpdtr.acme.gui.utils.U;

public class AcmeUrlInteraction extends UIInteraction {

	private UILogger logger;
	private Consumer<String> consumer;
	private AcmeSession session;

	public AcmeUrlInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Consumer<String> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.consumer = consumer;
	}

	@Override
	protected void perform() {
		switch (session.getVersion()) {
		case "Version 1":
			logger.important("Sorry, support for Acme v1 is not available");
			break;
		case "Version 2":
		default:
			
			logger.important("Which URL do you want to use ?");

			JComboBox<String> list = new JComboBox<String>(
					new String[] { AcmeUrls.LETS_ENCRYPT_V2, AcmeUrls.LETS_ENCRYPT_V2_STAGING });
			list.setSelectedItem(AcmeUrls.LETS_ENCRYPT_V2_STAGING);

			JButton button = new JButton("OK");
			U.clicked(button, (e) -> {
				list.setEnabled(false);
				button.setEnabled(false);
				consumer.accept((String) list.getSelectedItem());
			});

			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEADING));
			panel.add(list);
			panel.add(button);
			container.add(panel);
		}

	}

	public static void perform(Interacter interacter, JPanel sessionContainer, UILogger logger, AcmeSession session,
			Consumer<String> consumer) {
		new AcmeUrlInteraction(interacter, sessionContainer, logger, session, consumer).start();

	}

}
