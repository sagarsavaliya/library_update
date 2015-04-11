package com.example.rkucentrallibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Feedback_net extends ActionBarActivity {

    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ADDRESS = "http://27.54.180.75/webopac/webservicedemo.asmx";
    private static final String SOAP_ACTION = "http://tempuri.org/SubmitComment";
    private static final String OPERATION_NAME = "SubmitComment";
    EditText et;
    Global global;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        global = ((Global) getApplicationContext());
        et = (EditText) findViewById(R.id.editText1);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    public void Submitbtn(View v) {

        try {

            SoapObject request1 = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("memcd");
            pi1.setValue(global.getMember());
            pi1.setType(String.class);
            request1.addProperty(pi1);
            pi1 = new PropertyInfo();
            pi1.setName("comment");
            pi1.setValue(et.getText().toString());
            pi1.setType(String.class);
            request1.addProperty(pi1);

            int rating = (int) ratingBar.getRating();

            pi1 = new PropertyInfo();
            pi1.setName("rating");
            pi1.setValue(rating);
            pi1.setType(Float.class);
            request1.addProperty(pi1);

            SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope1.dotNet = true;
            envelope1.setOutputSoapObject(request1);

            HttpTransportSE httpTransport1 = new HttpTransportSE(SOAP_ADDRESS);

            String res;

            httpTransport1.call(SOAP_ACTION, envelope1);
            Object response1 = envelope1.getResponse();

            res = response1.toString();
            if (res.contains("true")) {
                this.showAlert("Thank You for your valuable feedback");
                Intent intent = new Intent(this, Home_net.class);
                startActivity(intent);
            } else if (res.contains("false")) {
                this.showAlert("Please follow rules and try again");
            } else {
                this.showAlert(res);
            }
        } catch (Exception ex) {
            this.showAlert(ex.toString());
        }
    }

    public void showAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.show();
    }
}
