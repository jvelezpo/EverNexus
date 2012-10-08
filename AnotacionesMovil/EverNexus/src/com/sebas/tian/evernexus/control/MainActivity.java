package com.sebas.tian.evernexus.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	
	/**
	 * Share this amazing app with your friends
	 */
	protected void share(){
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		String mySubject = "Hi, Check out EverNexus an awesome app.!!";
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySubject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi!\nYou have to see this\n\nhttp://evernexus.herokuapp.com/");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
}
