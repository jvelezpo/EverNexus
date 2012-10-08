package com.sebas.tian.evernexus.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.sebas.tian.evernexus.R;

/**
 * 
 * @author Sebastian
 * 
 */
public class NoteEditActivity extends MainActivity {
	private SendNoteBackground sendNoteBackground;
	private ProgressDialog pleaseWaitDialog;
	private EditText title;
	private EditText body;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit_layout);

		title = (EditText) findViewById(R.id.note_edit_title);
		body = (EditText) findViewById(R.id.note_edit_text);
		
		Bundle extras = getIntent().getExtras();
		title.setText(extras.getString("title"));
		body.setText(extras.getString("body"));
		setTitle("Editing " + extras.getString("title") + " s Note");
	}

	/**
	 * Show menu in this View
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.simple_menu, menu);
		return true;
	}

	/**
	 * Set click listener to the menu
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.simple_menu_about:
			startActivity(new Intent(NoteEditActivity.this, AboutActivity.class));
			break;
		case R.id.simple_menu_share:
			share();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	/**
	 * After the note has been edited it is send to the server 
	 */
	public void sendNote(View v){
		sendNoteBackground = new SendNoteBackground();
		sendNoteBackground.execute(URL + "pages/update_note.json");
	}

	/**
	 * Resets all the values to the defaults
	 */
	private void resetValues() {
		pleaseWaitDialog.dismiss();
		toaster("Error sending data to the server");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			toaster("The note was not saved!");
			startActivity(new Intent(NoteEditActivity.this, NoteViewActivity.class));
			NoteEditActivity.this.finish();
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * Goes to the server and try to do login
	 * 
	 * @author Sebastian
	 * 
	 */
	private class SendNoteBackground extends AsyncTask<Object, String, Boolean> {
		private String respond = "";

		protected void onPreExecute() {
			pleaseWaitDialog = ProgressDialog.show(NoteEditActivity.this, "Sending New Note", "Be patient, Come on!", true, true);

			pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					SendNoteBackground.this.cancel(true);
				}
			});
		}

		protected void onCancelled() {
			pleaseWaitDialog.dismiss();
		}

		protected void onPostExecute(Boolean result) {
			System.out.println("EL RESPOND " + respond);
			try {
				if (!isCancelled()) {
					if (result) {
						JSONObject jsonObject = new JSONObject(respond);
						if (jsonObject.getBoolean("log")) {
							startActivity(new Intent(NoteEditActivity.this, NoteViewActivity.class));
							NoteEditActivity.this.finish();
						} else {
							throw new Exception();
						} 
					} else {
						System.out.println("No se pudo en el post execute");
					}
					pleaseWaitDialog.dismiss();
				} else {
					resetValues();
				}
			} catch (Exception e) {
				resetValues();
			}
		}

		protected Boolean doInBackground(Object... params) {
			boolean result = postData(params[0].toString());
			return result;
		}

		public boolean postData(String path) {
			boolean result = false;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(path);
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("token", token));
				nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("body", body.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					respond += line + "\n";
				}
				result = true;
			} catch (ClientProtocolException e) {
				System.out.println("ERROR ClientProtocolException");
				result = false;
			} catch (IOException e) {
				System.out.println("ERROR IOException");
				result = false;
			} catch (Exception e) {
				System.out.println("GENERAL ERROR");
				result = false;
			}
			return result;
		}

	}
	
	
}
