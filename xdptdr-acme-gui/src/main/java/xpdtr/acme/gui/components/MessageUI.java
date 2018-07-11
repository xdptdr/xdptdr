package xpdtr.acme.gui.components;

import java.awt.Component;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MessageUI {

	public static JTextField render(String message) {
		JTextField component = new JTextField(message);
		component.setEditable(false);
		component.setBackground(null);
		component.setBorder(null);
		return component;
	}

	public static Component render(String message, boolean multiline) {
		if (multiline == false) {
			return render(message);
		} else {
			JTextArea component = new JTextArea(message);
			component.setEditable(false);
			component.setCursor(null);
			component.setOpaque(false);
			component.setFocusable(false);
			component.setFont(UIManager.getFont("Label.font"));
			component.setWrapStyleWord(true);
			component.setLineWrap(true);
			return component;
		}

	}

}
