package xpdtr.acme.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import xpdtr.acme.gui.layout.LayoutAdapter;

public class SameWidthLayout extends LayoutAdapter {

	private int vspace;

	public SameWidthLayout(int vspace) {
		this.vspace = vspace;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {

		double w = 0;
		double h = 0;

		int n = parent.getComponentCount();
		for (int i = 0; i < n; ++i) {
			Dimension sz = parent.getComponent(i).getMinimumSize();
			w = Math.max(w, sz.getWidth());
			h += sz.getHeight() + vspace;
		}

		return new Dimension((int) w, (int) h);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {

		double w = 0;
		double h = 0;

		int n = parent.getComponentCount();
		for (int i = 0; i < n; ++i) {
			Dimension sz = parent.getComponent(i).getPreferredSize();
			w = Math.max(w, sz.getWidth());
			h += sz.getHeight() + vspace;
		}

		return new Dimension((int) w, (int) h);
	}

	@Override
	public void layoutContainer(Container parent) {

		double width = 0;

		int n = parent.getComponentCount();
		for (int i = 0; i < n; ++i) {
			Dimension sz = parent.getComponent(i).getMinimumSize();
			width = Math.max(width, sz.getWidth());
		}

		double y = 0;
		for (int i = 0; i < n; ++i) {
			Component c = parent.getComponent(i);
			c.setSize((int) width, (int) c.getPreferredSize().getHeight());
			c.setLocation(0, (int) y);
			y += c.getPreferredSize().getHeight() + vspace;
		}

	}
}
