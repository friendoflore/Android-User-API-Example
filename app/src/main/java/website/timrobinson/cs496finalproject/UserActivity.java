package website.timrobinson.cs496finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserActivity extends AppCompatActivity implements OnClickListener{

    final String[] userData = new String[1];
    final String[] stateId = new String[1];
    final String[] stateData = new String[1];
    final String[] senatorData1 = new String[1];
    final String[] senatorData2 = new String[1];

    String userKey = "";
    String senatorKey1;
    String senatorKey2;

    TextView username;
    TextView description;
    TextView state;
    TextView senator1;
    TextView senator2;

    TextView editUser;
    TextView addStateInfo;

    int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        username = (TextView) findViewById(R.id.username);
        description = (TextView) findViewById(R.id.description);
        state = (TextView) findViewById(R.id.USState);
        senator1 = (TextView) findViewById(R.id.senator1);
        senator2 = (TextView) findViewById(R.id.senator2);

        editUser = (TextView) findViewById(R.id.EditButton);
        addStateInfo = (TextView) findViewById(R.id.addStateButton);

        editUser.setOnClickListener(this);
        addStateInfo.setOnClickListener(this);

        // Load user data with GET request using Extra data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userKey = extras.getString("USER_ID");
        }
        String userURL = "http://user-api-1246.appspot.com/user/" + userKey;
        final String stateSearchURL = "http://senators-1208.appspot.com/state/search";
        final String[] stateURL = new String[1];
        final String[] senatorURL1 = new String[1];
        final String[] senatorURL2 = new String[1];
        Toast urlToast = Toast.makeText(getApplicationContext(), "URL: " + userURL, Toast.LENGTH_SHORT);
        urlToast.show();

        // Get the user data
        AsyncHttpGet asyncHttpGet = new AsyncHttpGet(new AsyncHttpGet.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                userData[0] = output;
                Toast testToast = Toast.makeText(getApplicationContext(), userData[0], Toast.LENGTH_SHORT);
                testToast.show();

                JSONObject userJSON;

                try {
                    userJSON = new JSONObject(userData[0]);
                    username.setText(userJSON.get("username").toString());

                    if (userJSON.get("description").equals("")) {
                        description.setText("No description added");
                    } else {
                        description.setText(userJSON.get("description").toString());
                    }

                    if (userJSON.get("state").equals(null)) {
                        state.setText("No state added");
                        senator1.setText("");
                        senator2.setText("");
                    } else {
                        state.setText(userJSON.get("state").toString());

                        // Do Get request of the other API
                        // This will actually take 4 GET requests, since we'll get state id, Senator IDs
                        // and then 2 more GET requests to get the senator names.
                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name", userJSON.get("state").toString());


                        // Get the user's state ID
                        AsyncHttpPost asyncHttpPostStateId = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                stateId[0] = output;
                                Toast testToast = Toast.makeText(getApplicationContext(), "State ID: " + stateId[0], Toast.LENGTH_SHORT);
                                testToast.show();

                                stateURL[0] = "http://senators-1208.appspot.com/state/" + stateId[0];


                                // Get the user's state data
                                AsyncHttpGet asyncHttpGetStateData = new AsyncHttpGet(new AsyncHttpGet.AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        stateData[0] = output;
                                        Toast testToast = Toast.makeText(getApplicationContext(), "State data: " + stateData[0], Toast.LENGTH_SHORT);
                                        testToast.show();

                                        JSONObject stateDataObj;

                                        try {
                                            stateDataObj = new JSONObject(stateData[0]);
                                            JSONArray senatorArray = (JSONArray) stateDataObj.get("senators");
                                            senatorKey1 = senatorArray.get(0).toString();
                                            senatorKey2 = senatorArray.get(1).toString();

                                            senatorURL1[0] = "http://senators-1208.appspot.com/senator/" + senatorKey1;
                                            senatorURL2[0] = "http://senators-1208.appspot.com/senator/" + senatorKey2;


                                            // Get the user's state's first senator
                                            AsyncHttpGet asyncHttpGetSenator1 = new AsyncHttpGet(new AsyncHttpGet.AsyncResponse() {
                                                @Override
                                                public void processFinish(String output) {
                                                    senatorData1[0] = output;

                                                    Toast testToast = Toast.makeText(getApplicationContext(), "Sen 1 data: " + senatorData1[0], Toast.LENGTH_SHORT);
                                                    testToast.show();

                                                    JSONObject senator1Obj;

                                                    try {
                                                        senator1Obj = new JSONObject(senatorData1[0]);
                                                        senator1.setText(senator1Obj.get("name").toString());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            asyncHttpGetSenator1.execute(senatorURL1[0]);

                                            // Get the user's state's second senator
                                            AsyncHttpGet asyncHttpGetSenator2 = new AsyncHttpGet(new AsyncHttpGet.AsyncResponse() {
                                                @Override
                                                public void processFinish(String output) {
                                                    senatorData2[0] = output;

                                                    Toast testToast = Toast.makeText(getApplicationContext(), "Sen 2 data: " + senatorData2[0], Toast.LENGTH_SHORT);
                                                    testToast.show();

                                                    JSONObject senator2Obj;

                                                    try {
                                                        senator2Obj = new JSONObject(senatorData2[0]);
                                                        senator2.setText(senator2Obj.get("name").toString());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            asyncHttpGetSenator2.execute(senatorURL2[0]);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                asyncHttpGetStateData.execute(stateURL[0]);
                            }
                        }, data);
                        asyncHttpPostStateId.execute(stateSearchURL);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        asyncHttpGet.execute(userURL);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.EditButton:
                // Go to CreateActivity
                String test = "Edit user";
                Toast toast = Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT);
                toast.show();
                Intent goToEditActivity = new Intent(getApplicationContext(), EditActivity.class);
                goToEditActivity.putExtra("USER_ID", userKey);
                goToEditActivity.putExtra("username", username.getText());
                goToEditActivity.putExtra("description", description.getText());
                startActivityForResult(goToEditActivity, requestCode);

                break;
            case R.id.addStateButton:
                // Go to LoginActivity
                String test2 = "Add state info";
                Toast toast2 = Toast.makeText(getApplicationContext(), test2, Toast.LENGTH_SHORT);
                toast2.show();
                Intent goToAddStateActivity = new Intent(getApplicationContext(), AddStateActivity.class);
                goToAddStateActivity.putExtra("USER_ID", userKey);
                goToAddStateActivity.putExtra("state", state.getText());
                startActivityForResult(goToAddStateActivity, requestCode);

                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent restartIntent = getIntent();
        finish();
        startActivity(restartIntent);
    }
}
