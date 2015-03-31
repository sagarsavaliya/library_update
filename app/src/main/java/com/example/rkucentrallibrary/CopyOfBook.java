package com.example.rkucentrallibrary;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CopyOfBook extends Activity {
	
	Date duedate;
	
	private static final String SOAP_ACTION = "http://tempuri.org/Issue";
    private static final String OPERATION_NAME = "Issue";
    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ADDRESS ="http://27.54.180.75/webopac/webservicedemo.asmx";
    
    private static final String SOAP_ACTION1 = "http://tempuri.org/Count";
    private static final String OPERATION_NAME1 = "Count";
    private static final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";
    private static final String SOAP_ADDRESS1 ="http://27.54.180.75/webopac/webservicedemo.asmx";
	
	String d;
	 Date finalduedate;
	 int renewcount;
	 String datenew;
	 TextView title1;
	 TextView due1;
	 TextView author1;
	 TextView renew1;
	 TextView issue1;
	 TextView accno1;
	 String da[];
	 
	 Global global;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book);
		
		global=((Global)getApplicationContext());
		
		 title1=(TextView) findViewById(R.id.title1);
		author1=(TextView) findViewById(R.id.author1);
		due1=(TextView) findViewById(R.id.due1);
		renew1=(TextView) findViewById(R.id.renew1);
		issue1=(TextView) findViewById(R.id.issue1);
		accno1=(TextView) findViewById(R.id.accno1);
		
		
		
		
		
		Bundle extras=getIntent().getExtras();
		if (extras != null) {
		    d = extras.getString("bookdata");
		    
		}
		
		da=d.split("~");
		
		title1.setText(da[1]);
		author1.setText(da[2]);
		accno1.setText(da[3]);
		//renew1.setText(da[6]);
		renew1.setText(Integer.toString(Integer.parseInt(da[6])));
		
		renewcount = Integer.parseInt(da[6]);
		
		//issuedate
		
		String date11[]=da[5].split("/");
		
		
		String date22[]=date11[2].split(" ");
		
		
		if(date11[1].length()<2){
			
			date11[1]="0"+date11[1];
		}
		
		if(date11[0].length()<2){
			
			date11[0]="0"+date11[0];
		}
		
		String datenew11=date11[0]+"/"+date11[1]+"/"+date22[0];
		
		
		//duedate
		String date1[]=da[4].split("/");
		
		
		String date2[]=date1[2].split(" ");
		
		
		if(date1[1].length()<2){
			
			date1[1]="0"+date1[1];
		}
		
		if(date1[0].length()<2){
			
			date1[0]="0"+date1[0];
		}
		
		datenew=date1[0]+"/"+date1[1]+"/"+date2[0];
		
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
		
		
		 
		try {
			duedate = formatter2.parse(datenew);
			
			Date issuedate=formatter2.parse(datenew11);
			
		SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
		due1.setText(sdf.format(duedate));
		issue1.setText(sdf.format(issuedate));
		
		}catch(Exception e){
			
			
		}
		
		//txt.setText(datenew);
		
		
			/*txt2.setText(date.toString());
			txt1.setText(formatter.format(date));
			
			GregorianCalendar cal = new GregorianCalendar();
	        cal.setTime(date);
	        cal.add(Calendar.DATE,15);
	        
	                 
	         //txt.setText(cal.getTime().getDay());
	         
	         String game=Integer.toString(cal.getTime().getDay());
	         
	         txt.setText(game);
	         
	         Calendar c = Calendar.getInstance();
	         txt.setText(c.getTime().toString());

	         SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	         String formattedDate = df.format(c.getTime());
	         
	         txt1.setText(formattedDate);
	         
	 
		} catch (Exception e) {
			txt.setText(e.toString());
		}*/
		
		
		
		
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
		      m.setSubject("Acknowledgement for Successful Renewal of Book - Central Library‏"); 
		      
		      String message="Dear Student ("+global.getMember().toUpperCase()+"),\n"+"Congratulation! \n" + "Your renewal request for the book ("+global.getAccno().toUpperCase()+" -"+global.getTitle()+") have been successfully accepted & due date is extended upto : "+global.getDuedate()+". \n"+"Thanks for using self-renewal service.\n";
		      
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

	
	
	
	public void renew(View v){
		
		
		
		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		
		 
		try {
			//Date duedate = formatter.parse(datenew);
			
			Calendar c = Calendar.getInstance();
	         Date currentdate=c.getTime();
	         
	         //int days44 = Days.daysBetween(new DateTime(currentdate), new DateTime(duedate)).getDays();
	         
	         //Toast.makeText(this,currentdate.toString(),Toast.LENGTH_LONG).show();
	         
	         //Toast.makeText(this,Integer.toString(days44),Toast.LENGTH_LONG).show();
	         
	         //
	         
	         
	         
	         /*if(days44==14){
	        	 
	        	 this.showAlert("You can not renew same book on same day more than once.");
	         }*/

	        /* else if(duedate.compareTo(currentdate)<0 || duedate.compareTo(currentdate)==0){
	        	
	        	this.showAlert("Due Date is gone. Kindly contact library.");
	        }*/
	         
	         
	        int ab= duedate.compareTo(currentdate);
	        
	        if(duedate.getDay()==currentdate.getDay()){
	        	
	        	
	        }
	        
	       
	         
	         if((duedate.compareTo(currentdate))<0){
		        	this.showAlert("This book is overdue, it can not be renewed, please go to Central Library @ RK University.");
		     
	         }
	         
	        else if(renewcount>=3){
	        	
	        	
	        	this.showAlert("Renewal limit is exceeded. Kindly return your book to Central Library @ RK University.");
	        }
	        else{
	        	//
	        	
	        	
	        	SoapObject request1 = new SoapObject(WSDL_TARGET_NAMESPACE1, OPERATION_NAME1);
				
			   	PropertyInfo pi1=new PropertyInfo();
			   	pi1.setName("memberId");
			       pi1.setValue(da[0]);
			       pi1.setType(String.class);
			       request1.addProperty(pi1);
			       
			       
			       SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			       envelope1.dotNet = true;
			       envelope1.setOutputSoapObject(request1);
			       
			       HttpTransportSE httpTransport1 = new HttpTransportSE(SOAP_ADDRESS1);
			       
			       
			         	
			       
			          
			    	  
			           httpTransport1.call(SOAP_ACTION1, envelope1);                    
			           Object response1 = envelope1.getResponse();  

			          //Toast.makeText(this,response1.toString(),Toast.LENGTH_LONG).show();
			           
			           int count1= Integer.parseInt(response1.toString());
	        	
	        	//
	        	
	        	
	        	GregorianCalendar cal = new GregorianCalendar();
		        cal.setTime(currentdate);
		        cal.add(Calendar.DATE,count1);
		        Date newduedate=cal.getTime();
		       
		        
		        if(newduedate.getDay()==6){
		        	
		        	cal.setTime(newduedate);
		        	cal.add(Calendar.DATE,2);
		        	finalduedate=cal.getTime();
		        }
		        else if(newduedate.getDay()==0){
		        	
		        	cal.setTime(newduedate);
		        	cal.add(Calendar.DATE,1);
		        	finalduedate=cal.getTime();
		        }
		        else{
		        	
		        	finalduedate=newduedate;
		        }
		        
		        int days445 = Days.daysBetween(new DateTime(duedate), new DateTime(finalduedate)).getDays();
		        
		        if(days445==0){
		        	
		        	this.showAlert("You can not renew same book on same day more than once.");
		        	//Toast.makeText(this,"You can not renew same book on same day more than once.",Toast.LENGTH_LONG).show();
		        }
		        
		        //Toast.makeText(this,Integer.toString(days445),Toast.LENGTH_LONG).show();
		        else{
		        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		         String formattedDate = df.format(finalduedate);
		         
		         SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
					
				   	PropertyInfo pi=new PropertyInfo();
				   	pi.setName("memberid");
				       pi.setValue(da[0]);
				       pi.setType(String.class);
				       request.addProperty(pi);
				       pi=new PropertyInfo();
				       pi.setName("accno");
				       pi.setValue(da[3]);
				       pi.setType(String.class);
				       request.addProperty(pi);
				       pi=new PropertyInfo();
				       pi.setName("duedate");
				       pi.setValue(formattedDate);
				       pi.setType(String.class);
				       request.addProperty(pi);
				       
				       SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				       envelope.dotNet = true;
				       envelope.setOutputSoapObject(request);
				       
				       HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
				       
				         
				    	  
				           httpTransport.call(SOAP_ACTION, envelope);                    
				           Object response = envelope.getResponse();  

				           
				           if(response.toString().equals("false")){
				        	   
				        	   
				        	   this.showAlert("Sorry there is some problem.");
				        	   
				           }
				           else{
				        	   
								DownloadWebPageTask task = new DownloadWebPageTask();
								task.execute(new String[] { "http://www.vogella.com" });
				        	   
				        	    duedate=finalduedate;
				        	    SimpleDateFormat sdf55 = new SimpleDateFormat("E, MMM dd yyyy");
				       			due1.setText(sdf55.format(finalduedate));
				       			
				       			global.setDuedate(sdf55.format(finalduedate));
				       			
				       			renewcount=renewcount+1;
				       			renew1.setText(Integer.toString(renewcount));
				        	    this.showAlertButton("Book renewed successfully.");
				        	   
				           }
				       }  
		        
		       
	        }
				   
	        	
	        
			
		} catch (Exception e) {
			
			this.showAlert("Opps, There seems to be a problem with your internet connection.");
			//this.showAlert(e.toString());
		}
            
       
	}
	
public void showAlert(String msg){
		
		AlertDialog alertDialog=new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(msg);
		alertDialog.show();
	}

public void showAlertButton(String msg){
	 final Intent homepage2=new Intent(this,Home.class);
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(msg)
	   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	       public void onClick(DialogInterface dialog, int id) {
	    	  
		        startActivity(homepage2);
	       }
	   });
	  
	AlertDialog alert = builder.create();
	alert.show();
	
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
	                                             // write code to execute when clicked on this option
	            	Intent intent=new Intent(this,MainActivity.class);
	            	startActivity(intent);
	                                               return true;   
	                
	              default:
	                                  return super.onOptionsItemSelected(item);
	        }
	    }

}
