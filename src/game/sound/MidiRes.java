package game.sound;

import java.io.File;
import java.util.ArrayList;

import javax.sound.midi.MidiUnavailableException;

public class MidiRes
{
	private static String midiDirectory = "hex_midi/";

	public static void startMidi(MidiPlayer midiPlayer, MidiPlaylist playlist)
	{
		File folder = new File(midiDirectory);
		File[] files = folder.listFiles();
		ArrayList<String> songs = new ArrayList<String>();
		for(File f : files)
		{
			String name = f.getName();
			if(name.endsWith(".midi") || name.endsWith("mid"))
			songs.add(midiDirectory + name);
		}
		playlist = new MidiPlaylist("Test Songs", songs);
		MidiPlayer player = null;
		try {
			player = new MidiPlayer("Test MIDI Player", playlist);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		if (player != null) {
			player.start();
		}
	}
}
