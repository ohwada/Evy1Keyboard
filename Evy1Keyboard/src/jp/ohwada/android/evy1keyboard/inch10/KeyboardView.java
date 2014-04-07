package jp.ohwada.android.evy1keyboard.inch10;

import jp.ohwada.android.evy1keyboard.common.KeyboardController;
import jp.ohwada.android.midi.MidiSoundConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * KeyboardView
 */	
public class KeyboardView extends KeyboardController {
				
	// object
	private Resources mResources;
	
	// UI 
	private Button	mButtonInstrument;

	// variable
	private String mPackageName = "";
	private int mViewNum = 0;
	private int mChannel = 0;
	private int mInstrumentNum = 0;
	private int mPrevInstrumentNum =  -1;

    /**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager
	 * @param int num
	 * @param int inst	 
     */
	public KeyboardView( Context context, UsbMidiManager manager, int num, int inst ) {
		super( context, manager );
		TAG_SUB = "KeyboardView";
		log_d( "KeyboardView" );
		mViewNum = num;
		mChannel = num;
		mInstrumentNum = inst;
		mResources = context.getResources();
		mPackageName = context.getPackageName();
	}

    /**
	 * initView
	 * @param View view 
     */	
	public void initView( View view ) {	
		
		initImageView( view, "upper_c" );
		initImageView( view, "upper_cis" );
		initImageView( view, "upper_d" );
		initImageView( view, "upper_dis" );
		initImageView( view, "upper_e" );
		initImageView( view, "upper_f" );
		initImageView( view, "upper_fis" );
		initImageView( view, "upper_g" );
		initImageView( view, "upper_gis" );
		initImageView( view, "upper_a" );
		initImageView( view, "upper_ais" );	
		initImageView( view, "upper_b" );
		initImageView( view, "upper_c2" );

		initImageView( view, "lower_c" );
		initImageView( view, "lower_d" );
		initImageView( view, "lower_e" );
		initImageView( view, "lower_f" );
		initImageView( view, "lower_g" );
		initImageView( view, "lower_a" );
		initImageView( view, "lower_b" );
		initImageView( view, "lower_c2" );

		mButtonInstrument = (Button) view.findViewById( 
			getIdentifier( "Button_instrument_" + mViewNum ) );
		mButtonInstrument.setText( getInstrumentName( mInstrumentNum ) );
		mButtonInstrument.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickInstrument( view );
			}
		});	
		
		Spinner spOctave = (Spinner) view.findViewById( 
			getIdentifier( "Spinner_octave_" + mViewNum ) );
		spOctave.setSelection( OCTAVE_DEFAULT );
        spOctave.setOnItemSelectedListener( 
        	new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( 
            	AdapterView<?> parent, View view, int position, long id ) {
            	setOctave( position );	
            }
            @Override
            public void onNothingSelected( AdapterView<?> arg0 ) {
            	// dummy
            }
        });
	}

	/**
	 * initImageView
	 * @param String name 
	 */    
    private void initImageView( View view, String name ) {
    	int id = getIdentifier( "ImageView_" + mViewNum + "_" + name );
		ImageView iv = (ImageView) view.findViewById( id );
		iv.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch( View view, MotionEvent event ) {
				execTouch( view, event );
				return true;
			}
		});	
    }

    /**
	 * getIdentifier
	 * @param String name
	 * @return int
     */	
	private int getIdentifier( String name ) {
		return mResources.getIdentifier( name, "id", mPackageName );	
	}
	
    /**
	 * getInstrumentName
	 * @param int num
	 * @return String
     */
	private String getInstrumentName( int num ) {
		if ( num < MidiSoundConstants.INST_KEY_MIN ) return "";
		if ( num > MidiSoundConstants.INST_KEY_MAX ) return "";
		return MidiSoundConstants.INSTRUMENT_NAMES[ num ];
	}

	/**
	 * When a musical instrument selection button is clicked
	 * @param View view
	 */ 
	private void execClickInstrument( View view ) {
		// show selection dialog
		InstrumentDialog dialog = new InstrumentDialog( getContext() );
		dialog.initExpandableListView( mInstrumentNum );
		dialog.setOnChangedListener( 
			new InstrumentDialog.OnChangedListener() {
			@Override
			public void onClicked( int num ) {
				execClickChild( num );		
			}
		});
		dialog.show();
	}

	/**
	 * When a musical instrument is selected
	 * @param int num
	 */ 
	private void execClickChild( int num ) {
		mInstrumentNum = num;
		String name = getInstrumentName( num );
		mButtonInstrument.setText( name );
		toast_show( name );	
	}

	/**
	 * When a keyboard button is touched
	 * @param View view
	 * @param MotionEvent event
	 */
	protected void execTouch( View view, MotionEvent event ) {
		execTouchKey( view, event );
	}

	/**
	 * execTouchKey
	 * @param View view
	 * @param MotionEvent event
	 */
	protected void execTouchKey( View view, MotionEvent event ) {	
		// send to MIDI device, when a musical instrument is changed
		if ( mInstrumentNum !=  mPrevInstrumentNum ) {
			mPrevInstrumentNum = mInstrumentNum;
			sendProgramChange( mChannel, mInstrumentNum );
		}
		// send note
		int note = getNote( view );
		if ( note == NOTE_OUTSIDE ) return;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				sendNoteOn( mChannel, note ) ;
				break;
			case MotionEvent.ACTION_UP:
				sendNoteOff( mChannel, note ) ;
				break;
		}
	}

}
