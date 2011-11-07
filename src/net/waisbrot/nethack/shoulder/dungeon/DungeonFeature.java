package net.waisbrot.nethack.shoulder.dungeon;

public enum DungeonFeature implements DungeonThing {
	Sink, Fountain;
	
	@Override public boolean mobile() { return false; };
}
