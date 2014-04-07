package jp.ohwada.android.midi;

/*
 * Channel Mode Messages
 * http://www.midi.org/techspecs/midimessages.php
 */ 
public class MidiChannelModeMessage {
	
	// Channel Mode Message
    private final static byte[] MSG_ALL_SOUND_OFF 
    	= new byte[]{ (byte)0xB0, (byte)0x78, (byte)0x00 };
    private final static byte[] MSG_RESET_ALL_CONTROLLERS 
    	= new byte[]{ (byte)0xB0, (byte)0x79, (byte)0x00 };
    private final static byte[] MSG_LOCAL_CONTROL_OFF
    	= new byte[]{ (byte)0xB0, (byte)0x7A, (byte)0x00 };    	
    private final static byte[] MSG_LOCAL_CONTROL_ON
    	= new byte[]{ (byte)0xB0, (byte)0x7A, (byte)0x7F }; 
	private final static byte[] MSG_ALL_NOTE_OFF 
		= new byte[]{ (byte)0xB0, (byte)0x7B, (byte)0x00 };
	private final static byte[] MSG_OMNI_MODE_OFF
		= new byte[]{ (byte)0xB0, (byte)0x7C, (byte)0x00 };
	private final static byte[] MSG_OMNI_MODE_ON
		= new byte[]{ (byte)0xB0, (byte)0x7D, (byte)0x00 };
	private final static byte[] MSG_MONO_MODE_ON
		= new byte[]{ (byte)0xB0, (byte)0x7E, (byte)0x00 };
	private final static byte[] MSG_POLY_MODE_ON
		= new byte[]{ (byte)0xB0, (byte)0x7E, (byte)0x00 };
    									
	/*
	 * constructor
	 */ 		
	public MidiChannelModeMessage() {
		// dummy
	}

	/*
	 * All Sound Off
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getAllSoundOff( int channel ) {
		return buildCmd( channel, MSG_ALL_SOUND_OFF );		
	}

	/*
	 * Reset All Controllers
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getResetAllControllers( int channel ) {
		return buildCmd( channel, MSG_RESET_ALL_CONTROLLERS );		
	}

	/*
	 * Local Control Off
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getLocalControlOff( int channel ) {
		return buildCmd( channel, MSG_LOCAL_CONTROL_OFF );		
	}

	/*
	 * Local Control On
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getLocalControlOn( int channel ) {
		return buildCmd( channel, MSG_LOCAL_CONTROL_ON );		
	}

	/*
	 * All Note Off
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getAllNoteOff( int channel ) {
		return buildCmd( channel, MSG_ALL_NOTE_OFF );		
	}

	/*
	 * Omni Mode Off
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getOmniModeOff( int channel ) {
		return buildCmd( channel, MSG_OMNI_MODE_OFF );		
	}

	/*
	 * Omni Mode On
	 * @param int channel
	 * @return byte[] 
	 */ 
	public byte[] getOmniModeOn( int channel ) {
		return buildCmd( channel, MSG_OMNI_MODE_ON );		
	}

	/*
	 * Mono Mode On
	 * @param int channel
	 * @param int number
	 * @return byte[] 
	 */ 
	public byte[] getMonoModeOn( int channel, int number ) {
		return buildCmdMono( channel, number, MSG_MONO_MODE_ON );
	}

	/*
	 * Poly Mode On
	 * @param int channel
	 * @param int number
	 * @return byte[] 
	 */ 
	public byte[] getPolyModeOn( int channel, int number ) {
		return buildCmdMono( channel, number, MSG_POLY_MODE_ON );
	}

	/**
	 * buildCmd
	 * @param int channel
	 * @param byte[] bytes
	 * @return byte[]
	 */
	private byte[] buildCmd( int channel, byte[] bytes ) {
		bytes[ 0 ] = buildCmdChannel( channel, bytes[ 0 ] ) ;
		return bytes;		
	}

	/**
	 * buildCmdMono
	 * @param int channel
	 * @param int number
	 * @param byte[] bytes
	 * @return byte[]
	 */
	public byte[] buildCmdMono( int channel, int number, byte[] bytes ) {
		bytes[ 0 ] = buildCmdChannel( channel, bytes[ 0 ] ) ;
		bytes[ 2 ] = (byte)( number & 0x00ff ) ;				
		return bytes;
	}

	/**
	 * buildCmdChannel
	 * @param int channel
	 * @param byte b
	 * @return byte
	 */
	private byte buildCmdChannel( int channel, byte b ) {
		byte ret = (byte)(( b & 0xf0 ) | ( channel & 0x0f )) ;
		return ret;		
	}

}
