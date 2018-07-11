package xpdtr.acme.gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import xpdtr.acme.gui.layout.LayoutAdapter;

public class LabelsAndFields extends LayoutAdapter {

	private int hspace;
	private int rightMargin;

	public LabelsAndFields(int hspace, int rightMargin) {
		this.hspace = hspace;
		this.rightMargin = rightMargin;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {

		double w = 0;
		double h = 0;

		int n = parent.getComponentCount();
		for (int i = 0; i < n; i += 2) {

			Dimension labelSize = parent.getComponent(i).getMinimumSize();
			double lw = labelSize.getWidth();
			double lh = labelSize.getHeight();

			Dimension fieldSize = parent.getComponent(i + 1).getMinimumSize();
			double fw = fieldSize.getWidth();
			double fh = fieldSize.getHeight();

			w = Math.max(w, lw + hspace + fw + rightMargin);
			h += Math.max(lh, fh);

		}

		Dimension dim = new Dimension();
		dim.setSize(w, h);
		return dim;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {

		double w = 0;
		double h = 0;

		int n = parent.getComponentCount();
		for (int i = 0; i < n; i += 2) {

			Dimension labelSize = parent.getComponent(i).getPreferredSize();
			double lw = labelSize.getWidth();
			double lh = labelSize.getHeight();

			Dimension fieldSize = parent.getComponent(i + 1).getPreferredSize();
			double fw = fieldSize.getWidth();
			double fh = fieldSize.getHeight();

			w = Math.max(w, lw + hspace + fw + rightMargin);
			h += Math.max(lh, fh);

		}

		Dimension dim = new Dimension();
		dim.setSize(w, h);
		return dim;
	}

	@Override
	public void layoutContainer(Container parent) {

		double labelWidth = 0;

		int n = parent.getComponentCount();
		for (int i = 0; i < n; i += 2) {
			Dimension labelSize = parent.getComponent(i).getPreferredSize();
			double lw = labelSize.getWidth();
			labelWidth = Math.max(lw, labelWidth);
		}

		double fieldWidth = parent.getWidth()- labelWidth - hspace - rightMargin;

		double y = 0;

		for (int i = 0; i < n; i += 2) {

			Component label = parent.getComponent(i);
			Dimension labelSize = label.getPreferredSize();
			double lw = labelSize.getWidth();
			double lh = labelSize.getHeight();

			Component field = parent.getComponent(i + 1);
			Dimension fieldSize = field.getPreferredSize();
			double fh = fieldSize.getHeight();

			double labelYOffset = 0;
			double fieldYOffset = 0;
			if (lh < fh) {
				labelYOffset = (fh - lh) / 2;
			} else {
				fieldYOffset = (lh - fh) / 2;
			}

			label.setSize(label.getPreferredSize());
			label.setLocation((int) (labelWidth - lw), (int) (y + labelYOffset));

			field.setSize(new Dimension((int) fieldWidth, (int) (field.getPreferredSize().getHeight())));
			field.setLocation((int) (labelWidth + hspace), (int) (y + fieldYOffset));

			y += Math.max(lh, fh);
		}

	}

}
