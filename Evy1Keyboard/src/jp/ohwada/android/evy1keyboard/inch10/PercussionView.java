package jp.ohwada.android.evy1keyboard.inch10;

import jp.ohwada.android.evy1keyboard.R;
import jp.ohwada.android.evy1keyboard.common.CommonController;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * PercussionView
 */	
public class PercussionView extends CommonController {

	// note
	private static final int PERC_KEY_MIN = MidiSoundConstants.PERC_KEY_MIN;
	private static final int PERC_KEY_MAX = MidiSoundConstants.PERC_KEY_MAX;

	// tag
	private static final int TAG_MIN = 0;
	private static final int TAG_MAX = 12;
	private static final int BUTTON_NUM = TAG_MAX + 1;	
	
	// UI
	private Button mButtonPerct;
	private Button[] mButtons = new Button[ BUTTON_NUM ];
	
	// variable
	private int[] mKeys = new int[ BUTTON_NUM ];	

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager
	 */ 
	public PercussionView( Context context, UsbMidiManager manager  ) {
		super( context, manager );
		TAG_SUB = "PercussionView";
		log_d( "PercussionView" );
	}

	/**
	 * initView
	 * @param View view
	 */ 
	public void initView( View view ) {	
		initButton( view, 0, R.id.Button_perc0 );
		initButton( view, 1, R.id.Button_perc1 );
		initButton( view, 2, R.id.Button_perc2 );
		initButton( view, 3, R.id.Button_perc3 );
		initButton( view, 4, R.id.Button_perc4 );
		initButton( view, 5, R.id.Button_perc5 );
		initButton( view, 6, R.id.Button_perc6 );
		initButton( view, 7, R.id.Button_perc7 );

		mButtonPerct = (Button) view.findViewById( R.id.Button_perc_select );
		mButtonPerct.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickPerc( view );
			}
		});	
	}

	/**
	 * initButton
	 * @param View view
	 * @param int num
	 * @param int id
	 */    
    private void initButton( View view, int num, int id ) {
    	int key = MidiSoundConstants.PERC_ACOUSTIC_BASS_DRUM + 5 * num;
		String name = MidiSoundConstants.getPercussionName( key );
		Button btn = (Button) view.findViewById( id );
		btn.setText( name );
		btn.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				execTouch( view, event );
				return true;
			}
		});		
		mButtons[ num ] = btn;	
		mKeys[ num ] = key;
    }
 
 	/**
	 * When a percussion selection button is clicked
	 * @param View view
	 */ 
	private void execClickPerc( View view ) {
		// show selection dialog
		PercussionDialog dialog = new PercussionDialog( getContext() );
		dialog.initView( mKeys );
		dialog.setOnChangedListener( 
			new PercussionDialog.OnChangedListener() {
			@Override
			public void onClicked( int parent, int key ) {
				execClickChild( parent, key );		
			}
		});
		dialog.show();
	}

	/**
	 * When a percussion is selected
	 * @param int num
	 */ 
	private void execClickChild( int parent, int key ) {
		String name = MidiSoundConstants.getPercussionName( key );
		mButtons[ parent ].setText( name );
		mKeys[ parent ] = key; 
	}
				
	/**
	 * When a percussion button is touched
	 * @param View view
	 * @param MotionEvent event
	 */
	private void execTouch( View view, MotionEvent event ) {
		int note = getNote( view );
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				sendNoteOn( CH_PERCUSSION, note ) ;
				break;
			case MotionEvent.ACTION_UP:
				sendNoteOff( CH_PERCUSSION, note ) ;
				break;
		}
	}
		
	/**
	 * getNote
	 * @param View view
	 * @return int
	 */  
	private int getNote( View view ) {
		int tag = getTag( view, TAG_MIN, TAG_MAX );
		int note = mKeys[ tag ];
		if ( note < PERC_KEY_MIN ) {
			note = PERC_KEY_MIN;
		} else if ( note > PERC_KEY_MAX ) {
			note = PERC_KEY_MAX;
		}
		return note;
	}	
}
