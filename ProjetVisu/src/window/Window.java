package window;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window implements Runnable {
	private JFrame window;
	private DrawArea view;
	
	public Window() {
		window = new JFrame("Projet - Visualisation");
		view = new DrawArea();
	}

	public void run() {
		window.setPreferredSize(new Dimension(800, 600));
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
