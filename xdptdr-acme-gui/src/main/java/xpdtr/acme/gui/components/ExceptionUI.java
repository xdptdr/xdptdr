package xpdtr.acme.gui.components;

import java.awt.Component;

public class ExceptionUI {

	public static Component render(Exception ex) {
		return MessageUI.render(ex.getClass().getName() + " : " + ex.getMessage());
	}

}
