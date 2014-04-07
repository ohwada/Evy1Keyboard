package jp.ohwada.android.evy1keyboard.inch10;

import jp.ohwada.android.evy1keyboard.Constant;
import jp.ohwada.android.evy1keyboard.R;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * View10
 */	
public class View10 {

	// debug
	private String TAG_SUB = "View10";
	
	// UI 
    private KeyboardVocalView mKeyboardView1;
    private KeyboardView mKeyboardView2;
    private PercussionView mPercussionView;

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager
	 */	
	public View10( Context context, UsbMidiManager manager ) {
		mKeyboardView1 = new KeyboardVocalView( 
			context, manager, 1, MidiSoundConstants.INST_ACOUSTIC_PIANO );
		mKeyboardView2 = new KeyboardView( 
			context, manager, 2, MidiSoundConstants.INST_STRING_ENSEMBLE_1 );
		mPercussionView = new PercussionView( context, manager );
	}

    /**
	 * stop
     */	
	public void stop() {
		mKeyboardView1.sendAllChannelOff();	
	}

	/**
	 * initView
	 * @param View view
	 */	
	public void initView( View view ) {
		log_d( "initView" );
		Button btnPanic = (Button) view.findViewById( R.id.Button_panic_2 );
		btnPanic.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				stop();
			}
		});	

		mKeyboardView1.initView( view );
		mKeyboardView2.initView( view );
		mPercussionView.initView( view ); 
	}

    /**
	 * setLylic
	 * @param String str 
     */	
	public void setLylic( String str ) {
		mKeyboardView1.setLylic( str );
	}

	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );	
	}

}
