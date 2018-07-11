package xpdtr.acme.gui.components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import xpdtr.acme.gui.layout.FullWidthLayout;
import xpdtr.acme.gui.utils.U;

public class Title {

	public static Component create() {

		JTextField component = MessageUI.render("xdptdr's Acme GUI");
		U.setFont(component, Font.BOLD, 1.3);
		U.setMargins(component, 0, 20);
		component.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panel = new JPanel(new FullWidthLayout());
		panel.add(component);
		return panel;
	}

}
