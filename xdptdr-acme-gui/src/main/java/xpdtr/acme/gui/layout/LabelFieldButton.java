package xpdtr.acme.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import xpdtr.acme.gui.layout.LayoutAdapter;

public class LabelFieldButton extends LayoutAdapter {

	private int hspace;

	public LabelFieldButton(int hspace) {
		this.hspace = hspace;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Component l = parent.getComponent(0);
		Component c = parent.getComponent(1);
		Component b = parent.getComponent(2);

		int lw = (int) l.getMinimumSize().getWidth();
		int lh = (int) l.getMinimumSize().getHeight();

		int bw = (int) b.getMinimumSize().getWidth();
		int bh = (int) b.getMinimumSize().getHeight();

		int cw = (int) c.getMinimumSize().getWidth();
		int ch = (int) c.getMinimumSize().getHeight();

		return new Dimension(Math.max(lw, bw + cw + hspace), lh + Math.max(bh, ch));
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {

		Component l = parent.getComponent(0);
		Component c = parent.getComponent(1);
		Component b = parent.getComponent(2);

		int lw = (int) l.getPreferredSize().getWidth();
		int lh = (int) l.getPreferredSize().getHeight();

		int bw = (int) b.getPreferredSize().getWidth();
		int bh = (int) b.getPreferredSize().getHeight();

		int cw = (int) c.getPreferredSize().getWidth();
		int ch = (int) c.getPreferredSize().getHeight();

		return new Dimension(Math.max(lw, bw + cw + hspace), lh + Math.max(bh, ch));
	}

	@Override
	public void layoutContainer(Container parent) {

		Component l = parent.getComponent(0);
		int lw = (int) l.getPreferredSize().getWidth();
		int lh = (int) l.getPreferredSize().getHeight();

		Component c = parent.getComponent(1);
		int cw = (int) c.getPreferredSize().getWidth();
		int ch = (int) c.getPreferredSize().getHeight();

		Component b = parent.getComponent(2);
		int bw = (int) b.getPreferredSize().getWidth();
		int bh = (int) b.getPreferredSize().getHeight();

		Dimension newLSize = new Dimension(l.getPreferredSize());
		Dimension newCSize = new Dimension(c.getPreferredSize());
		Dimension newBSize = new Dimension(b.getPreferredSize());

		int w = Math.max((int) lw, (int) cw + (int) bw);

		newLSize.setSize(w, lh);
		newCSize.setSize(w - bw - hspace, Math.max(ch, bh));
		newBSize.setSize(bw, Math.max(ch, bh));

		int x = 0;

		l.setLocation(x, 0);
		l.setSize(newLSize);

		c.setLocation(x, lh);
		c.setSize(newCSize);

		b.setLocation(x + hspace + w - bw, lh);
		b.setSize(newBSize);

	}

}
