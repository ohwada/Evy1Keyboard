package jp.ohwada.android.evy1keyboard;

import jp.ohwada.android.evy1keyboard.inch10.View10;
import jp.ohwada.android.evy1keyboard.inch4.View4;
import jp.ohwada.android.usbmidi.UsbMidiManager;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity
 */	
public class MainActivity extends Activity {

	// debug
	private String TAG_SUB = "MainActivity";
						
	// object
	private UsbMidiManager mUsbMidiManager;
	
	// UI 
    private TextView mTextViewTitle;
    private View4 mView4;
    private View10 mView10;

	/**
	 * === onCreate ===
	 */	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		/* Customizing the title bar */
        requestWindowFeature( Window.FEATURE_CUSTOM_TITLE );
		/* set the layout on the screen */
		View view = getLayoutInflater().inflate( R.layout.activity_main, null ); 
        setContentView( view );
		/* set the layout on the title bar */
        getWindow().setFeatureInt( 
        	Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar );
 
         // Set up the custom title
        TextView tvTitleLeft = (TextView) findViewById( R.id.TextView_title_left ) ;
        tvTitleLeft.setText( R.string.app_name );   
        mTextViewTitle = (TextView) findViewById( R.id.TextView_title_right );

		mUsbMidiManager = new UsbMidiManager( this );
		mUsbMidiManager.setOnChangedListener( new UsbMidiManager.OnChangedListener() {
			@Override
			public void onAttached( UsbDevice device ) {
				execAttached( device );
			}
			@Override
        	public void onDetached( UsbDevice device ) {
        		execDetached( device );
        	}
		});

		String lylic = getResources().getString( R.string.lylic_jp );
		if ( is10inch() ) {
			mView10 = new View10( this, mUsbMidiManager );
			mView10.initView( view );
			mView10.setLylic( lylic );	
		} else {
			mView4 = new View4( this, mUsbMidiManager );
			mView4.initView( view );
			mView4.setLylic( lylic );	
		}
	}
    
	/**
	 * === onResume ===
	 */
    @Override
    public void onResume() {
		super.onResume();
		log_d( "onResume" );
		showNotConnect();
		mUsbMidiManager.open();
		mUsbMidiManager.findDevice();
    }

	/**
	 * === onDestroy ===
	 */
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if ( mView4 != null ) {
    		mView4.stop();
    	}
    	if ( mView10 != null ) {
    		mView10.stop();
    	}
    	mUsbMidiManager.close();
    }
	
	/**
	 * execAttached
	 * @param UsbDevice device
	 */	  
	private void execAttached( UsbDevice device ) {
		String name = device.getDeviceName();
		showConnected( name );
		toast_show( "Attached : " + name );
	}

	/**
	 * execDetached
	 * @param UsbDevice device
	 */	 
	private void execDetached( UsbDevice device ) {
		showNotConnect();
		toast_show( "Detached : " + device.getDeviceName() );  
	}

	/**
	 * showNotConnect
	 */	 
	private void showNotConnect() {
		mTextViewTitle.setText( R.string.not_connect );
	}

	/**
	 * showNotConnect
	 */	 
	private void showConnected( String name ) {
		mTextViewTitle.setText( "Connected : " + name );
	}

	/**
	 *  is10inch
	 * @return 
	 */	 
	private boolean is10inch() {
		Configuration configuration = getResources().getConfiguration();
		int width = configuration.smallestScreenWidthDp;
		if ( width >= 720 ) return true;
		return false;
	}

	/**
	 * toast_show
	 * @param String msg
	 */
	private void toast_show( String msg ) {
		ToastMaster.makeText( this, msg, Toast.LENGTH_SHORT ).show();
	}

	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (Constant.D) Log.d( Constant.TAG, TAG_SUB + " " + msg );	
	}		
}
