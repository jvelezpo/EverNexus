package com.sebas.tian.evernexus;

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
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * 
 * @author Sebastian
 * 
 */
public class NoteViewActivity extends MainActivity {

	private NoteBackground noteBackground;
	private ProgressDialog pleaseWaitDialog;
	private TextView title;
	private TextView body;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_view_layout);

		title = (TextView) findViewById(R.id.note_view_title);
		body = (TextView) findViewById(R.id.note_view_text);

		noteBackground = new NoteBackground();
		noteBackground.execute(URL + "pages/note.json");
	}

	/**
	 * Show menu in this View
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.note_menu, menu);
		return true;
	}

	/**
	 * Set click listener to the menu
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.note_menu_about:
			startActivity(new Intent(NoteViewActivity.this, AboutActivity.class));
			break;
		case R.id.note_menu_edit:
			Intent i = new Intent(NoteViewActivity.this, NoteEditActivity.class);
			i.putExtra("title", title.getText().toString());
			i.putExtra("body", body.getText().toString());
			startActivity(i);
			NoteViewActivity.this.finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/**
	 * Resets all the values to the defaults
	 */
	private void resetValues() {
		pleaseWaitDialog.dismiss();
		toaster("Error getting data from the server");
	}
	
	/**
	 * Goes to the server and try to do login
	 * 
	 * @author Sebastian
	 * 
	 */
	private class NoteBackground extends AsyncTask<Object, String, Boolean> {
		private String respond = "";

		protected void onPreExecute() {
			pleaseWaitDialog = ProgressDialog
					.show(NoteViewActivity.this, "Downloading Your Last Note",
							"Be patient, Come on!", true, true);

			pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					NoteBackground.this.cancel(true);
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
							title.setText(jsonObject.getString("title"));
							body.setText(jsonObject.getString("body"));
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
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("token", token));
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
