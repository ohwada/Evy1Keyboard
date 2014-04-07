package jp.ohwada.android.evy1keyboard.common;

import jp.ohwada.android.evy1.Evy1Phonetic;
import jp.ohwada.android.evy1keyboard.R;
import jp.ohwada.android.midi.MidiMsgConstants;
import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * KeyboardController
 */	
public class KeyboardController extends CommonController {

	// note
	protected static final int NOTE_MIN = MidiMsgConstants.NOTE_MIN;
	protected static final int NOTE_MAX = MidiMsgConstants.NOTE_MAX;
	protected static final int NOTE_OUTSIDE = -1;

	// octave	
	protected static final int OCTAVE = 12;
	protected static final int OCTAVE_MIN = 0;
	protected static final int OCTAVE_MAX = 10;
	protected static final int OCTAVE_DEFAULT = 5;
	
	// tag
	protected static final int TAG_MIN = 0;
	protected static final int TAG_MAX = 12;

	// object
	private Evy1Phonetic mPhonetic;

	// UI 
	private EditText mEditTextInput;
	private TextView mTextViewLylic;
		
	// variable
	private int mOctave = OCTAVE_DEFAULT;
	private boolean isInstrument = true;
	private boolean isVocal = false;

	// Lyic
	private String[] mLyicStrings = null;
	private int mLyicPointer = 0;

    /**
	 * === Constructor ===
	 * @param Context context
	 * @param UsbMidiManager manager
     */
	public KeyboardController( Context context, UsbMidiManager manager ) {
		super( context, manager );
		TAG_SUB = "KeyboardController";
		log_d( "KeyboardController" );
		mPhonetic = new Evy1Phonetic();
	}

    /**
	 * initVocal
	 * @param View view 
     */	
	protected void initVocal( View view ) {

		mTextViewLylic = (TextView) view.findViewById( R.id.TextView_lylic );
		// It is made for a focus, in order to remove a focus from EditView
		mTextViewLylic.setFocusable( true );
        mTextViewLylic.setFocusableInTouchMode( true );  
        mTextViewLylic.requestFocus();

		mEditTextInput = (EditText) view.findViewById( R.id.EditText_input );
		
		Button btn_set = (Button) view.findViewById( R.id.Button_set );
		btn_set.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				execClickSet( view );
			}
		});	

		CheckBox cb_instrument = (CheckBox) view.findViewById( R.id.CheckBox_instrument );
		cb_instrument.setChecked( true );
		cb_instrument.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isInstrument = checkBox.isChecked();
            }
        });

		CheckBox cb_vocal = (CheckBox) view.findViewById( R.id.CheckBox_vocal );
		cb_vocal.setChecked( false );
		cb_vocal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CheckBox checkBox = (CheckBox) view;
                isVocal = checkBox.isChecked();
            }
        });

	}

    /**
	 * setLylic
	 * @param String str 
     */	
	public void setLylic( String str ) {
		mEditTextInput.setText( str );
	}

    /**
	 * setOctave
	 * @param int val
     */	
	protected void setOctave( int val ) {
		if ( val < OCTAVE_MIN ) {
			val = OCTAVE_MIN;
		} else if ( val > OCTAVE_MAX ) {
			val = OCTAVE_MAX;
		}
		mOctave = val;
	}

    /**
	 * isInstrument
	 * @return boolean
     */	
	protected boolean isInstrument() {
		return isInstrument;
	}

    /**
	 * isVocal
	 * @return boolean
     */	
	protected boolean isVocal() {
		return isVocal;
	}

	/**
	 * When lylic set button is clicked
	 * @param View view
	 */
	private void execClickSet( View view ) {
		// focus on TextView, in order to remove a focus from EditView
		mTextViewLylic.requestFocus();
		String str = mEditTextInput.getText().toString();
		if ( str.length() == 0 ) return;
		String[] strings = mPhonetic.splitText( str );
		if ( mPhonetic.checkLylics( strings ) ) {
			mLyicStrings = strings;
			mLyicPointer = 0;
			toast_show( str );	
		} else {
			String msg = mPhonetic.getInaccurateLylics();
			toast_show( "Inaccurate : " + msg );			
		}
	}

	/**
	 * sendLylic
	 */
	protected void sendLylic() {
		String lylic = getNextLylic();
		// If there are lylic words
		if (( lylic != null )&&( lylic.length() > 0 )) {
			mTextViewLylic.setText( lylic );
			String pa = mPhonetic.getAlphabet( lylic );
			// set up, if there is Phonetic Alphabet
			if ( pa != null ) {
				byte[] bytes = mPhonetic.getSymbols( pa );
				sendSystemExclusive( bytes );
			}
		}
	}

	/**
	 * getNextLylic
	 * @return String
	 */  
	private String getNextLylic() {
		if ( mLyicStrings == null ) return null;
		String str = mLyicStrings[ mLyicPointer ];
		mLyicPointer ++;
		if ( mLyicPointer >= mLyicStrings.length ) {
			mLyicPointer = 0;
		}
		return str;
	}
		
	/**
	 * getNote
	 * @param View view
	 * @return int
	 */  
	protected int getNote( View view ) {
		int tag = getTag( view, TAG_MIN, TAG_MAX );
		int note = OCTAVE * mOctave + tag; 
		// not sounded, if it is outside the range. 
		if ( note < NOTE_MIN ) {
			note = NOTE_OUTSIDE;
		} else if ( note > NOTE_MAX ) {
			note = NOTE_OUTSIDE;
		}
		return note;
	}	

}
