package xpdtr.acme.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import xpdtr.acme.gui.layout.LayoutAdapter;

public class FullWidthLayout extends LayoutAdapter {

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return parent.getComponent(0).getPreferredSize();
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return parent.getComponent(0).getMinimumSize();
	}

	@Override
	public void layoutContainer(Container parent) {
		Component c = parent.getComponent(0);
		int w = parent.getWidth();
		double h = c.getPreferredSize().getHeight();
		c.setSize(w, (int) h);
		c.setLocation(0, 0);

	}
}
