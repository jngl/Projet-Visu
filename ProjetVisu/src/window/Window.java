package window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class Window implements Runnable {
	private JFrame window;
	private JList<String> liste;
	private DrawArea view;
	private State state;
	private JButton open;
	
	private File[] files;
	
	public enum State {launch}
	
	public Window() {
		state = State.launch;
		window = new JFrame("Projet - Visualisation");
		window.setSize(new Dimension(800, 600));
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.setMinimumSize(new Dimension(195, 200));
		fillLeftSide();
		
		view = new DrawArea();
		
		SwingUtilities.invokeLater(this);
	}

	public void run() {
		switch(state) {
		case launch :
			window.setVisible(true);
		}
	}
	
	public void changeState(State state) {
		this.state = state;
		run();
	}
	
	private void fillLeftSide() {
		File in = new File(System.getProperty("user.dir") + File.separator + "In");
		files = in.listFiles();
		final DefaultListModel<String> filesName = new DefaultListModel<String>();
		for(int i = 0; i < files.length; ++i) {
			filesName.addElement(files[i].getName());
		}
		liste = new JList<String>(filesName);
		liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		liste.setSelectedIndex(0);
		liste.setVisibleRowCount(-1);
		JScrollPane listePanel = new JScrollPane(liste);
		listePanel.setPreferredSize(new Dimension(180, Integer.MAX_VALUE));
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout(5, 5));
		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		JPanel pan = new JPanel();
		pan.add(new JLabel("Files :"));
		center.add(pan, BorderLayout.NORTH);
		center.add(listePanel, BorderLayout.CENTER);
		JPanel botSide = new JPanel();
		open = new JButton("Ouvrir");
		botSide.add(open);
		contentPanel.add(center, BorderLayout.CENTER);
		contentPanel.add(botSide, BorderLayout.SOUTH);
		window.add(contentPanel, BorderLayout.WEST);
	}
}
