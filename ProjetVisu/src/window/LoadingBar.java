package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class LoadingBar extends JPanel {
	private static final long serialVersionUID = 2L;
	private double percentage;
	private int width;
	private int height;

	public LoadingBar(double percentage) {
		this.percentage = percentage;
		width = 300;
		height = 20;
		percentage = 0.0;
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
	}
	
	public void setChargement(double percentage) {
		this.percentage = percentage;
		if(percentage < 0.0)
			percentage = 0.0;
		if(percentage > 100.0)
			percentage = 100.0;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setColor(Color.BLACK);
		graphics.clearRect(0, 0, width - 1, height - 1);
		graphics.drawLine(0, 0, width - 1, 0);
		graphics.drawLine(0, height - 1, width - 1, height - 1);
		graphics.drawLine(0, 0, 0, height - 1);
		graphics.drawLine(width - 1, 0, width - 1, height - 1);
		for(int i = 1; i < height / 2 + 1; ++i) {
			graphics.setColor(new Color((float) ((100 - percentage) / 100), 0.0f, (float) (percentage / 100), 1.0f - (float) ((height - i) / 2) / (float) height));
			graphics.drawLine(1, i, (int) (1.0 + percentage * (double) (width - 2) / 100.0), i);
			graphics.drawLine(1, height - 1 - i, (int) (1.0 + percentage * (double) (width - 2) / 100.0), height - 1 - i);
		}
	}
}
