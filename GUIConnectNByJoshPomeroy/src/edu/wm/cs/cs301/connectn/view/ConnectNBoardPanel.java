/**
 * Class that creates the visual board grid in the GUI.
 * Writes to the GUI when moves are played.
 * Credit to the Wordle project for drawing the boxes and centered strings.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import edu.wm.cs.cs301.connectn.model.ConnectNModel;
import edu.wm.cs.cs301.connectn.model.Location;
import edu.wm.cs.cs301.connectn.model.AppColors;

public class ConnectNBoardPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private int  leftMargin,  width;
	
	private final int topMargin, charWidth;

	private final Insets insets;

	private Rectangle[][] grid;
	
	private final ConnectNModel model;
	
	private final ConnectNFrame view;
	
	public ConnectNBoardPanel(ConnectNFrame view, ConnectNModel model) {
		this.model = model;
		this.view = view;
		this.topMargin = 0;
		this.charWidth = 64;
		this.insets = new Insets(0, 6, 6, 6);
		this.width = view.getWidth();
		
		int columns = (charWidth + insets.right) * model.getColumnLimit();
		this.leftMargin = (width - columns) / 2;
		int rows = (charWidth + insets.bottom) * model.getRowLimit() + 2 * topMargin;
		this.setPreferredSize(new Dimension(width, rows));

		this.grid = calculateRectangles();
	}
	
	/**
	 * Creates the necessary number of rectangles for the board size
	 * @return - The finished grid
	 */
	private Rectangle[][] calculateRectangles() {
		Rectangle[][] grid = new Rectangle[model.getRowLimit()][model.getColumnLimit()];
		int columns = (charWidth + insets.right) * model.getColumnLimit(); // 70*7
		this.leftMargin = (width - columns) / 2; 
		
		int x = leftMargin;
		int y = topMargin;

		for (int row = 0; row < model.getRowLimit(); row++) {
			for (int column = 0; column < model.getColumnLimit(); column++) {
				grid[row][column] = new Rectangle(x, y, charWidth, charWidth);
				x += charWidth + insets.right;
			}
			x = leftMargin;
			y += charWidth + insets.bottom;
		}
		return grid;
	}
	
	/**
	 * Draws the grid
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		Font charFont = AppFonts.getMediumTextFont();
		Location[][] boardGrid = model.getBoardGrid();
		for (int row = 0; row < grid.length; row++) {
			for (int column = 0; column < grid[row].length; column++) {
				Rectangle r = grid[row][column];
				Location location = boardGrid[row][column];
				drawOutline(g2d, r);
				drawPiecePlayed(g2d, location, r, charFont);
			}
		}
	}
	
	private void drawOutline(Graphics2D g2d, Rectangle r) {
		int x = r.x + 1;
		int y = r.y + 1;
		int width = r.width - 2;
		int height = r.height - 2;
		g2d.setColor(AppColors.OUTLINE);
		g2d.setStroke(new BasicStroke(3f));
		g2d.drawLine(x, y, x + width, y);
		g2d.drawLine(x, y + height, x + width, y + height);
		g2d.drawLine(x, y, x, y + height);
		g2d.drawLine(x + width, y, x + width, y + height);
	}
	
	private void drawPiecePlayed(Graphics2D g2d, Location location, Rectangle r, Font font) {
		if (location != null) {
			g2d.fillRect(r.x, r.y, r.width, r.height);
			g2d.setColor(AppColors.BLACK);
			drawCenteredString(g2d, Character.toString(location.getToken()), r, font);
		}
	}
	
	public void resetBoard() {
		width = view.getWidth();
		int columns = (charWidth + insets.right) * model.getColumnLimit();
		leftMargin = (width - columns) / 2;
		int rows = (charWidth + insets.bottom) * model.getRowLimit() + 2 * topMargin;
		setPreferredSize(new Dimension(width, rows));
		this.grid = calculateRectangles();
	}
	
	private void drawCenteredString(Graphics2D g2d, String text, Rectangle rect, Font font) {
		FontMetrics metrics = g2d.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2)
				+ metrics.getAscent();

		g2d.setFont(font);
		g2d.drawString(text, x, y);
	}
}