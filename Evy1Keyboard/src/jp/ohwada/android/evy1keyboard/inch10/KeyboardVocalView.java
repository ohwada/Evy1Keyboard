package jp.ohwada.android.evy1keyboard.inch10;

import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * KeyboardVocalView
 */	
public class KeyboardVocalView extends KeyboardView{

    /**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager
	 * @param int num
	 * @param int inst	 
     */
	public KeyboardVocalView( Context context, UsbMidiManager manager, int num, int inst ) {
		super( context, manager, num, inst );
		TAG_SUB = "KeyboardVocalView";
		log_d( "KeyboardVocalView" );
	}

    /**
	 * initView
	 * @param View view 
     */	
	public void initView( View view ) {
		super.initView( view );	
		initVocal( view );
	}

	/**
	 * When a keyboard button is touched
	 * @param View view
	 * @param MotionEvent event
	 */
	protected void execTouch( View view, MotionEvent event ) {
		if ( isInstrument() ) {
			execTouchKey( view, event );
		}
		if ( isVocal() ) {
			int note = getNote( view );
			if ( note == NOTE_OUTSIDE ) return;
			switch ( event.getAction() ) {
				case MotionEvent.ACTION_DOWN:
					sendLylic();
					sendNoteOn( CH_VOCALOID, note );
					break;
				case MotionEvent.ACTION_UP:
					sendNoteOff( CH_VOCALOID, note );		
					break;
			}
		}	
	}

}
