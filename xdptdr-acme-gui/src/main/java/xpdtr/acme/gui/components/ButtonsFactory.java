package xpdtr.acme.gui.components;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsFactory {

	Map<AcmeGuiActions, Runnable> clicked = new HashMap<>();
	Map<AcmeGuiActions, JButton> buttons = new HashMap<>();

	public void setClicked(AcmeGuiActions action, Runnable handler) {
		clicked.put(action, handler);
	}

	private void disableAll() {
		buttons.forEach((a, b) -> {
			b.setEnabled(false);
		});
	}

	private void clicked(JButton button, Runnable consumer) {
		if (consumer != null) {
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					disableAll();
					consumer.run();
				}
			});
		}
	}

	public Buttons render(Container contentPane) {

		JPanel rows = new JPanel();
		rows.setLayout(new BoxLayout(rows, BoxLayout.PAGE_AXIS));

		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEADING));

		for (AcmeGuiActions action : AcmeGuiActions.values()) {
			JButton button = new JButton(action.getLabel());
			clicked(button, clicked.get(action));
			buttons.put(action, button);
			if (action.getRow() == 1) {
				row1.add(button);
			} else {
				row2.add(button);
			}
		}

		rows.add(row1);
		rows.add(row2);

		contentPane.add(rows, BorderLayout.SOUTH);

		return new Buttons(buttons);
	}

}
