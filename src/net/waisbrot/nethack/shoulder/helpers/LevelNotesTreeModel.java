package net.waisbrot.nethack.shoulder.helpers;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import modlib.terminator.model.Location;
import net.waisbrot.nethack.shoulder.dungeon.DungeonBranch;
import net.waisbrot.nethack.shoulder.dungeon.DungeonLevel;
import net.waisbrot.nethack.shoulder.dungeon.DungeonThing;

/**
 * Represents the Nethack dungeon map and some important features in the dungeon.
 * Assumes the following structure:
 * Nethack (root node)
 * -Dungeons of Doom
 *   - 1  (a level in the dungeons of doom)
 *   - 2 
 *    - fountain[2]  (a feature on the second floor)
 *   - 3
 *    - stairs to Gnomish Mines
 * - Gnomish Mines
 *   - 4  (a level in the gnomish mines)
 *   - 5
 *     - minetown   (a feature [special level] on the 5th floor of the mines) 
 * @author nate
 *
 */
public class LevelNotesTreeModel implements TreeModel {	
	private final String root = "Nethack";
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
	private static LevelNotesTreeModel self;
	
	public static LevelNotesTreeModel getInstance() {
		if (self == null)
			self = new LevelNotesTreeModel();
		return self;
	}
	
	private LevelNotesTreeModel() {}
	
	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent == root) {
			return DungeonBranch.values()[index];
		} else if (parent instanceof DungeonBranch) {
			return ((DungeonBranch)parent).getLevel(index);
		} else if (parent instanceof DungeonLevel) {
			Iterator<Entry<DungeonThing, List<Location>>> it = ((DungeonLevel)parent).immobilesIterator();
			for(int i = 0; i < index-1 && it.hasNext(); i++)
				it.next();
			assert it.hasNext();
			return it.next();
		} else {
			return null;
		}
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent == root) {
			return DungeonBranch.values().length;
		} else if (parent instanceof DungeonBranch) {
			return ((DungeonBranch)parent).size();
		} else if (parent instanceof DungeonLevel) {
			return ((DungeonLevel)parent).immobilesSize();
		} else {
			return 0;
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		throw new UnsupportedOperationException("Why do we need this?");
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object arg0) {
		return false;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		listeners.remove(arg0);		
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		throw new UnsupportedOperationException("Can't change the tree this way");
	}
	
	public void nodeChanged(Object parent) {
		TreeModelEvent e;
		if (parent == root) {
			e = new TreeModelEvent(this, new TreePath(root));
		} else if (parent instanceof DungeonBranch) {
			e = new TreeModelEvent(this, new TreePath(new Object[]{root, parent}));
		} else {
			e = null;
		}
		for (TreeModelListener l : listeners)
			l.treeStructureChanged(e);
	}

	private static class Tester extends JFrame {
		public Tester() {
		setLocation(new Point(50, 50));		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);
		
		JScrollPane scrollPane = new JScrollPane();
		mainPane.add(scrollPane, BorderLayout.CENTER);
		
		TreeModel model = new LevelNotesTreeModel();
		JTree interestTree = new JTree(model);
		scrollPane.setViewportView(interestTree);
		
		JPanel statusPanel = new JPanel();
		mainPane.add(statusPanel, BorderLayout.SOUTH);		
		}
	}
	public static void main(String[] args) {
		Tester t = new Tester();
		t.setVisible(true);
	}

}
