package xpdtr.acme.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import xpdtr.acme.gui.misc.ExceptionType;
import xpdtr.acme.misc.ExceptionHandler;

public class AcmeGuiMain {

	public static void show(ExceptionHandler eh) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			eh.handle(e, ExceptionType.NOT_FATAL);
		}

		try {

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {

					JFrame.setDefaultLookAndFeelDecorated(true);
					AcmeGui gui = new AcmeGui();
					gui.init();
					gui.setVisible(true);
				}
			});
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			eh.handle(e, ExceptionType.FATAL);
		}

	}

	public static void main(String[] args) {
		show(new ExceptionHandler() {
			@Override
			public void handle(Exception e, ExceptionType et) {
				System.out.println(et.name() + " : " + e.getClass().getName() + " : " + e.getMessage());

			}
		});
	}

}
