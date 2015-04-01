package com.example.rkucentrallibrary;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SelfIssue extends ActionBarActivity {

	private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	private static final String SOAP_ADDRESS = "http://172.172.98.98/webopac/webservicedemo.asmx";
	
	private static final String SOAP_ACTION = "http://tempuri.org/CheckAccession";
	private static final String OPERATION_NAME = "CheckAccession";
	private static final String SOAP_ACTION1 = "http://tempuri.org/GetBookDetails";
	private static final String OPERATION_NAME1 = "GetBookDetails";
	
	//secure service
	private static final String WSDL_TARGET_NAMESPACE2 = "http://tempuri.org/";
	private static final String SOAP_ADDRESS2 ="http://172.172.98.98/webopac/securewebservice.asmx";
	private static final String SOAP_ACTION2 = "http://tempuri.org/DoIssue";
    private static final String OPERATION_NAME2 = "DoIssue";

	Global global;
	String memberId;
	EditText accNo;
	Button issueButton;
	Button scanButton;
	TextView tv;
	TextView title1;
	TextView author1;
	TextView accno1;
	TextView ava1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfissue);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		global = ((Global) getApplicationContext());
		memberId = global.getMember();
		
		accNo = (EditText) this.findViewById(R.id.accno);
		accNo.setEnabled(false);
		issueButton = (Button) this.findViewById(R.id.button1);
		issueButton.setEnabled(false);

		title1 = (TextView) findViewById(R.id.title1);
		author1 = (TextView) findViewById(R.id.author1);
		accno1 = (TextView) findViewById(R.id.accno1);
		ava1 = (TextView) findViewById(R.id.ava);
	}

	public void scan(View v) {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		// start scanning
		scanIntegrator.initiateScan();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		// check we have a valid result
		if (scanningResult != null) {
			// get content from Intent Result
			String scanContent = scanningResult.getContents();
			// get format name of data scanned
			//String scanFormat = scanningResult.getFormatName();
			// output to UI
			accNo.setText(scanContent);
			// tv.setText("CONTENT: "+scanContent);
			issueButton.setEnabled(true);
			bkdetail();
		} else {
			// invalid scan data or scan canceled
			this.showAlert("No scan data received!");
		}
	}

	// public void check(View v){
	// String accno="et017893";
	public void bkdetail() {

		try {

			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

			PropertyInfo pi = new PropertyInfo();
			pi.setName("memberId");
			pi.setValue(memberId);
			pi.setType(String.class);
			request.addProperty(pi);
			pi = new PropertyInfo();
			pi.setName("accno");
			pi.setValue(accNo.getText().toString());
			pi.setType(String.class);
			request.addProperty(pi);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

			String result = null;

			httpTransport.call(SOAP_ACTION, envelope);
			Object response = envelope.getResponse();

			result = response.toString();
			
			title1.setText("Book Title");
			author1.setText(" - ");
			accno1.setText(" - ");
			ava1.setText(" - ");

			if (result.equals("0")) {

				this.showAlert("Incorrect AccNo");
			} else if (result.equals("-1")) {

				this.showAlert("Already Issued to a member");
			} else if (result.equals("-2")) {

				this.showAlert("Reference book can not issue");
			} else if (result.equals("-3")) {

				this.showAlert("Account limit exceeded");
			} else {
				// this.showAlert("issue");

				// Intent i=new Intent(this,bookdata.class);
				// this.startActivity(i);

				SoapObject request1 = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME1);

				PropertyInfo pi1 = new PropertyInfo();

				pi1.setName("accno");
				pi1.setValue(accNo.getText().toString());
				pi1.setType(String.class);
				request1.addProperty(pi1);

				SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope1.dotNet = true;
				envelope1.setOutputSoapObject(request1);

				HttpTransportSE httpTransport1 = new HttpTransportSE(SOAP_ADDRESS);

				String data = null;

				httpTransport1.call(SOAP_ACTION1, envelope1);
				Object response1 = envelope1.getResponse();

				data = response1.toString();

				if (data != null) {
					String[] data2 = data.split("~");
					
					title1.setText(data2[0]);
					author1.setText(data2[1]);
					accno1.setText(data2[2]);
					ava1.setText(data2[3]);
				}
			}
		}

		catch (Exception ex) {

			this.showAlert(ex.toString());
		}

	}

	private Element buildAuthHeader() {
	    String USR=global.getusr();
	    String PWD=global.getpwd();
	    Element h = new Element().createElement("http://tempuri.org/","UserData");
	    Element username = new Element().createElement("http://tempuri.org/","usr");
	    username.addChild(Node.TEXT,USR);
	    h.addChild(Node.ELEMENT, username);
	    Element pass = new Element().createElement("http://tempuri.org/","pwd");
	    pass.addChild(Node.TEXT,PWD);
	    h.addChild(Node.ELEMENT, pass);

	    return h;  
		  
	}
	
	
	public void issueBook(View v){
		
		
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE2, OPERATION_NAME2);
		
	 	   PropertyInfo pi=new PropertyInfo();
	   	   pi.setName("memberId");
	       pi.setValue(memberId);
	       pi.setType(String.class);
	       request.addProperty(pi);
	       pi=new PropertyInfo();
	       pi.setName("accno");
	       pi.setValue(global.getAccno());
	       pi.setType(String.class);
	       request.addProperty(pi);
	       
	       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	       
	       envelope.env="http://schemas.xmlsoap.org/soap/envelope/";     
	       envelope.headerOut=new Element[1];
		   envelope.headerOut[0] = buildAuthHeader();
	       envelope.dotNet = true;
	       envelope.setOutputSoapObject(request);
	       
	       HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS2);
	       
	       try{
	    	   
	    	    httpTransport.call(SOAP_ACTION2, envelope);                    
	            Object response = envelope.getResponse();
	           
	            String result=response.toString();
	            
	            if(result.equals("true")){
	            	
	            	this.showAlert("Issued Succesfully");
	            	
	            	DownloadWebPageTask task = new DownloadWebPageTask();
					task.execute(new String[] { "http://www.vogella.com" });
	            	
	            	Intent i=new Intent(this,Home.class);
	            	this.startActivity(i);
	            }
	            else{
	            	this.showAlert("Failed to issue the book \nPlease try again");
	            }
	       }
	       catch(Exception ex){
	    	   this.showAlert(ex.toString());
	       }
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		   
	    @Override
	    protected void onPostExecute(String result) {
	      
	    	showAlert(result);
	    }

		@Override
		protected String doInBackground(String... arg0) {
			
			Mail m = new Mail("central.library.rku@gmail.com", "Library@105");
			
			//String[] toArr = {"kamalmpatel@gmail.com"};

				String[] toArr =global.getEmails();
			
			  m.setTo(toArr); 
		      m.setFrom("central.library.rku@gmail.com"); 
		      m.setSubject("Acknowledgement for Successful Issue of Book - Central Library‏"); 
		      
		      String message="Dear Student ("+global.getMember().toUpperCase()+"),\n"+"Congratulation! \n" + "Your issue request for the book ("+global.getAccno().toUpperCase()+" -"+global.getTitle()+") have been successfully accepted.\nThanks for using self-issue service.\n";
		      
		     // m.setBody("Email body. Test Email."); 
		      
		      m.setBody(message.toString());
		      
		      try { 
		         // m.addAttachment("/sdcard/filelocation"); 
		   
		          if(m.send()) { 
		            return "Email was sent successfully.";
		          } else { 
		           return "Email was not sent.";
		          } 
		        } catch(Exception e) { 
		          //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
		          
		        	return e.toString();
		        }
			//return null; 
			
			
			
		}
	  }

	public void showAlert(String msg) {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(msg);
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		getMenuInflater().inflate(R.menu.option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.logout:
			// write code to execute when clicked on this option
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// public void onBackPressed() {
	// Intent intent = new Intent(this,Home.class);
	// intent.addCategory(Intent.CATEGORY_HOME);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// startActivity(intent);
	// }
}