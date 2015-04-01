package com.example.rkucentrallibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class Feedback extends ActionBarActivity {

	EditText et;
	Global global;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		global = ((Global) getApplicationContext());
		EditText et=(EditText) findViewById(R.id.editText1);

	}

	public void submitbtn(View v) {
		DownloadWebPageTask task = new DownloadWebPageTask();
		task.execute(new String[] { "http://www.vogella.com" });
		
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String result) {

			showAlert(result);
		}

		@Override
		protected String doInBackground(String... arg0) {

			Mail m = new Mail("central.library.rku@gmail.com", "Library@105");

			// String[] toArr = {"kamalmpatel@gmail.com"};

			String[] toArr = global.getEmails();

			m.setTo(toArr);
//			m.setFrom("central.library.rku@gmail.com");
			m.setFrom("dhavalankola@gmail.com@gmail.com");
			m.setSubject("Feedback from RKU Central Library App.");

			String message = "Hello Sir, I am ("
					+ global.getMember()
					+ ") and this is my feedback"
					+ "\n\n"
					+ et.getText()
					+ "Thank you";

			// m.setBody("Email body. Test Email.");

			m.setBody(message.toString());

			try {
				// m.addAttachment("/sdcard/filelocation");

				if (m.send()) {
					return "Feedback received successfully.";
				} else {
					return "Feedback not sent.\n Try again";
				}
			} catch (Exception e) {
				// Toast.makeText(MailApp.this,
				// "There was a problem sending the email.",
				// Toast.LENGTH_LONG).show();

				return e.toString();
			}
			// return null;

		}
	}

	public void showAlert(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(msg);
		alertDialog.show();
	}
}
