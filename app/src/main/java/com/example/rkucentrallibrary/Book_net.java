package com.example.rkucentrallibrary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book_net extends ActionBarActivity {

    private static final String SOAP_ACTION = "http://tempuri.org/DoRenew";
    private static final String OPERATION_NAME = "DoRenew";
    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ADDRESS = "http://27.54.180.75/webopac/securewebservice.asmx";
    private static final String SOAP_ACTION1 = "http://tempuri.org/getBookImageURL";
    private static final String OPERATION_NAME1 = "getBookImageURL";
    private static final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";
    private static final String SOAP_ADDRESS1 = "http://27.54.180.75/webopac/webservicedemo.asmx";
    Date duedate;
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
    ImageView iv;
    ProgressDialog pd;

    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);

        global = ((Global) getApplicationContext());

        title1 = (TextView) findViewById(R.id.title1);
        author1 = (TextView) findViewById(R.id.author1);
        due1 = (TextView) findViewById(R.id.due1);
        renew1 = (TextView) findViewById(R.id.renew1);
        issue1 = (TextView) findViewById(R.id.issue1);
        accno1 = (TextView) findViewById(R.id.accno1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            d = extras.getString("bookdata");

        }

        da = d.split("~");

        title1.setText(da[1]);
        author1.setText(da[2]);
        accno1.setText(da[3]);
        // renew1.setText(da[6]);
        renew1.setText(Integer.toString(Integer.parseInt(da[6])));

        renewcount = Integer.parseInt(da[6]);


        //webservice to display book image


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE1, OPERATION_NAME1);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("accno");
        pi.setValue(da[3]);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS1);

        try {

            httpTransport.call(SOAP_ACTION1, envelope);
            Object response = envelope.getResponse();

            String imgurl = response.toString();

            pd = new ProgressDialog(this);
            pd.setMessage("Downloading Image");
            iv = (ImageView) findViewById(R.id.bookimg);
            new DownloadImageTask(iv).execute(imgurl);
        } catch (Exception ex) {
            this.showAlert(ex.toString());
        }

        // issuedate

        String date11[] = da[5].split("/");
        String date22[] = date11[2].split(" ");

        if (date11[1].length() < 2) {
            date11[1] = "0" + date11[1];
        }
        if (date11[0].length() < 2) {
            date11[0] = "0" + date11[0];
        }
        String datenew11 = date11[0] + "/" + date11[1] + "/" + date22[0];

        // duedate
        String date1[] = da[4].split("/");
        String date2[] = date1[2].split(" ");
        if (date1[1].length() < 2) {
            date1[1] = "0" + date1[1];
        }
        if (date1[0].length() < 2) {
            date1[0] = "0" + date1[0];
        }
        datenew = date1[0] + "/" + date1[1] + "/" + date2[0];

        SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");

        try {
            duedate = formatter2.parse(datenew);
            Date issuedate = formatter2.parse(datenew11);

            SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
            due1.setText(sdf.format(duedate));
            issue1.setText(sdf.format(issuedate));

        } catch (Exception e) {

        }
    }

    private Element buildAuthHeader() {
        String USR = global.getusr();
        String PWD = global.getpwd();

        Element h = new Element().createElement("http://tempuri.org/",
                "UserData");
        Element username = new Element().createElement("http://tempuri.org/",
                "usr");
        username.addChild(Node.TEXT, USR);
        h.addChild(Node.ELEMENT, username);
        Element pass = new Element()
                .createElement("http://tempuri.org/", "pwd");
        pass.addChild(Node.TEXT, PWD);
        h.addChild(Node.ELEMENT, pass);

        return h;

    }

    public void renew(View v) {

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
                OPERATION_NAME);

        String memberid = global.getMember();

        PropertyInfo pi = new PropertyInfo();
        pi.setName("memberId");
        pi.setValue(memberid);
        pi.setType(String.class);
        request.addProperty(pi);

        String acc = global.getAccno();

        pi = new PropertyInfo();
        pi.setName("accno");
        pi.setValue(acc);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("book_title");
        pi.setValue(da[1]);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);

        envelope.env = "http://schemas.xmlsoap.org/soap/envelope/";
        envelope.headerOut = new Element[1];
        envelope.headerOut[0] = buildAuthHeader();
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        // System.setProperty("http.keepAlive","false");
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();

            this.showAlert(response.toString());

            if (response.toString().contains("done successfully")) {
                DownloadWebPageTask task = new DownloadWebPageTask();
                task.execute(new String[]{"http://www.vogella.com"});
            } else {
                Toast.makeText(Book_net.this, "Unable to send mail",
                        Toast.LENGTH_LONG).show();
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

    public void showAlertButton(String msg) {
        final Intent homepage2 = new Intent(this, Home.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
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
            m.setFrom("central.library.rku@gmail.com");
            m.setSubject("Acknowledgement for Successful Renewal of Book - Central Library‏");

            String message = "Dear Student ("
                    + global.getMember().toUpperCase()
                    + "),\n"
                    + "Congratulation! \n"
                    + "Your renewal request for the book ("
                    + global.getAccno().toUpperCase()
                    + " -"
                    + global.getTitle()
                    + ") have been successfully accepted & due date is extended upto : "
                    + global.getDuedate() + ". \n"
                    + "Thanks for using self-renewal service.\n";

            // m.setBody("Email body. Test Email.");

            m.setBody(message.toString());

            try {
                // m.addAttachment("/sdcard/filelocation");

                if (m.send()) {
                    return "Email was sent successfully.";
                } else {
                    return "Email was not sent.";
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

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            pd.dismiss();
            bmImage.setImageBitmap(result);
        }
    }

}