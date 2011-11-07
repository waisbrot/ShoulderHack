package net.waisbrot.nethack.shoulder.dungeon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import modlib.terminator.model.Location;
import net.waisbrot.nethack.shoulder.helpers.LevelNotesTreeModel;

public class DungeonLevel {
	private final int floorNumber;
	private final SortedMap<DungeonThing,List<Location>> immobiles = new TreeMap<DungeonThing,List<Location>>();
	public DungeonLevel(int number) {
		floorNumber = number;
	}
	public void awareOf(DungeonThing thing) {
		if (!immobiles.containsKey(thing)) {
			immobiles.put(thing, new ArrayList<Location>(0));
			LevelNotesTreeModel.getInstance().nodeChanged(this);
		}
	}
	@Override
	public String toString() {
		return String.valueOf(floorNumber);
	}
	
	public Iterator<Entry<DungeonThing, List<Location>>> immobilesIterator() {
		return immobiles.entrySet().iterator();
	}
	public int immobilesSize() {
		return immobiles.size();
	}
}
