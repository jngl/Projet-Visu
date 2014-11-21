package window;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window {
	private JFrame window;
	
	public Window() {
		window = new JFrame("Projet - Visualisation");
		window.setPreferredSize(new Dimension(800, 600));
	}
}
