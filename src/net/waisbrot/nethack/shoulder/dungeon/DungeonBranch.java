package net.waisbrot.nethack.shoulder.dungeon;

import java.util.ArrayList;

import net.waisbrot.nethack.shoulder.helpers.LevelNotesTreeModel;

public enum DungeonBranch {
	DungeonsOfDoom("Dungeons of Doom"), GnomishMines("Gnomish Mines"), Sokoban("Sokoban"), Quest("The Quest"), Gehennom("Gehennom"), VladsTower("Vlad's Tower"), EndGame("The End Game");
	private final String name;
	private ArrayList<DungeonLevel> floors = new ArrayList<DungeonLevel>();
	private final int MAX_DLVL = 100;  // sanity checker
	private DungeonBranch(String s) {
		name = s;
	}
	@Override
	public String toString() {
		return name + " ("+floors.size()+" levels seen)";
	}
	public DungeonLevel getLevel(int i) {
		return floors.get(i);
	}
	public DungeonLevel setLevel(int i) {
		assert i >= 0;
		assert i < MAX_DLVL;
		boolean changed = false;
		while (floors.size() <= i) {
			floors.add(new DungeonLevel(floors.size()));
			changed = true;
		}
		if (changed)
			LevelNotesTreeModel.getInstance().nodeChanged(this);
		return getLevel(i);
	}
	public int size() {
		return floors.size();
	}
}
