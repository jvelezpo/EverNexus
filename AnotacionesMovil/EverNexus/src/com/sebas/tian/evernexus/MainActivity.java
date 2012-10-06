package com.sebas.tian.evernexus;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Sebastian
 *
 */
public class MainActivity extends Activity {
	public static final String URL = "http://evernexus.herokuapp.com/";
	
	public static String token = "";
	
	protected void toaster(String msg) {
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
