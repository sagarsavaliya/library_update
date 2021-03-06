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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CopyOfBookIssue extends Activity {
	
	 private static final String SOAP_ACTION1 = "http://tempuri.org/DoIssue";
	    private static final String OPERATION_NAME1 = "DoIssue";
	    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	    private static final String SOAP_ADDRESS ="http://172.172.98.98/webopac/webservicedemo.asmx";

	
	    private static final String SOAP_ACTION12 = "http://tempuri.org/DoIssue";
	    private static final String OPERATION_NAME12 = "DoIssue";
	    private static final String WSDL_TARGET_NAMESPACE12 = "http://tempuri.org/";
	    
	    private static final String SOAP_ADDRESS12 ="http://27.54.180.75/webopac/securewebservice.asmx";
	    
		 Global global;
		 String memberId;
		 
		 TextView title1;
		 TextView author1;
		 TextView accno1;
	 
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

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookissue);
		
		global=((Global)getApplicationContext());
		memberId=global.getMember();
		
		title1=(TextView) findViewById(R.id.title1);
		author1=(TextView) findViewById(R.id.author1);
		accno1=(TextView) findViewById(R.id.accno1);
		
		title1.setText(global.getTitle());
		author1.setText(global.getAuthor());
		accno1.setText(global.getAccno());
		
	}
	
	private Element buildAuthHeader() {
	    Element h = new Element().createElement("http://tempuri.org/","UserData");
	    Element username = new Element().createElement("http://tempuri.org/","usr");
	    username.addChild(Node.TEXT, "KMP1");
	    h.addChild(Node.ELEMENT, username);
	    Element pass = new Element().createElement("http://tempuri.org/","pwd");
	    pass.addChild(Node.TEXT, "PATEL1");
	    h.addChild(Node.ELEMENT, pass);

	    return h;  
		  
	}
	
	
	public void issueBook(View v){
		
		
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME1);
		
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
	       envelope.dotNet = true;
	       envelope.setOutputSoapObject(request);
	       
	       HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	       
	       try{
	    	   
	    	 
	           
	          
	    	   
	    	   httpTransport.call(SOAP_ACTION1, envelope);                    
	           Object response = envelope.getResponse();
	           
	            String result=response.toString();
	            
	            if(result.equals("true")){
	            	
	            	this.showAlert("Issued Succesfully");
	            	
	            	DownloadWebPageTask task = new DownloadWebPageTask();
					task.execute(new String[] { "http://www.vogella.com" });
	            	
//	            	Intent i=new Intent(this,Home.class);
//	            	this.startActivity(i);
	            }
	            else{
	            	this.showAlert("Failed to issue the book \nPlease try again");
	            }
	           
	           
	       }
	       catch(Exception ex){
	    	   this.showAlert(ex.toString());
	       }
	}
	
public void showAlert(String msg){		
		AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(msg);
		alertDialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		getMenuInflater().inflate(R.menu.option, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
//                write code to execute when clicked on this option
            	Intent intent=new Intent(this,MainActivity.class);
            	startActivity(intent);
                                               return true;   
                
              default:
                                  return super.onOptionsItemSelected(item);
        }
    }

//	@Override
//    public void onBackPressed() {
//            	 Intent intent = new Intent(this,Home.class);
//            	 startActivity(intent);
//    }
}