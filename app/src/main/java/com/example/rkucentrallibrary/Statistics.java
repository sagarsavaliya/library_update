package com.example.rkucentrallibrary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

public class Statistics extends ActionBarActivity {


    private static final String SOAP_ACTION1 = "http://tempuri.org/getLoginCount";
    private static final String OPERATION_NAME1 = "getLoginCount";
    private static final String SOAP_ACTION2 = "http://tempuri.org/getRenewCount";
    private static final String OPERATION_NAME2 = "getRenewCount";
    private static final String SOAP_ACTION3 = "http://tempuri.org/getSelfIssueCount";
    private static final String OPERATION_NAME3 = "getSelfIssueCount";

    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ADDRESS = "http://172.172.98.98/webopac/webservicedemo.asmx";
//    private static final String SOAP_ADDRESS = "http://27.54.180.75/webopac/webservicedemo.asmx";

    Global global;
    TextView logincount;
    TextView renewcount;
    TextView issuecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        global = ((Global) getApplicationContext());

        logincount = (TextView) findViewById(R.id.lcount);
        renewcount = (TextView) findViewById(R.id.rcount);
        issuecount = (TextView) findViewById(R.id.icount);

        try {
            //Login Count
            SoapObject request1 = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME1);

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("memid");
            pi1.setValue(global.getMember());
            pi1.setType(String.class);
            request1.addProperty(pi1);

            SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope1.dotNet = true;
            envelope1.setOutputSoapObject(request1);

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {

                httpTransport.call(SOAP_ACTION1, envelope1);
                Object response1 = envelope1.getResponse();
                logincount.setText(response1.toString());
            } catch (Exception ex) {
                this.showAlert(ex.toString());
            }
            //Renew Count
            SoapObject request2 = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME2);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("memid");
            pi2.setValue(global.getMember());
            pi2.setType(String.class);
            request2.addProperty(pi1);

            SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope2.dotNet = true;
            envelope2.setOutputSoapObject(request2);

            HttpTransportSE httpTransport2 = new HttpTransportSE(SOAP_ADDRESS);

            try {

                httpTransport2.call(SOAP_ACTION2, envelope2);
                Object response2 = envelope2.getResponse();
                renewcount.setText(response2.toString());
            } catch (Exception ex) {
                this.showAlert(ex.toString());
            }
            //Issue Count
            SoapObject request3 = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME3);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("memid");
            pi3.setValue(global.getMember());
            pi3.setType(String.class);
            request3.addProperty(pi3);

            SoapSerializationEnvelope envelope3 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope3.dotNet = true;
            envelope3.setOutputSoapObject(request3);

            HttpTransportSE httpTransport3 = new HttpTransportSE(SOAP_ADDRESS);

            try {

                httpTransport3.call(SOAP_ACTION1, envelope3);
                Object response3 = envelope3.getResponse();
                issuecount.setText(response3.toString());
            } catch (Exception ex) {
                this.showAlert(ex.toString());
            }
        } catch (Exception ex) {
            this.showAlert(ex.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.show();
    }
}
