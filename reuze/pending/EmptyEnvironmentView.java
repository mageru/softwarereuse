package reuze.pending;
import java.awt.Color;

import reuze.aa_i_Action;
import reuze.aa_i_Agent;
import reuze.aa_i_Environment;
import reuze.aa_i_EnvironmentState;
import reuze.ga_Point;

/**
 * Simple graphical environment view without content but with some useful
 * transformation features. The transformation features allow to scale
 * and translate 2D world coordinates into view coordinates. When creating
 * subclasses, it should normally be sufficient to override the method
 * {@link #paintComponent(java.awt.Graphics)}.
 * 
 * @author Ruediger Lunde
 */
public class EmptyEnvironmentView extends AgentAppEnvironmentView {
	
	private static final long serialVersionUID = 1L;
	private int borderTop = 10;
	private int borderLeft = 10;
	private int borderBottom = 10;
	private int borderRight = 10;

	private double offsetX;
	private double offsetY;
	private double scale;

	/**
	 * Specifies the number of pixels left blank on each side of the agent view
	 * panel.
	 */
	public void setBorder(int top, int left, int bottom, int right) {
		borderTop = top;
		borderLeft = left;
		borderBottom = bottom;
		borderRight = right;
	}

	/**
	 * Specifies a bounding box in world coordinates. The resulting
	 * transformation is able to display everything within this bounding box
	 * without scrolling.
	 */
	public void adjustTransformation(double minXW, double minYW, double maxXW,
			double maxYW) {
		// adjust coordinates relative to the left upper corner of the graph
		// area
		double scaleX = 1f;
		double scaleY = 1f;
		if (maxXW > minXW)
			scaleX = (getWidth() - borderLeft - borderRight) / (maxXW - minXW);
		if (maxYW > minYW)
			scaleY = (getHeight() - borderTop - borderBottom) / (maxYW - minYW);
		offsetX = -minXW;
		offsetY = -minYW;
		scale = Math.min(scaleX, scaleY);
	}

	/** Returns the x_view of a given point in world coordinates. */
	protected int x(ga_Point xyW) {
		return x(xyW.getX());
	}

	/** Returns the y_view of a given point in world coordinates. */
	protected int y(ga_Point xyW) {
		return y(xyW.getY());
	}

	/** Returns the x_view of a given x-value in world coordinates. */
	protected int x(double xW) {
		return (int) Math.round(scale * (xW + offsetX) + borderLeft);
	}

	/** Returns the y_view of a given y-value in world coordinates. */
	protected int y(double yW) {
		return (int) Math.round(scale * (yW + offsetY) + borderTop);
	}

	/** Transforms a given world length into view length. */
	protected int scale(int length) {
		return (int) Math.round(scale * length);
	}

	/** Stores the model and initiates painting. */
	@Override
	public void setEnvironment(aa_i_Environment env) {
		super.setEnvironment(env);
		repaint();
	}
	
	public void agentActed(aa_i_Agent agent, aa_i_Action action,
			aa_i_EnvironmentState resultingState) {
		repaint();
	}

	public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState resultingState) {
		repaint();
	}

	/**
	 * Shows a graphical representation of the agent in its environment.
	 * Override this dummy implementation to get a useful view of the agent!
	 */
	@Override
	public void paintComponent(java.awt.Graphics g) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, getWidth(), getHeight());
	}
}