package website.timrobinson.cs496finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.HashMap;

public class AddSenatorsActivity extends AppCompatActivity implements OnClickListener {

    EditText sen1Name;
    EditText sen1Years;
    EditText sen1NetWorth;
    EditText sen1LargestIndustry;
    EditText sen2Name;
    EditText sen2Years;
    EditText sen2NetWorth;
    EditText sen2LargestIndustry;

    String szSen1Name;
    String szSen1Years;
    String szSen1NetWorth;
    String szSen1LargestIndustry;
    String szSen2Name;
    String szSen2Years;
    String szSen2NetWorth;
    String szSen2LargestIndustry;

    Button senatorSubmit;

    String stateKey;
    String sen1Key;
    String sen2Key;

    String putURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_senators);

        sen1Name = (EditText) findViewById(R.id.sen1Name);
        sen1Years = (EditText) findViewById(R.id.sen1Years);
        sen1NetWorth = (EditText) findViewById(R.id.sen1NetWorth);
        sen1LargestIndustry = (EditText) findViewById(R.id.sen1LargestIndustry);
        sen2Name = (EditText) findViewById(R.id.sen2Name);
        sen2Years = (EditText) findViewById(R.id.sen2Years);
        sen2NetWorth = (EditText) findViewById(R.id.sen2NetWorth);
        sen2LargestIndustry = (EditText) findViewById(R.id.sen2LargestIndustry);

        senatorSubmit = (Button) findViewById(R.id.senatorSubmit);

        senatorSubmit.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stateKey = extras.getString("stateId");
        }
    }

    @Override
    public void onClick(View v) {
        boolean flag = true;
        String toastMessage = "All fields must be completed";
        Toast validateToast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);

        szSen1Name = sen1Name.getText().toString();
        szSen1Years = sen1Years.getText().toString();
        szSen1NetWorth = sen1NetWorth.getText().toString();
        szSen1LargestIndustry = sen1LargestIndustry.getText().toString();
        szSen2Name = sen2Name.getText().toString();
        szSen2Years = sen2Years.getText().toString();
        szSen2NetWorth = sen2NetWorth.getText().toString();
        szSen2LargestIndustry = sen2LargestIndustry.getText().toString();

        if (szSen1Name.trim().equals("")) {
            flag = false;
        }
        if (szSen1Years.trim().equals("")) {
            flag = false;
        }
        if (szSen1NetWorth.trim().equals("")) {
            flag = false;
        }
        if (szSen1LargestIndustry.trim().equals("")) {
            flag = false;
        }
        if (szSen2Name.trim().equals("")) {
            flag = false;
        }
        if (szSen2Years.trim().equals("")) {
            flag = false;
        }
        if (szSen2NetWorth.trim().equals("")) {
            flag = false;
        }
        if (szSen2LargestIndustry.trim().equals("")) {
            flag = false;
        }

        if (flag) {
            // Do two POST requests and store the returned keys

            // First POST
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("name", szSen1Name);
            data.put("years", szSen1Years);
            data.put("net_worth", szSen1NetWorth);
            data.put("largest_ind_contributor", szSen1LargestIndustry);

            AsyncHttpPost asyncHttpPost1 = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    sen1Key = output;

                    HashMap<String, String> data2 = new HashMap<String, String>();
                    data2.put("name", szSen2Name);
                    data2.put("years", szSen2Years);
                    data2.put("net_worth", szSen2NetWorth);
                    data2.put("largest_ind_contributor", szSen2LargestIndustry);

                    AsyncHttpPost asyncHttpPost2 = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            sen2Key = output;

                            // 2 PUT requests here
                            putURL = "http://senators-1208.appspot.com/state/" + stateKey + "/senator/" + sen1Key;
                            AsyncHttpPut asyncHttpPut1 = new AsyncHttpPut(new AsyncHttpPut.AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
//
                                }
                            });
                            asyncHttpPut1.execute(putURL);

                            putURL = "http://senators-1208.appspot.com/state/" + stateKey + "/senator/" + sen2Key;
                            AsyncHttpPut asyncHttpPut2 = new AsyncHttpPut(new AsyncHttpPut.AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
//
                                }
                            });
                            asyncHttpPut2.execute(putURL);

                            finish();
                        }
                    }, data2);
                    asyncHttpPost2.execute("http://senators-1208.appspot.com/senator");

                }
            }, data);
            asyncHttpPost1.execute("http://senators-1208.appspot.com/senator");


            // Do two PUT requests with the key from the intent and the keys we just got back
        } else {
            validateToast.show();
        }

    }
}
