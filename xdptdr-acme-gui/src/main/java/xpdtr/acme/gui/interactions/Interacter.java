package xpdtr.acme.gui.interactions;

import javax.swing.JFrame;

public class Interacter {

	private JFrame frame;
	private Runnable autoscroll;

	public Interacter(JFrame frame, Runnable autoscroll) {
		this.frame = frame;
		this.autoscroll = autoscroll;
	}

	public void perform(Runnable runnable) {
		runnable.run();
		frame.validate();
		if (autoscroll != null) {
			autoscroll.run();
		}
	}

}
