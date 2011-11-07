package net.waisbrot.nethack.shoulder.helpers;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.waisbrot.nethack.shoulder.dungeon.DungeonBranch;
import net.waisbrot.nethack.shoulder.dungeon.DungeonLevel;
import net.waisbrot.nethack.shoulder.dungeon.DungeonThing;

public class LevelNotes extends JFrame {
	private static LevelNotes self = null;
	public static LevelNotes getInstance() {
		if (self == null)
			self = new LevelNotes();
		return self; 
	}
	
	private JLabel statusLabel;
	private JTree interestTree;
	
	/**
	 * Create the frame.
	 */
	public LevelNotes() {
		setLocation(new Point(50, 50));		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);
		
		JScrollPane scrollPane = new JScrollPane();
		mainPane.add(scrollPane, BorderLayout.CENTER);
		
		interestTree = new JTree(LevelNotesTreeModel.getInstance());
		scrollPane.setViewportView(interestTree);
		
		JPanel statusPanel = new JPanel();
		mainPane.add(statusPanel, BorderLayout.SOUTH);
		
		statusLabel = new JLabel("New label");
		statusPanel.add(statusLabel);
	}
	
	private DungeonBranch currentBranch = DungeonBranch.DungeonsOfDoom;
	private DungeonLevel currentLevel = DungeonBranch.DungeonsOfDoom.setLevel(1);
	
	public void setDlvl(final int lvl) {
		DungeonLevel newLevel = currentBranch.setLevel(lvl);
		if (newLevel != currentLevel) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					statusLabel.setText("Dlvl: "+lvl);
				}
			});
		}
	}
	
	public void noteDungeonThing(DungeonThing thing) {
		currentLevel.awareOf(thing);
	}	
}
