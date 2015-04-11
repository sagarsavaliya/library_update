package com.example.rkucentrallibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

public class Home_net extends ActionBarActivity {

    private static final String SOAP_ACTION = "http://tempuri.org/GetData";
    private static final String OPERATION_NAME = "GetData";
    private static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    //	private static final String SOAP_ADDRESS = "http://172.172.98.98/webopac/webservicedemo.asmx";
    private static final String SOAP_ADDRESS = "http://27.54.180.75/webopac/webservicedemo.asmx";

    private static final String SOAP_ACTION1 = "http://tempuri.org/KMPService";
    private static final String OPERATION_NAME1 = "KMPService";
    private static final String WSDL_TARGET_NAMESPACE1 = "http://tempuri.org/";
    private static final String SOAP_ADDRESS1 = "http://172.172.98.98/webopac/securewebservice.asmx";

    // http://27.54.180.75/webopac/securewebservice.asmx
    // http://172.172.98.98/webopac/securewebservice.asmx

    TextView tv;
    String memberid;
    ListView listView;

    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ActionBar supportActionBar = getSupportActionBar();

//		FloatingActionButton button = (FloatingActionButton) findViewById(R.id.issueButton);
//		button.setSize(FloatingActionButton.SIZE_MINI);
//		button.setColorNormalResId(R.color.fabBtnColor);
//		button.setColorPressedResId(R.color.fabBtnPressedColor);
//		button.setIcon(R.drawable.ic_fab_star);
//		button.setStrokeVisible(false);

        loadActivity();
    }


    private void loadActivity() {
        // Done all of My work here

        global = ((Global) getApplicationContext());

		/*Bundle extras = getIntent().getExtras();
        if (extras != null) {
			memberid = extras.getString("memberid");
			// tv.setText(memberid);
		}*/

        memberid = global.getMember();

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
                OPERATION_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("memberid");
        pi.setValue(memberid);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        final String s[];

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            Object response = envelope.getResponse();

            if (response.toString().equals(" ")) {

//				this.showAlert("Go and get some books from Central Library @ RK University.");

            } else {

                s = response.toString().split("~");
                int length = s.length;
                int notitle = length / 7;
                int index = 1;
                String[] title = new String[notitle];
                int i = 0;

                for (int j = 1; j <= notitle; j++) {

                    title[i] = s[index];
                    index = index + 7;
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, title);

                listView = (ListView) findViewById(R.id.books);
                listView.setAdapter(adapter);

                final Intent inte = new Intent(this, Book_net.class);

                listView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {

                        // int apple=(String) listView.getItemAtPosition(arg2)
                        int data = 0;

                        for (int k = 0; k < s.length; k++) {

                            if (s[k].equals(listView
                                    .getItemAtPosition(arg2))) {

                                data = k;
                                break;
                            }
                        }

                        String bookdata = s[data - 1] + "~" + s[data] + "~"
                                + s[data + 1] + "~" + s[data + 2] + "~"
                                + s[data + 3] + "~" + s[data + 4] + "~"
                                + s[data + 5];

                        global.setAccno(s[data + 2]);
                        global.setTitle(s[data]);

                        inte.putExtra("bookdata", bookdata);
                        startActivity(inte);

                    }

                });

            }
        } catch (Exception e) {

            // this.showAlert("Opps, There seems to be a problem with your internet connection.");
            this.showAlert(e.toString());
        }
    }

    public void showAlert(String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.show();
    }


    public void issueBook(View v) {
        this.showAlert("Books can be issued from library network only");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.option, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

//		case R.id.action_refresh:
//			this.onCreate(null);
//			loadActivity();
//			break;


            case R.id.action_statistics:
                Intent inte = new Intent(this, Statistics_net.class);
                startActivity(inte);
                break;

            case R.id.action_feedback:
                Intent in = new Intent(this, Feedback_net.class);
                startActivity(in);
                break;

            case R.id.logout:
                // write code to execute when clicked on this option
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
