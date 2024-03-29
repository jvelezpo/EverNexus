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

import com.sebas.tian.evernexus.R;
import com.sebas.tian.evernexus.dao.UserDao;
import com.sebas.tian.evernexus.entity.User;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


/**
 * 
 * @author Sebastian
 * 
 */
public class LoginActivity extends MainActivity {

	private LoginBackground loginBackground;
	private ProgressDialog pleaseWaitDialog;
	private EditText username_et;
	private EditText password_ed;
	private CheckBox userId_chk;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		username_et = (EditText) findViewById(R.id.login_username);
		password_ed = (EditText) findViewById(R.id.login_password);
		userId_chk = (CheckBox) findViewById(R.id.login_user_id_chk);
		
		UserDao userDao = new UserDao(this);
		User user = userDao.getUser();
		if(user.getUsername() != null){
			username_et.setText(user.getUsername());
			userId_chk.setChecked(true);
		}
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
			startActivity(new Intent(LoginActivity.this, AboutActivity.class));
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
	 * After clicking on login buttom
	 * @param v
	 */
	public void login(View v) {
		loginBackground = new LoginBackground();
		loginBackground.execute(URL + "pages/login.json", username_et.getText()
				.toString(), password_ed.getText().toString());
	}

	/**
	 * Resets all the values to the defaults
	 */
	private void resetValues() {
		password_ed.setText("");
		username_et.requestFocus();
		pleaseWaitDialog.dismiss();
		toaster("Wrong user name or password");
	}
	
	/**
	 * Saves the username in the db if the chackbox is checked
	 */
	private void saveUsername(){
		UserDao userDao = new UserDao(this);
		if (userId_chk.isChecked()){
			User user = new User();
			user.setUsername(username_et.getText().toString());
			user.setEmail("");
			user.setPassword(password_ed.getText().toString());
			userDao.addUser(user);
		}else{
			userDao.deleteAllUser();
		}
	}

	/**
	 * Goes to the server and try to do login
	 * 
	 * @author Sebastian
	 * 
	 */
	private class LoginBackground extends AsyncTask<Object, String, Boolean> {
		private String respond = "";

		protected void onPreExecute() {
			pleaseWaitDialog = ProgressDialog.show(LoginActivity.this,
					"Attempting to login", "Be patient, Come on!", true, true);

			pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					LoginBackground.this.cancel(true);
				}
			});
		}

		protected void onCancelled() {
			pleaseWaitDialog.dismiss();
		}

		protected void onPostExecute(Boolean result) {
			try {
				if (!isCancelled()) {
					if (result) {
							JSONObject jsonObject = new JSONObject(respond);
							if (jsonObject.getBoolean("log")) {
								saveUsername();
								token = jsonObject.getString("token");
								startActivity(new Intent(LoginActivity.this, NoteViewActivity.class));
								LoginActivity.this.finish();
							} else {
								throw new Exception();
							}
					} else {
						resetValues();
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
			boolean result = getData(params[0].toString(),
					params[1].toString(), params[2].toString());
			return result;
		}

		public boolean getData(String path, String user, String pass) {
			boolean result = false;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(path);
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("user", user));
				nameValuePairs.add(new BasicNameValuePair("pass", pass));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				// Execute HTTP Get Request
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
