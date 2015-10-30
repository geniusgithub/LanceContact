package com.geniusgithub.contact.dialer.util;


import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.provider.Settings;

import com.geniusgithub.contact.dialer.DialpadView.KeyDigit;
import com.geniusgithub.contact.util.CommonLog;
import com.geniusgithub.contact.util.LogFactory;

public class KeyboardTone {

	private static final CommonLog log = LogFactory.createLog(KeyboardTone.class.getSimpleName());
	
    /** The DTMF tone volume relative to other sounds in the stream */
    private static final int TONE_RELATIVE_VOLUME = 80;
    /** Stream type used to play the DTMF tones off call, and mapped to the volume control keys */
    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_DTMF;
    /** The length of DTMF tones in milliseconds */
    private static final int TONE_LENGTH_MS = 150;
    // determines if we want to playback local DTMF tones.
    private boolean mDTMFToneEnabled = false;
    
    private Context mContext;
    private ToneGenerator mToneGenerator;
    private final Object mToneGeneratorLock = new Object();
    
	private static KeyboardTone mInstance;
 
    
	public static synchronized KeyboardTone getInstance(Context context) {
		if (mInstance == null){
			mInstance  = new KeyboardTone(context);
		
		}
		return mInstance;
	}

    private KeyboardTone(Context context){
    	mContext = context;
    }
    
    public void init(){
		 synchronized (mToneGeneratorLock) {
	         if (mToneGenerator == null) {
	             try {
	                 mToneGenerator = new ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME);
	             } catch (RuntimeException e) {
	                 log.e("Exception caught while creating local tone generator: " + e);
	                 mToneGenerator = null;
	             }
	         }
	     }
    }
    
    public void unInit(){
	   synchronized (mToneGeneratorLock) {
           if (mToneGenerator != null) {
               mToneGenerator.release();
               mToneGenerator = null;
           }
       }
    }
    
    public void checkSystemSetting() {
    	mDTMFToneEnabled =  Settings.System.getInt(mContext.getContentResolver(),
    	                Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
    }
    
    public void playTone(KeyDigit keyDigit) {
    	switch(keyDigit){
	 	case KEY_ONE:
	 		playTone(ToneGenerator.TONE_DTMF_1);
	     	break;
	     case KEY_TWO:
	    	 playTone(ToneGenerator.TONE_DTMF_2);
	     	break;
	     case KEY_THREE:
	    	 playTone(ToneGenerator.TONE_DTMF_3);
	     	break;
	     case KEY_FOUR:
	    	 playTone(ToneGenerator.TONE_DTMF_4);
	     	break;
	     case KEY_FIVE:
	    	 playTone(ToneGenerator.TONE_DTMF_5);
	     	break;
	     case KEY_SIX:
	    	 playTone(ToneGenerator.TONE_DTMF_6);
	     	break;
	     case KEY_SEVEN:
	    	 playTone(ToneGenerator.TONE_DTMF_7);
	     	break;
	     case KEY_EIGHT:
	    	 playTone(ToneGenerator.TONE_DTMF_8);
	     	break;
	     case KEY_NIGHT:
	    	 playTone(ToneGenerator.TONE_DTMF_9);
	     	break;
	     case KEY_START:
	    	 playTone(ToneGenerator.TONE_DTMF_S);
	    	 break;
	     case KEY_ZERO:
	         playTone(ToneGenerator.TONE_DTMF_0);	 
	    	 break;
	     case KEY_POUND:
	    	 playTone(ToneGenerator.TONE_DTMF_P);
	    	 break;

	     default:
	    	 break;
	}
    }
    /**
     * Plays the specified tone for TONE_LENGTH_MS milliseconds.
     */
    public void playTone(int tone) {
        playTone(tone, TONE_LENGTH_MS);
    }
    
    
    /**
     * Play the specified tone for the specified milliseconds
     *
     * The tone is played locally, using the audio stream for phone calls.
     * Tones are played only if the "Audible touch tones" user preference
     * is checked, and are NOT played if the device is in silent mode.
     *
     * The tone length can be -1, meaning "keep playing the tone." If the caller does so, it should
     * call stopTone() afterward.
     *
     * @param tone a tone code from {@link ToneGenerator}
     * @param durationMs tone length.
     */
    private void playTone(int tone, int durationMs) {
        // if local tone playback is disabled, just return.
        if (!mDTMFToneEnabled) {
            return;
        }

        // Also do nothing if the phone is in silent mode.
        // We need to re-check the ringer mode for *every* playTone()
        // call, rather than keeping a local flag that's updated in
        // onResume(), since it's possible to toggle silent mode without
        // leaving the current activity (via the ENDCALL-longpress menu.)
        AudioManager audioManager =
                (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if ((ringerMode == AudioManager.RINGER_MODE_SILENT)
            || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                log.e("playTone: mToneGenerator == null, tone: " + tone);
                return;
            }

            // Start the new tone (will stop any playing tone)
            mToneGenerator.startTone(tone, durationMs);
            log.e("startTone tone = " + tone);
        }
    }
    
    /**
     * Stop the tone if it is played.
     */
    private void stopTone() {
        // if local tone playback is disabled, just return.
        if (!mDTMFToneEnabled) {
            return;
        }
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                log.e("stopTone: mToneGenerator == null");
                return;
            }
            mToneGenerator.stopTone();
        }
    }
}
