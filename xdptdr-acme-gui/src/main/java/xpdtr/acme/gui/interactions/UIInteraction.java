package xpdtr.acme.gui.interactions;

import java.awt.Container;

public abstract class UIInteraction {

	protected Container container;
	protected Interacter interacter;

	public UIInteraction(Interacter interacter, Container container) {
		this.interacter = interacter;
		this.container = container;
	}

	public final void start() {
		interacter.perform(this::perform);
	}

	protected abstract void perform();

}
