package xpdtr.acme.gui.components;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xpdtr.acme.gui.utils.U;

public class OrderCreationUI {

	private static final String KNOWN_SITES = "knownSites";

	private JComboBox<String> siteInput;
	private JButton createButton;
	private JButton cancelButton;
	private Consumer<String> onCreate;
	private Runnable onCancel;

	private List<String> knownSites;

	public OrderCreationUI(Consumer<String> onCreate, Runnable onCancel) {
		this.onCreate = onCreate;
		this.onCancel = onCancel;
	}

	public void renderInput(Container container) {
		addFields(container);
		addButtons(container);
	}

	private void addFields(Container container) {
		JPanel fieldsPanel = new JPanel(new LabelsAndFields(5, 15));
		fieldsPanel.add(new JLabel("Site"));
		addContactInput(fieldsPanel);
		container.add(fieldsPanel);
	}

	private void addContactInput(JPanel fieldsPanel) {
		siteInput = new JComboBox<>();
		siteInput.setEditable(true);
		knownSites = LinesPersister.getLines(KNOWN_SITES);
		addKnownSites(knownSites, siteInput);
		fieldsPanel.add(siteInput);

	}

	private void addButtons(Container container) {
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEADING));

		createButton = new JButton("Create");
		U.clicked(createButton, this::create);

		cancelButton = new JButton("Cancel");
		U.clicked(cancelButton, this::cancel);

		buttons.add(createButton);
		buttons.add(cancelButton);
		container.add(buttons);
	}

	private void disable() {
		siteInput.setEnabled(false);
		createButton.setEnabled(false);
		cancelButton.setEnabled(false);
	};

	private void create(ActionEvent e) {
		disable();
		String selected = siteInput.getSelectedItem().toString();
		LinesPersister.saveLines(KNOWN_SITES, knownSites, selected);
		onCreate.accept(selected);
	}

	private void cancel(ActionEvent e) {
		disable();
		onCancel.run();
	}

	private void addKnownSites(List<String> knownSites, JComboBox<String> siteInput) {
		for (String contact : knownSites) {
			siteInput.addItem(contact);
		}
	}

}
