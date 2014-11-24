package window;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import utils.Utils;
import datas.GazDatas;
import datas.InterpolatedDatas;
import exception.MyOutOfBoundException;

public class Window implements Runnable {
	private JFrame window;
	private JList<String> liste;
	private DrawArea view;
	private State state;
	private JButton open;
	private JPanel clPanel;
	private CardLayout cl;
	private JPanel openOptions;
	private JPanel chargement;
	private LoadingBar loadingBar;
	
	private String selectedValue;
	
	private JRadioButton shepard;
	private JComboBox<String> day;
	private JComboBox<String> hour;
	private JTextField width;
	private JTextField height;
	private Window thisWindow;
	
	private GazDatas gazDatas;
	
	private InterpolatedDatas interpolatedDatas;
	
	private File[] files;
	
	public enum State {launch, openOptions, chargement}
	
	public Window() {
		thisWindow = this;
		state = State.launch;
		window = new JFrame("Projet - Visualisation");
		window.setSize(new Dimension(800, 600));
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.setMinimumSize(new Dimension(195, 200));
		fillLeftSide();
		clPanel = new JPanel();
		cl = new CardLayout();
		clPanel.setLayout(cl);
		window.add(clPanel, BorderLayout.CENTER);
		clPanel.add(new JLabel(), State.launch.toString());
		openOptions = new JPanel();
		clPanel.add(openOptions, State.openOptions.toString());
		chargement = new JPanel();
		clPanel.add(chargement, State.chargement.toString());
		
		view = new DrawArea();

		SwingUtilities.invokeLater(this);
	}

	public void run() {
		cl.show(clPanel, state.toString());
		window.setVisible(true);
	}
	
	public void changeState(State state) {
		this.state = state;
		run();
	}
	
	private void fillOpenOptions() {
		openOptions.removeAll();
		openOptions.setLayout(new GridLayout(14, 1));
		JPanel panel0 = new JPanel();
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(new JLabel(selectedValue, JLabel.CENTER), BorderLayout.CENTER);
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		panel3.add(new JLabel("Choisissez une methode :", JLabel.CENTER));
		JPanel panel4 = new JPanel();
		ButtonGroup group = new ButtonGroup();
		shepard = new JRadioButton("Shepard");
		JRadioButton hardy = new JRadioButton("Hardy Quadrics");
		group.add(shepard);
		group.add(hardy);
		shepard.setSelected(true);
		Box box = Box.createHorizontalBox();
		box.add(shepard);
		box.add(hardy);
		panel4.add(box);
		JPanel panel5 = new JPanel();
		JPanel panel6 = new JPanel();
		panel6.add(new JLabel("Choisissez une date :", JLabel.CENTER));
		JPanel panel7 = new JPanel();
		day = new JComboBox<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(gazDatas.getBeginDate());
		while(!Utils.getString(gazDatas.getEndDate()).split("-")[0].equals(Utils.getString(cal.getTime()).split("-")[0])) {
			day.addItem(Utils.getString(cal.getTime()).split("-")[0]);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		day.addItem(Utils.getString(cal.getTime()).split("-")[0]);
		hour = new JComboBox<String>();
		for(int i = 0; i < 10; ++i) {
			hour.addItem("0" + i + ":00");
		}
		for(int i = 10; i < 24; ++i) {
			hour.addItem(i + ":00");
		}
		panel7.add(day);
		panel7.add(hour);
		JPanel panel8 = new JPanel();
		JPanel panel9 = new JPanel();
		panel9.add(new JLabel("Choisissez une résolution :", JLabel.CENTER));
		JPanel panel10 = new JPanel();
		Box box2 = Box.createHorizontalBox();
		int originalWidth = 50;
		width = new JTextField("" + originalWidth);
		height = new JTextField("" + (int) ((double) originalWidth * (gazDatas.getMaxLatitude() - gazDatas.getMinLatitude()) / (gazDatas.getMaxLongitude() - gazDatas.getMinLongitude())));
		width.setPreferredSize(new Dimension(50, 20));
		height.setPreferredSize(new Dimension(50, 20));
		box2.add(width);
		box2.add(new JLabel("x"));
		box2.add(height);
		panel10.add(box2);
		JPanel panel11 = new JPanel();
		JPanel panel12 = new JPanel();
		JButton valider = new JButton("Valider");
		valider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				state = State.chargement;
				int methode = InterpolatedDatas.HardyMethod;
				if(shepard.isSelected()) {
					methode = InterpolatedDatas.ShepardMethod;
				}
				String[] date = ((String) day.getSelectedItem()).split("/");
				fillChargement();
				run();
				try {
					Date selectedDate = Utils.getDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(((String) hour.getSelectedItem()).split(":")[0]), 0, 0);
					System.out.println(Utils.getString(selectedDate));
					new LoadingInterpolatedDatas(gazDatas, selectedDate,
							methode, Integer.parseInt(width.getText()), Integer.parseInt(height.getText()), thisWindow);
				} catch (NumberFormatException | MyOutOfBoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel12.add(valider);
		openOptions.add(panel0);
		openOptions.add(panel1);
		openOptions.add(panel2);
		openOptions.add(panel3);
		openOptions.add(panel4);
		openOptions.add(panel5);
		openOptions.add(panel6);
		openOptions.add(panel7);
		openOptions.add(panel8);
		openOptions.add(panel9);
		openOptions.add(panel10);
		openOptions.add(panel11);
		openOptions.add(panel12);
		openOptions.validate();
	}
	
	private void fillChargement() {
		chargement.removeAll();
		chargement.setLayout(new GridLayout(6, 1));
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		loadingBar = new LoadingBar(0.0);
		chargement.add(panel1);
		chargement.add(panel2);
		chargement.add(panel3);
		panel3.add(new JLabel("Chargement :", JLabel.CENTER));
		panel4.add(loadingBar);
		chargement.add(panel4);
		chargement.validate();
	}
	
	public void setChargement(double percentage) {
		loadingBar.setChargement(percentage*100);
	}
	
	public void setInterpolatedDatas(InterpolatedDatas datas) {
		interpolatedDatas = datas;
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
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedValue = liste.getSelectedValue();
				gazDatas = new GazDatas("In" + File.separator + selectedValue);
				state = State.openOptions;
				fillOpenOptions();
				run();
			}
		});
		botSide.add(open);
		contentPanel.add(center, BorderLayout.CENTER);
		contentPanel.add(botSide, BorderLayout.SOUTH);
		window.add(contentPanel, BorderLayout.WEST);
	}
}
