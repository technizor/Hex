package game.sound;

import java.util.ArrayList;

/**
 * A MIDI playlist of songs.
 * 
 * @author Sherman Ying
 * @version December 19, 2012
 * @since 1.7
 */
public class MidiPlaylist
{
	// Looping mode constants
	public static final int LOOP_ALL = 0;
	public static final int LOOP_ONE = 2;
	public static final int NO_LOOP = 3;
	public static final int PLAY_ONE = 1;

	private int currentSong;
	private int loopingMode;
	private String playlistName;
	private boolean shuffling;
	// Playlist properties
	private ArrayList<MidiSong> songList;

	/**
	 * Creates a MIDI playlist with the given name and creates a list of songs
	 * from the file names.
	 * 
	 * @param name
	 *            the name of the playlist.
	 * @param songs
	 *            the list of songs to play.
	 */
	public MidiPlaylist(String name, ArrayList<String> songs) {
		playlistName = name;
		currentSong = 0;
		shuffling = false;
		loopingMode = LOOP_ALL;
		songList = new ArrayList<MidiSong>(songs.size());
		for (int song = 0; song < songs.size(); song++) {
			try {
				MidiSong midi = new MidiSong(songs.get(song));
				songList.add(midi);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Adds the song to the playlist.
	 * 
	 * @param song
	 *            the song to add.
	 */
	public void addSong(MidiSong song)
	{
		songList.add(song);
	}

	/**
	 * Adds the song to the playlist at the given position.
	 * 
	 * @param song
	 *            the song to add.
	 * @param index
	 *            the position to add the song.
	 */
	public void addSong(MidiSong song, int index)
	{
		songList.add(index, song);
	}

	/**
	 * Gives the track number of the song played.
	 * 
	 * @return the track number.
	 */
	public int getCurrentTrackNum()
	{
		return currentSong;
	}

	/**
	 * Gives the playlist's looping mode value.
	 * 
	 * @return the looping mode value.
	 */
	public int getLoopingMode()
	{
		return loopingMode;
	}

	/**
	 * Gives the name of the playlist.
	 * 
	 * @return the name of this playlist.
	 */
	public String getName()
	{
		return playlistName;
	}

	/**
	 * Gives the song at the given track number.
	 * 
	 * @param trackNum
	 *            the number of the song track.
	 * @return the song with the given track number.
	 */
	public MidiSong getSong(int trackNum)
	{
		return songList.get(trackNum % songList.size());
	}

	/**
	 * Gives the list of songs.
	 * 
	 * @return the list of MIDI songs.
	 */
	public ArrayList<MidiSong> getSongList()
	{
		return songList;
	}

	/**
	 * Tells whether the playlist shuffles the order of playing.
	 * 
	 * @return whether the playlist shuffles.
	 */
	public boolean isShuffling()
	{
		return shuffling;
	}

	public int nextSong()
	{
		int length = songList.size();
		if (!shuffling) {
			if (loopingMode == LOOP_ALL) {
				currentSong = (currentSong + 1) % length;
			} else if (loopingMode == PLAY_ONE) {
				currentSong = -1;
			} else if (loopingMode == NO_LOOP) {
				currentSong++;
				if (currentSong >= length)
					currentSong = -1;
			}
		} else {
			if (loopingMode == LOOP_ALL) {
				currentSong = (int) (Math.random() * length);
			}
		}
		return currentSong;
	}

	/**
	 * Sets the playlist looping mode. If the mode is invalid, sets to the
	 * default of looping every song.
	 * 
	 * @param mode
	 *            the looping mode
	 */
	public void setLoopingMode(int mode)
	{
		if (mode != NO_LOOP || mode != LOOP_ONE || mode != PLAY_ONE)
			loopingMode = LOOP_ALL;
		else
			loopingMode = mode;
	}

	/**
	 * Sets whether the playlist will shuffle.
	 * 
	 * @param shuffle
	 *            whether the playlist should shuffle the order of songs.
	 */
	public void setShuffling(boolean shuffle)
	{
		shuffling = shuffle;
	}
}
