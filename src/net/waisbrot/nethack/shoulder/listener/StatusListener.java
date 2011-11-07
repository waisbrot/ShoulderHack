package net.waisbrot.nethack.shoulder.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modlib.terminator.model.TextLine;
import net.waisbrot.nethack.shoulder.client.Client.DataListener;
import net.waisbrot.nethack.shoulder.helpers.LevelNotes;

public class StatusListener implements DataListener {
	private static final Pattern BOTTOM_LINE = Pattern.compile("Dlvl:(\\d+)\\s*\\$:(\\d+)\\s*HP:(\\d+)[(](\\d+)[)]\\s*Pw:(\\d+)[(](\\d+)[)]\\s*AC:(\\d+)\\s*Xp:(\\d+)/(\\d+)\\s*T:(\\d+)");
	@Override
	public void data(TextLine s, int i) {
		if (i == 0) {
			Matcher m = BOTTOM_LINE.matcher(s.getString());
			if (m.matches()) {
				LevelNotes.getInstance().setDlvl(Integer.parseInt(m.group(1)));
			}
		}
	}
}
