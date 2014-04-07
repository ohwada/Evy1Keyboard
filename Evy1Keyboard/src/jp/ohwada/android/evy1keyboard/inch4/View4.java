package jp.ohwada.android.evy1keyboard.inch4;

import jp.ohwada.android.evy1keyboard.R;
import jp.ohwada.android.evy1keyboard.common.KeyboardController;
import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * View4
 */	
public class View4 extends KeyboardController {

    /**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager 
     */
	public View4( Context context, UsbMidiManager manager ) {
		super( context, manager );
		TAG_SUB = "View4";
		log_d( "View4" );
	}

    /**
	 * stop
     */	
	public void stop() {
		sendAllChannelOff();	
	}
	
    /**
	 * initView
	 * @param View view 
     */	
	public void initView( View view ) {	
                
		initButtonKey( view, R.id.ButtonC );
		initButtonKey( view, R.id.ButtonCis );
		initButtonKey( view, R.id.ButtonD );
		initButtonKey( view, R.id.ButtonDis );
		initButtonKey( view, R.id.ButtonE );
		initButtonKey( view, R.id.ButtonF );
		initButtonKey( view, R.id.ButtonFis );
		initButtonKey( view, R.id.ButtonG );
		initButtonKey( view, R.id.ButtonGis );
		initButtonKey( view, R.id.ButtonA );
		initButtonKey( view, R.id.ButtonAis );	
		initButtonKey( view, R.id.ButtonB );
		initButtonKey( view, R.id.ButtonC2 );

		initVocal( view );
	}

	/**
	 * initButtonKey
	 * @param int id
	 */    
    private void initButtonKey( View view, int id ) {
		Button btn = (Button) view.findViewById( id );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				execTouch( view, event );
				return true;
			}
		});	
    }

	/**
	 * When a keyboard button is touched 
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouch( View view, MotionEvent event ) {
		int note = getNote( view );
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				execTouchDown( note ); 	
				break;
			case MotionEvent.ACTION_UP:
				execTouchUp( note ); 	
				break;
		}
	}

	/**
	 * execTouchDown
	 * @param int note
	 */
	private void execTouchDown( int note ) {
		if ( isVocal() ) {
			sendLylic();
			sendNoteOn( CH_VOCALOID, note ) ;
		}
		if ( isInstrument() ) {	
			sendNoteOn( CH_PIANO, note ) ;	    
		}
	}

	/**
	 * execTouchUp
	 * @param int note
	 */
	private void execTouchUp( int note  ) {
		if ( isInstrument() ) {
			sendNoteOff( CH_PIANO, note ) ;
		}
		if ( isVocal() ) {
			sendNoteOff( CH_VOCALOID, note ) ;		
		}		
	}

}
