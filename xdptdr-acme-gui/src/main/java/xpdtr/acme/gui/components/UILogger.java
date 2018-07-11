package xpdtr.acme.gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import xpdtr.acme.gui.utils.U;

public class UILogger {

	private Container container;
	private Container destination;

	public UILogger(Container container) {
		this.container = container;
		this.destination = container;
	}

	public Container beginGroup(String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		TitledBorder bf = BorderFactory.createTitledBorder(title);
		bf.setTitleFont(bf.getTitleFont().deriveFont(Font.BOLD));
		panel.setBorder(bf);
		destination = panel;
		container.add(destination);
		return destination;
	}

	public void important(String message) {
		JTextField label = MessageUI.render(message);
		U.setFont(label, Font.BOLD);
		destination.add(label);
	}

	public void message(String message) {
		destination.add(MessageUI.render(message));
	}

	public void message(String message, boolean multiline) {
		destination.add(MessageUI.render(message, multiline));
	}

	public void exception(Exception exception) {
		destination.add(ExceptionUI.render(exception));
	}

	public void endGroup() {
		destination = container;
	}

	public Container getDestination() {
		return destination;
	}

	public void leading(Component... components) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		for (Component component : components) {
			panel.add(component);
		}
		destination.add(panel);
	}

	public void leading(Collection<? extends Component> components) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		for (Component component : components) {
			panel.add(component);
		}
		destination.add(panel);
	}

}
