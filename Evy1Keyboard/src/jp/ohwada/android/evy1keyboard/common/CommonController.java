package jp.ohwada.android.evy1keyboard.common;

import jp.ohwada.android.evy1keyboard.Constant;
import jp.ohwada.android.evy1keyboard.ToastMaster;
import jp.ohwada.android.midi.MidiChannelModeMessage;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;
import jp.ohwada.android.usbmidi.UsbMidiOutput;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * CommonController
 */	
public class CommonController {

	// debug
	protected String TAG_SUB = "CommonController";

	// CH
	protected static final int CH_VOCALOID = 0;
	protected static final int CH_PIANO = 1;
	protected static final int CH_PERCUSSION = MidiSoundConstants.CH_PERCUSSION;  // 9
			
	// MIDI
	private static final int MIDI_CABLE = 0;
	private static final int MAX_CHANNEL = 16;
	private static final int NOTE_VELOCITY = 127;
	
	// object
	private Context mContext;
	private UsbMidiManager mUsbMidiManager;

    /**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager
     */
	public CommonController( Context context, UsbMidiManager manager ) {
		log_d( "CommonController" );
		mContext = context;
		mUsbMidiManager = manager;
	}

    /**
	 * getContext
	 * @return Context
     */
	protected Context getContext() {
		return mContext;
	}

	/*
	 * sendNoteOn
	 * @param int ch
	 * @param int note 
	 */
	protected void sendNoteOn( int ch, int note ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		output.sendNoteOn( 
			MIDI_CABLE, ch, note, NOTE_VELOCITY );	
	}

	/*
	 * sendNoteOff
	 * @param int ch
	 * @param int note 
	 */	
	protected void sendNoteOff( int ch, int note ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		output.sendNoteOff( 
			MIDI_CABLE, ch, note, NOTE_VELOCITY );	
	}

	/*
	 * sendProgramChange
	 * @param int ch
	 * @param int num
	 */	
	protected void sendProgramChange( int ch, int num ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		output.sendProgramChange( MIDI_CABLE, ch, num );
	}

	/*
	 * sendSystemExclusive
	 * @param byte[] bytes
	 */	
	protected void sendSystemExclusive( byte[] bytes ) {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
		output.sendSystemExclusive( MIDI_CABLE, bytes );
	}

    /**
	 * sendAllChannelOff
	 */
    public void sendAllChannelOff() {
		UsbMidiOutput output = mUsbMidiManager.getUsbMidiOutput();
		if ( output == null ) return;
        MidiChannelModeMessage midi = new MidiChannelModeMessage();
        for ( int i=0; i<MAX_CHANNEL; i++ ) {
        	output.sendControlChange( MIDI_CABLE, midi.getResetAllControllers( i ) );
        	output.sendControlChange( MIDI_CABLE, midi.getAllNoteOff( i ) );
        	output.sendControlChange( MIDI_CABLE, midi.getAllSoundOff( i ) );
		}
	}
	
	/**
	 * getTag
	 * @param View view
	 * @param int min
	 * @param int max	 
	 * @return int
	 */  
	protected int getTag( View view, int min, int max ) {
		int tag = Integer.parseInt( (String) view.getTag() );
		if ( tag < min ) {
			tag = min;
		} else if ( tag > max ) {
			tag = max;
		}
		return tag;
	}

	/**
	 * toast_show
	 * @param String msg
	 */
	protected void toast_show( String msg ) {
		ToastMaster.makeText( mContext, msg, Toast.LENGTH_SHORT ).show();
	}
	
	/**
	 * logcat
	 * @param String msg
	 */
	protected void log_d( String msg ) {
		if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );	
	}	
}
