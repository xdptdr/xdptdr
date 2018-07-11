package xpdtr.acme.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import xpdtr.acme.gui.layout.LayoutAdapter;

public class StackedLayout extends LayoutAdapter {

	private int vspace;
	private int topPadding;

	public StackedLayout(int vspace) {
		this.vspace = vspace;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {

		double w = 0;
		double h = topPadding;
		for (int i = 0; i < parent.getComponentCount(); ++i) {
			Component c = parent.getComponent(i);
			double cw = c.getPreferredSize().getWidth();
			double ch = c.getPreferredSize().getHeight();
			w = Math.max(w, cw);
			h += ch + vspace;

		}

		return new Dimension((int) w, (int) h);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		double w = 0;
		double h = topPadding;
		for (int i = 0; i < parent.getComponentCount(); ++i) {
			Component c = parent.getComponent(i);
			double cw = c.getMinimumSize().getWidth();
			double ch = c.getMinimumSize().getHeight();
			w = Math.max(w, cw);
			h += ch;

		}

		return new Dimension((int) w, (int) h);
	}

	@Override
	public void layoutContainer(Container parent) {

		double w = 0;

		for (int i = 0; i < parent.getComponentCount(); ++i) {
			w = Math.max(w, parent.getComponent(i).getPreferredSize().getWidth());
		}

		w = parent.getWidth();

		int x = parent.getInsets().left;

		double h = topPadding;
		for (int i = 0; i < parent.getComponentCount(); ++i) {
			Component c = parent.getComponent(i);
			double ch = c.getMinimumSize().getHeight();

			c.setLocation(x, (int) h);
			c.setSize((int) w, (int) c.getPreferredSize().getHeight());

			h += ch + vspace;
		}

	}

	public void setTopPadding(int topPadding) {
		this.topPadding = topPadding;

	}

}