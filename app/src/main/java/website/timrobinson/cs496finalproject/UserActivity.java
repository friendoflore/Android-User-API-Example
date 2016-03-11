package website.timrobinson.cs496finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity implements OnClickListener{

    final String[] userData = new String[1];

    String userKey = "";

    TextView username;
    TextView description;
    TextView state;
    TextView senator1;
    TextView senator2;

    TextView editUser;
    TextView addStateInfo;

//    final String[] dUserKey = new String[1];
//    final String[] dUsername = new String[1];
//    final String[] dDescription = new String[1];
//    final String[] dState = new String[1];

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
        Toast urlToast = Toast.makeText(getApplicationContext(), "URL: " + userURL, Toast.LENGTH_SHORT);
        urlToast.show();

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
                        // This will actually take 3 GET requests, since we'll get Senator IDs
                        // and then 2 more GET requests to get the senator names.


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
                startActivity(goToEditActivity);
                break;
            case R.id.addStateButton:
                // Go to LoginActivity
                String test2 = "Add state info";
                Toast toast2 = Toast.makeText(getApplicationContext(), test2, Toast.LENGTH_SHORT);
                toast2.show();
                Intent goToAddStateActivity = new Intent(getApplicationContext(), AddStateActivity.class);
                goToAddStateActivity.putExtra("USER_ID", userKey);
                goToAddStateActivity.putExtra("state", state.getText());
                startActivity(goToAddStateActivity);
                break;
            default:
                break;
        }
    }
}
