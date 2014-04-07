package jp.ohwada.android.usbmidi;

import java.util.List;

import jp.ohwada.android.usb.UsbBaseManager;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;

/**
 * UsbMidiManager
 */	
public class UsbMidiManager extends UsbBaseManager {

	// interface parameter
	private static final int SUBCLASS_MIDI_STREAMING = 3;	
	private static final int[] TYPES = new int[]{ 
		UsbConstants.USB_ENDPOINT_XFER_INT, 
		UsbConstants.USB_ENDPOINT_XFER_BULK };

	// status
	private static final int DEVICE_NULL = 0;
	private static final int DEVICE_INTERFACE_UNMATCH = 1;
	private static final int DEVICE_ENDPOINT_UNMATCH = 2;
	private static final int DEVICE_OPEN_FAILED = 3;
	private static final int DEVICE_OPEN_SUCCESS = 4;
	private static final int DEVICE_OPEN_ALREDY = 5;
			
	// variable
	private UsbDevice mDevice;
	private UsbMidiOutput mOutput;

    /**
	 * constructor
	 * @param Context context
     */	    
	public UsbMidiManager( Context context ) {
		super( context );
		TAG_SUB = "UsbMidiManager";
	}

	/**
	 * get MIDI output device
	 * @return UsbMidiOutput
	 */
	public UsbMidiOutput getUsbMidiOutput() {
		return mOutput;
	}

    /**
	 * open
     */	
	public void open() {
		registerAttached();
		registerDetached();
		registerPermission();
	}

    /**
	 * close
     */	
	public void close() {
		unregister();
		closeDevice();
	}

	/**
	 * findDevice
	 */	
	public void findDevice() {
		List<UsbDevice> list = getDeviceList();
		for ( int i = 0; i < list.size(); i++ ) {
			UsbDevice device = list.get( i );
			int ret = findConnection( device );
   			switch ( ret ) {
				case DEVICE_OPEN_FAILED:
					requestPermission( device );
					return;
				case DEVICE_OPEN_SUCCESS:
					notifyAttached( device );
					return;
	   		}
		}
	}
	   		
	/**
	 * findConnection
	 * @param UsbDevice device
	 * @return int 
	 */ 
	 protected int findConnection( UsbDevice device ) {
		log_d( "findConnection " + device );
		if ( device == null ) return DEVICE_NULL;
		if ( mOutput != null ) return DEVICE_OPEN_ALREDY;
	   	List<UsbInterface> list_intf = findInterface( 
	   		device, 
	   		UsbConstants.USB_CLASS_AUDIO, 
	   		SUBCLASS_MIDI_STREAMING );
	   	if ( list_intf.size() == 0 ) {
            log_d( "could not find interface" );
	   		return DEVICE_INTERFACE_UNMATCH;
	   	}
	   	UsbInterface usbInterface = list_intf.get(0);
	   	List<UsbEndpoint> list_ep = findEndpoint( 
	   		usbInterface, 
	   		UsbConstants.USB_DIR_OUT, 
	   		TYPES );
	   	if ( list_ep.size() == 0 ) {
			log_d( "could not find endpoint" );
	   		return DEVICE_ENDPOINT_UNMATCH;
	   	}
	   	UsbEndpoint endpoint = list_ep.get(0);
	   	UsbDeviceConnection connection = openDevice( device );
        if ( connection == null ) {
            log_d( "could not open device" );
        	return DEVICE_OPEN_FAILED;
        }
        mDevice = device ;
		mOutput = new UsbMidiOutput( usbInterface, endpoint, connection );
        return DEVICE_OPEN_SUCCESS;
	}

	/**
	 * closeDevice
	 */
	private void closeDevice() {
		if (mOutput != null) {
			mOutput.close();
			mOutput = null;
		}
		mDevice = null;
	}

	/**
	 * receiveAttached
	 * @param UsbDevice device
	 */
   	protected void	receiveAttached( UsbDevice device ) {
   		log_d( "receiveAttached" );
   		int ret = findConnection( device );
   		switch ( ret ) {
			case DEVICE_OPEN_FAILED:
				requestPermission( device );
				break;
			case DEVICE_OPEN_SUCCESS:
				notifyAttached( device );
				break;
	   	}
   	}

	/**
	 * receiveDetached
	 * @param UsbDevice device
	 */
   	protected void receiveDetached( UsbDevice device ) {
	   	log_d( "receiveDetached" );
		if ( !matchDevice( device, mDevice ) ) return;
		closeDevice();
		notifyDetached( device );
   	}

	/**
	 * receivePermission
	 * @param UsbDevice device
	 */ 
	protected void	receivePermission( UsbDevice device ) {
	   	log_d( "receivePermission" );
   		clearRequestPermission();
   		int ret = findConnection( device );
   		switch ( ret ) {
			case DEVICE_OPEN_SUCCESS:
				notifyAttached( device );
				break;
	   	}
   	}
	   	
}
