package com.example.rkucentrallibrary;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private static final String SOAP_ACTION = "http://tempuri.org/LoginTest";
    private static final String OPERATION_NAME = "LoginTest";
    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    //private static final String SOAP_ADDRESS ="http://10.0.2.2/logindemo2/DemoWebService.asmx";
    private static final String SOAP_ADDRESS ="http://27.54.180.75/webopac/webservicedemo.asmx";

	EditText editText_memberId;
	EditText editText_password;
	SharedPreferences sharedpreferences;
	
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
		
		editText_memberId=(EditText) this.findViewById(R.id.memberId);
		editText_password=(EditText) this.findViewById(R.id.password);
		
		sharedpreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
		
		if (sharedpreferences.contains("MemberId"))
	      {
	         editText_memberId.setText(sharedpreferences.getString("MemberId",""));
	         editText_password.requestFocus();
	      }
	}
	
	public void showAlert(String msg){
		
		AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(msg);
		alertDialog.show();
	}
	
	
	
	public void loginClick(View v){
		
		String memberId=editText_memberId.getText().toString();
		String password=editText_password.getText().toString();
		
		
		if(memberId.trim().length()==0 || password.trim().length()==0){
		
			this.showAlert("Invalid Barcode ID or Password");
		}
		
		else{
			
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
			
		   	PropertyInfo pi=new PropertyInfo();
		   	pi.setName("memberid");
		       pi.setValue(memberId);
		       pi.setType(String.class);
		       request.addProperty(pi);
		       pi=new PropertyInfo();
		       pi.setName("password");
		       pi.setValue(password);
		       pi.setType(String.class);
		       request.addProperty(pi);
		       
		       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		       envelope.dotNet = true;
		       envelope.setOutputSoapObject(request);
		       
		       HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		       
		       
		         	
		       
		      try  
		       {     
		    	  
		           httpTransport.call(SOAP_ACTION, envelope);                    
		           Object response = envelope.getResponse();  

		          
		           
		          // if(response.toString().equals("false")){
		           if(response.toString().equals("none")){
		        	   
		        	   this.showAlert("Incorrect Barcode ID or Password");
		        	   
		           }
		           else if(response.toString().equals("C")){
		        	   
		        	   this.showAlert("Sorry, You are suspended. Kindly contact librarian.");
		        	   
		           }
		           else{
		        	   
		        	   global.setEmails(response.toString().split(","));
		        	   global.setMember(memberId);
		        	   
		        	   Editor editor = sharedpreferences.edit();
		        	   editor.putString("MemberId",memberId);
		        	   editor.commit();
		        	      
		        	   Intent i=new Intent(this,Home.class);
		        	   i.putExtra("memberid",memberId.toUpperCase());
		        	   startActivity(i);
		           }
		       }  
		       catch (Exception exception)   
		       {
		    	   
		           this.showAlert("Opps, There seems to be a problem with your internet connection.");    
		    	   
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

}