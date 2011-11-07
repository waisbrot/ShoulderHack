package net.waisbrot.nethack.shoulder.dungeon;

public enum SpecialRoom implements DungeonThing {
	Zoo, Vault, EmptyVault, Oracle, Throne, Beehive, Graveyard, Barracks, Swamp, Shop;

	@Override public boolean mobile() { return false; };
}
