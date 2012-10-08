package com.sebas.tian.evernexus.control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sebas.tian.evernexus.R;

/**
 * 
 * @author Sebastian
 *
 */
public class AboutActivity extends MainActivity {

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
	}
	
	
	/**
	 * Share this amazing app with your friends
	 */
	public void share(View v){
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		String mySubject = "Hi, Check out EverNexus an awesome app.!!";
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySubject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi!\nYou have to see this\n\nhttp://evernexus.herokuapp.com/");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
}
