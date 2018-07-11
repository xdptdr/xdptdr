package xpdtr.acme.gui.components;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import xpdtr.acme.gui.components.AcmeGuiActions;

public class Buttons {

	private Map<AcmeGuiActions, JButton> buttons = new HashMap<>();
	private Map<AcmeGuiActions, Boolean> enabled = new HashMap<>();

	public Buttons(Map<AcmeGuiActions, JButton> buttons) {
		this.buttons = buttons;
	}

	public void setEnabled(AcmeGuiActions action, boolean value) {
		enabled.put(action, value);
	}

	public void update() {
		for (AcmeGuiActions action : AcmeGuiActions.values()) {
			if (buttons.containsKey(action)) {
				JButton button = buttons.get(action);
				boolean e = Boolean.TRUE.equals(enabled.get(action)) && button.getActionListeners().length > 0;
				button.setEnabled(e);
			}
		}
	}

}
