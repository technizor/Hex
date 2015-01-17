package game.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiPlayer implements MetaEventListener
{
	private Sequence currentSequence;
	private String playerName;
	// Player properties
	private MidiPlaylist playlist;
	private Sequencer sequencer;

	/**
	 * Creates a new MIDI player with the given name and playlist.
	 * 
	 * @param name
	 *            the name of the MIDI player.
	 * @param songList
	 *            the playlist of the MIDI player.
	 * @throws MidiUnavailableException
	 *             thrown when the default MIDI sequencer is not available.
	 */
	public MidiPlayer(String name, MidiPlaylist songList)
			throws MidiUnavailableException {
		playerName = name;
		playlist = songList;
		sequencer = MidiSystem.getSequencer();
		sequencer.addMetaEventListener(this);
	}

	/**
	 * Gives the current sequence being played.
	 * 
	 * @return the current sequence.
	 */
	public Sequence getCurrentSequence()
	{
		return currentSequence;
	}

	/**
	 * Gives the name of the MIDI player.
	 * 
	 * @return the name of the MIDI player.
	 */
	public String getName()
	{
		return playerName;
	}

	/**
	 * Gives the playlist of this MIDI player.
	 * 
	 * @return the playlist.
	 */
	public MidiPlaylist getPlaylist()
	{
		return playlist;
	}

	@Override
	public void meta(MetaMessage event)
	{
		if (event.getType() == 0x2F) // end of track message id
		{
			int mode = playlist.getLoopingMode();
			if (mode == MidiPlaylist.LOOP_ALL || mode == MidiPlaylist.NO_LOOP) {
				if (sequencer != null && sequencer.isOpen()) {
					try {
						sequencer.setSequence(playlist.getSong(
								playlist.nextSong()).getSequence());
						sequencer.setTickPosition(0);
						sequencer.start();
						System.out.println("Now playing: "
								+ playlist.getSong(
										playlist.getCurrentTrackNum())
										.getName());
					} catch (InvalidMidiDataException e) {
						e.printStackTrace();
					}
				}
			} else if (mode == MidiPlaylist.LOOP_ONE) {
				sequencer.setTickPosition(0);
				sequencer.start();
			}
		}
	}

	public void start()
	{
		try {
			sequencer.open();
		} catch (MidiUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sequencer.setSequence(playlist.getSong(
					playlist.getCurrentTrackNum()).getSequence());
			sequencer.start();
			System.out
					.println("Now playing: "
							+ playlist.getSong(playlist.getCurrentTrackNum())
									.getName());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public void stop()
	{
		sequencer.close();
	}
}
