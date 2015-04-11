package com.example.rkucentrallibrary;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

	private static final String SOAP_ACTION = "http://tempuri.org/LoginTest";
	private static final String OPERATION_NAME = "LoginTest";
	private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	private static final String SOAP_ADDRESS ="http://172.172.98.98/webopac/webservicedemo.asmx";
    private static final String SOAP_ADDRESS1 = "http://27.54.180.75/webopac/webservicedemo.asmx";

	EditText editText_memberId;
	EditText editText_password;
	SharedPreferences sharedpreferences;
    CheckBox cb;
    Global global;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		global=((Global)getApplicationContext());

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}


        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setLogo(R.drawable.ic_launcher);
//		supportActionBar.setDisplayHomeAsUpEnabled(true);

		editText_memberId=(EditText) this.findViewById(R.id.memberId);
		editText_password=(EditText) this.findViewById(R.id.password);
        cb = (CheckBox) findViewById(R.id.checkBoxI);

		sharedpreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

		if (sharedpreferences.contains("MemberId"))
		{
			editText_memberId.setText(sharedpreferences.getString("MemberId",""));
			editText_password.requestFocus();
		} else {
            overlay();
        }
    }


	public void loginClick(View v){

		String memberId=editText_memberId.getText().toString();
		String password=editText_password.getText().toString();


        if(memberId.trim().length()==0 || password.trim().length()==0){

			this.showAlert("Invalid Barcode ID or Password");
		} else {

            if (cb.isChecked()) {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

                PropertyInfo pi = new PropertyInfo();
                pi.setName("memberid");
                pi.setValue(memberId);
                pi.setType(String.class);
                request.addProperty(pi);
                pi = new PropertyInfo();
                pi.setName("password");
                pi.setValue(password);
                pi.setType(String.class);
                request.addProperty(pi);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS1);


                try {

                    httpTransport.call(SOAP_ACTION, envelope);
                    Object response = envelope.getResponse();


                    // if(response.toString().equals("false")){
                    if (response.toString().equals("none")) {

                        this.showAlert("Incorrect Barcode ID or Password");

                    } else if (response.toString().equals("C")) {

                        this.showAlert("Sorry, You are suspended. Kindly contact librarian.");

                    } else {

                        global.setEmails(response.toString().split(","));
                        global.setMember(memberId);

                        Editor editor = sharedpreferences.edit();
                        editor.putString("MemberId", memberId);
                        editor.apply();

                        Intent i = new Intent(this, Home_net.class);
//                        i.putExtra("memberid",memberId.toUpperCase());
                        startActivity(i);
                    }
                } catch (Exception exception) {
                    this.showAlert("Oops, There seems to be a problem with your internet connection.");
                }


// SOAP_ADDRESS is different from here

            } else {

                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

                PropertyInfo pi = new PropertyInfo();
                pi.setName("memberid");
                pi.setValue(memberId);
                pi.setType(String.class);
                request.addProperty(pi);
                pi = new PropertyInfo();
                pi.setName("password");
                pi.setValue(password);
                pi.setType(String.class);
                request.addProperty(pi);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);


                try {

                    httpTransport.call(SOAP_ACTION, envelope);
                    Object response = envelope.getResponse();


                    // if(response.toString().equals("false")){
                    if (response.toString().equals("none")) {

                        this.showAlert("Incorrect Barcode ID or Password");

                    } else if (response.toString().equals("C")) {

                        this.showAlert("Sorry, You are suspended. Kindly contact librarian.");

                    } else {

                        global.setEmails(response.toString().split(","));
                        global.setMember(memberId);

                        Editor editor = sharedpreferences.edit();
                        editor.putString("MemberId", memberId);
                        editor.apply();

                        Intent i = new Intent(this, Home.class);
                        //					i.putExtra("memberid",memberId.toUpperCase());
                        startActivity(i);
                    }
                } catch (Exception exception) {
                    this.showAlert("Oops, There seems to be a problem with your internet connection.");
                }
            }
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
				.setMessage("Are you sure you want to exit?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}).setNegativeButton("No", null).show();

	}

    public void aboutclick(View view) {

        Intent i = new Intent(this, AboutUs.class);
        startActivity(i);
    }


    public void overlay() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.coach_mark);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.coach_mark_master_view);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showAlert(String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.show();
    }
}