package website.timrobinson.cs496finalproject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddStateActivity extends AppCompatActivity implements OnClickListener {

    String stateString;
    String userKey;

    TextView latitude;
    TextView longitude;
    TextView state;
    TextView stateMissingMessage;
    EditText population;
    EditText medianIncome;
    EditText costOfLivingIndex;
    EditText largestEmployer;
    Button submitState;
    Button submitStateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_state);

        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        state = (TextView) findViewById(R.id.state);
        population = (EditText) findViewById(R.id.population);
        medianIncome = (EditText) findViewById(R.id.medianIncome);
        costOfLivingIndex = (EditText) findViewById(R.id.costOfLivingIndex);
        largestEmployer = (EditText) findViewById(R.id.largestEmployer);
        submitState = (Button) findViewById(R.id.submitState);
        submitStateInfo = (Button) findViewById(R.id.submitStateInfo);
        stateMissingMessage = (TextView) findViewById(R.id.stateMissingMessage);

        submitState.setOnClickListener(this);
        submitStateInfo.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userKey = extras.getString("USER_ID");
        }

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
    }

    class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if(location != null) {
                double pLat = location.getLatitude();
                double pLong = location.getLongitude();

                latitude.setText(Double.toString(pLat));
                longitude.setText(Double.toString(pLong));

                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(pLat, pLong, 1);
                    stateString = addresses.get(0).getAdminArea();
                    state.setText(stateString);

                    // Check to see if state is in the DB. If it is not, prompt the user if they
                    // would like to add their state's data

                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("name", stateString);

                    AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {

                        @Override
                        public void processFinish(String output) {
                            if (!output.equals("")) {
                                // The state is already in the database, expose "Add state button"
                                // This allows the user to return to their homepage, now with state data
                                // The add state button does a POST request to "../edit", saving the
                                // user's new state data
                                population.setVisibility(View.GONE);
                                medianIncome.setVisibility(View.GONE);
                                costOfLivingIndex.setVisibility(View.GONE);
                                largestEmployer.setVisibility(View.GONE);
                                submitState.setVisibility(View.GONE);
                                submitStateInfo.setVisibility(View.GONE);
                                stateMissingMessage.setVisibility(View.GONE);
                                submitState.setVisibility(View.VISIBLE);

                            } else {
                                // The state is not in the DB, expose input fields and "Add state info button"
                                // This state info culminates in a POST request to the state API
                                // We then start the "AddSenatorsActivity"
                                    // The AddSenatorsActivity will finish with 2 POST requests and a PUT request
                                submitState.setVisibility(View.GONE);
                                population.setVisibility(View.VISIBLE);
                                medianIncome.setVisibility(View.VISIBLE);
                                costOfLivingIndex.setVisibility(View.VISIBLE);
                                largestEmployer.setVisibility(View.VISIBLE);
                                submitStateInfo.setVisibility(View.VISIBLE);
                                stateMissingMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }, data);
                    asyncHttpPost.execute("http://senators-1208.appspot.com/state/search");


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submitState:
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("user_id", userKey);
                data.put("state", stateString);

                AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {

                    @Override
                    public void processFinish(String output) {
                        Toast testToast = Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT);
                        testToast.show();
                        finish();
                        return;
                    }
                }, data);
                asyncHttpPost.execute("http://user-api-1246.appspot.com/edit");
                break;
            case R.id.submitStateInfo:
                boolean flag = true;
                String toastMessage = "All fields must be completed";
                Toast validateToast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
                if (population.getText().toString().trim().equals("")) {
                    flag = false;
                }
                if (medianIncome.getText().toString().trim().equals("")) {
                    flag = false;
                }
                if (costOfLivingIndex.getText().toString().trim().equals("")) {
                    flag = false;
                }
                if (largestEmployer.getText().toString().trim().equals("")) {
                    flag = false;
                }

                if (flag) {
                    // Run the POST, etc.
                    HashMap<String, String> stateData = new HashMap<String, String>();
                    stateData.put("name", state.getText().toString());
                    stateData.put("population", population.getText().toString());
                    stateData.put("cost_of_living", costOfLivingIndex.getText().toString());
                    stateData.put("median_income", medianIncome.getText().toString());
                    stateData.put("largest_employer", largestEmployer.getText().toString());

                    AsyncHttpPost asyncHttpPostNewState = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            // Get the new State ID and pass it into the intent to the next activity
//                            JSONObject newStateObj;
//
//                            try {
//                                newStateObj = new JSONObject(output.toString());

                            Intent goToSenators = new Intent(getApplicationContext(), AddSenatorsActivity.class);
                            goToSenators.putExtra("stateId", output);
                            startActivity(goToSenators);
//
//                              goToSenators.putExtra("stateId", newStateObj.get("key").toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            Toast successToast = Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT);
                            successToast.show();
                        }
                    }, stateData);
                    asyncHttpPostNewState.execute("http://senators-1208.appspot.com/state");


                } else {
                    validateToast.show();
                }

                break;
            default:
                break;
        }

    }
}
