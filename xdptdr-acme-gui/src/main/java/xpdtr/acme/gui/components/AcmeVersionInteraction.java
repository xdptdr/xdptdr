package xpdtr.acme.gui.components;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import xpdtr.acme.gui.interactions.Interacter;
import xpdtr.acme.gui.interactions.UIInteraction;
import xpdtr.acme.gui.utils.U;

public class AcmeVersionInteraction extends UIInteraction {

	private UILogger logger;
	private Consumer<String> consumer;

	public AcmeVersionInteraction(Interacter interacter, Container container, UILogger logger, Consumer<String> consumer) {
		super(interacter, container);
		this.logger = logger;
		this.consumer = consumer;
	}

	@Override
	protected void perform() {

		logger.important("Which version of ACME do you want to use ?");

		JComboBox<String> list = new JComboBox<String>(new String[] { "Version 1", "Version 2" });
		list.setSelectedItem("Version 2");

		JButton button = new JButton("OK");
		U.clicked(button, (e) -> {
			interacter.perform(() -> {
				list.setEnabled(false);
				button.setEnabled(false);
				consumer.accept((String) list.getSelectedItem());

			});
		});

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		panel.add(list);
		panel.add(button);
		container.add(panel);

	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, Consumer<String> consumer) {
		new AcmeVersionInteraction(interacter, container, logger, consumer).start();
	}

}
