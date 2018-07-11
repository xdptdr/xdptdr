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

public class AccountCreationUI {

	private static final String KNOWN_CONTACTS = "knownContacts";
	
	private JComboBox<String> contactInput;
	private JButton createButton;
	private JButton cancelButton;
	private Consumer<String> onCreate;
	private Runnable onCancel;
	private List<String> knownContacts;

	public AccountCreationUI(Consumer<String> onCreate, Runnable onCancel) {
		this.onCreate = onCreate;
		this.onCancel = onCancel;
	}

	public void renderInput(Container container) {
		addFields(container);
		addButtons(container);
	}

	private void addFields(Container container) {
		JPanel fieldsPanel = new JPanel(new LabelsAndFields(5, 15));
		fieldsPanel.add(new JLabel("Contact"));
		addContactInput(fieldsPanel);
		container.add(fieldsPanel);
	}

	private void addContactInput(JPanel fieldsPanel) {
		contactInput = new JComboBox<>();
		contactInput.setEditable(true);
		knownContacts = LinesPersister.getLines(KNOWN_CONTACTS);
		addKnownContacts(knownContacts, contactInput);
		fieldsPanel.add(contactInput);

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
		contactInput.setEnabled(false);
		createButton.setEnabled(false);
		cancelButton.setEnabled(false);
	};

	private void create(ActionEvent e) {
		disable();
		String selected = contactInput.getSelectedItem().toString();
		LinesPersister.saveLines(KNOWN_CONTACTS, knownContacts, selected);
		onCreate.accept(selected);
	}

	private void cancel(ActionEvent e) {
		disable();
		onCancel.run();
	}



	private void addKnownContacts(List<String> knownContacts, JComboBox<String> contactInput) {
		for (String contact : knownContacts) {
			contactInput.addItem(contact);
		}
	}
}
