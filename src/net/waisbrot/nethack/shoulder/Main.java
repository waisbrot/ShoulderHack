package net.waisbrot.nethack.shoulder;

import java.awt.EventQueue;

import javax.swing.JFrame;

import modlib.terminator.model.TextLine;
import net.waisbrot.nethack.shoulder.client.Client;
import net.waisbrot.nethack.shoulder.client.Client.DataListener;
import net.waisbrot.nethack.shoulder.helpers.LevelNotes;
import net.waisbrot.nethack.shoulder.listener.SoundListener;
import net.waisbrot.nethack.shoulder.listener.StatusListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class Main {

	public JFrame mainFrame;
	private JTextArea logTextArea;
	private final Client client;
	
	public static Main self;
	private JLabel lblCounter;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		self = new Main();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					self.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		self.client.replay();
		self.client.begin();
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public Main() throws IOException {
		initialize_gui();
		client = new Client();
		client.addListener(new DataListener() {
			private long count = 0;
			ArrayList<String> lines = new ArrayList<String>();
			@Override
			public void data(TextLine s, int i) {
				while(lines.size() <= i)
					lines.add(null);
				lines.set(i, s.getString());

				StringBuilder sb = new StringBuilder();
				for (int j = lines.size()-1; j >= 0; j--) {
					if (j < 10)
						sb.append(' ');
					sb.append(j).append(':').append(lines.get(j)).append('\n');
				}
				logTextArea.setText(sb.toString());
				
				lblCounter.setText("Text line analyzed: " + ++count);
			}});
		client.addListener(new SoundListener());
		client.addListener(new StatusListener());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize_gui() {
		mainFrame = new JFrame();
		mainFrame.setTitle("ShoulderHack");
		mainFrame.setBounds(100, 100, 552, 475);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_MASK));
		mnFile.add(mntmQuit);
		
		JLabel lblProcessing = new JLabel("Processing:");
		mainFrame.getContentPane().add(lblProcessing, BorderLayout.NORTH);
		
		lblCounter = new JLabel("Text analyzed:");
		mainFrame.getContentPane().add(lblCounter, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		logTextArea = new JTextArea();
		logTextArea.setFont(new Font("Monaco", Font.PLAIN, 14));
		logTextArea.setEditable(false);
		scrollPane.setViewportView(logTextArea);
		
		LevelNotes.getInstance().setVisible(true);
	}

}
