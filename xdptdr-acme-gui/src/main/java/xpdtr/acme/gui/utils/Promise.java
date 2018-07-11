package xpdtr.acme.gui.utils;

import java.util.function.Consumer;

public class Promise<T> {

	private Consumer<T> handler;
	private Thread thread;

	public Promise(Consumer<Promise<T>> consumer) {
		this.thread = new Thread(() -> {
			consumer.accept(this);
		});
	}

	public void then(Consumer<T> handler) {
		this.handler = handler;
		thread.start();
	}

	public void done(T response) {
		handler.accept(response);
	}

}
