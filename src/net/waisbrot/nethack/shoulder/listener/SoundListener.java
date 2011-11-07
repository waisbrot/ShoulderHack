package net.waisbrot.nethack.shoulder.listener;

import java.util.regex.Pattern;

import modlib.terminator.model.TextLine;
import net.waisbrot.nethack.shoulder.client.Client.DataListener;
import net.waisbrot.nethack.shoulder.dungeon.DungeonFeature;
import net.waisbrot.nethack.shoulder.dungeon.DungeonThing;
import net.waisbrot.nethack.shoulder.dungeon.SpecialRoom;
import net.waisbrot.nethack.shoulder.helpers.LevelNotes;

public class SoundListener implements DataListener {
	private static enum Sound {
		Sink("You hear a slow drip.|You hear a gurgling noise.|You hear dishes being washed!", DungeonFeature.Sink),
		Fountain("You hear water falling on coins.|You hear the splashing of a naiad.|You hear bubbling water.|You hear a soda fountain!", DungeonFeature.Fountain),
		Vault("You hear the footsteps of a guard on patrol.|You hear someone counting money.|You hear Ebenezer Scrooge!|You hear the quarterback calling the play.", SpecialRoom.Vault),
		EmptyVault("You hear someone searching.", SpecialRoom.EmptyVault),
		Oracle("You hear snoring snakes.|You hear a strange wind.|You hear convulsive ravings.|You hear someone say \"No more woodchucks!\"|You hear a loud ZOT!", SpecialRoom.Oracle),
		Throne("You hear the tones of courtly conversation.|You hear a sceptre pounded in judgment.||Someone shouts \"Off with (:?his|her) head!|You hear Queen Beruthiel's cats!", SpecialRoom.Throne),
		Beehive("You hear a low buzzing.|You hear an angry drone.|You hear bees in your bonnet!|You hear bees in your (nonexistent) bonnet!", SpecialRoom.Beehive),
		Graveyard("You suddenly realize it is unnaturally quiet.|The \\w+ on the back of your \\w+ stands up.|The \\w+ on your \\w+ seems to stand up.", SpecialRoom.Graveyard),
		Barracks("You hear blades being honed.|You hear loud snoring.|You hear dice being thrown.|You hear General MacArthur!", SpecialRoom.Barracks),
		Swamp("You hear mosquitoes!|You smell marsh gas!|You hear Donald Duck!", SpecialRoom.Swamp),
		Shop("You hear someone cursing shoplifters.|You hear the chime of a cash register.|You hear Neiman and Marcus arguing!", SpecialRoom.Shop);


		public final Pattern pattern;
		public final DungeonThing goesWith;
		Sound(String pattern, DungeonThing goesWith) {
			this.pattern = Pattern.compile(".*(?:"+pattern+").*");
			this.goesWith = goesWith;
		}
	}

	@Override
	public void data(TextLine line, int i) {
		if (i > 21) {
			String str = line.getString();
			for (Sound sound : Sound.values()) {
				if (sound.pattern.matcher(str).matches()) {
					LevelNotes.getInstance().noteDungeonThing(sound.goesWith);
				}
			}
		}
	}
}
