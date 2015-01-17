package game.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

/**
 * A Musical Instrument Digital Interface utility class.
 * 
 * @author Sherman Ying
 * @version December 19, 2012
 * @since 1.7
 */
public class MidiSong
{
	private String fileName;
	private Sequence midi;
	// MIDI properties
	private String name;

	/**
	 * Constructs a song from the given MIDI file.
	 * 
	 * @param fileName
	 *            the name of the MIDI file
	 * @throws InvalidMidiDataException
	 *             when the file is corrupted or is the incorrect format
	 * @throws IOException
	 *             when the file does not exist
	 */
	public MidiSong(String fileName) throws InvalidMidiDataException,
			IOException {
		this.name = fileName.substring(0, fileName.lastIndexOf("."));
		this.fileName = fileName;
		midi = MidiSystem.getSequence(new File(fileName));
	}

	/**
	 * Constructs a song from the given MIDI file and name.
	 * 
	 * @param name
	 *            the name of the song
	 * @param fileName
	 *            the name of the MIDI file
	 * @throws InvalidMidiDataException
	 *             when the file is corrupted or is the incorrect format
	 * @throws IOException
	 *             when the file does not exist
	 */
	public MidiSong(String name, String fileName)
			throws InvalidMidiDataException, IOException {
		this.name = name;
		this.fileName = fileName;
		midi = MidiSystem.getSequence(new File(fileName));
	}

	/**
	 * Gives the file reference to the MIDI file.
	 * 
	 * @return the file referring to the MIDI file
	 */
	public File getFile()
	{
		return new File(fileName);
	}

	/**
	 * Gives the file name.
	 * 
	 * @return the file name
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Gives the name of the song.
	 * 
	 * @return the name
	 */
	public String getName()
	{

		return name;
	}

	/**
	 * Gives the MIDI sequence of the song.
	 * 
	 * @return the MIDI sequence
	 */
	public Sequence getSequence()
	{
		return midi;
	}
}
