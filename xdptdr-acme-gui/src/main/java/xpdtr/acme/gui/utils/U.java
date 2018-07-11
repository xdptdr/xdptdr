package xpdtr.acme.gui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingWorker;

import xpdtr.acme.gui.interactions.Interacter;

public class U {

	private U() {
	}

	public static void setFont(Component component, int style) {
		component.setFont(component.getFont().deriveFont(style));
	}

	public static void setFont(Component component, int style, double multiplier) {
		Font font = component.getFont();
		font = font.deriveFont(style);
		font = font.deriveFont((float) (font.getSize2D() * multiplier));
		component.setFont(font);
	}

	public static void setMargins(JComponent component, int h, int v) {
		component.setBorder(BorderFactory.createEmptyBorder(v, h, v, h));
	}

	public static void addM(Container container, List<Component> components) {
		for (Component component : components) {
			container.add(component);
		}
	}

	public static void addM(Container container, Component component) {
		container.add(component);
	}

	public static void clicked(JButton button, Consumer<ActionEvent> consumer) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				consumer.accept(event);
			}
		});
	}

	public static void clicked(JButton button, Interacter interacter, Runnable runnable) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				interacter.perform(runnable);
			}
		});
	}

	public static Runnable disabler(Component... components) {
		return () -> {
			for (Component component : components) {
				component.setEnabled(false);
			}
		};
	}

	public static JButton button(String text, Interacter interacter, Runnable action) {
		JButton button = new JButton(text);
		clicked(button, interacter, action);
		return button;
	}

	public static JButton button(String text, Runnable action) {
		JButton button = new JButton(text);
		clicked(button, e -> action.run());
		return button;
	}

	public static JButton button(String text) {
		JButton button = new JButton(text);
		return button;
	}

	public static <T> void work(Interacter interacter, Callable<T> work, Consumer<SwingWorker<T, Void>> done) {
		new SwingWorker<T, Void>() {
			@Override
			protected T doInBackground() throws Exception {
				return work.call();
			}
			protected void done() {
				interacter.perform(() -> {
					done.accept(this);
				});
			};
		}.execute();
	}
}
