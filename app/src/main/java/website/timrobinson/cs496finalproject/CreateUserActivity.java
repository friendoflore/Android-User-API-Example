package website.timrobinson.cs496finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class CreateUserActivity extends AppCompatActivity implements OnClickListener {

    EditText username;
    EditText password;
    EditText rePassword;
    Button createUserButton;

    String usernameSubmit;
    String passwordSubmit;
    String passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        username = (EditText) findViewById(R.id.createUsername);
        password = (EditText) findViewById(R.id.createPassword);
        rePassword = (EditText) findViewById(R.id.createRetype);

        createUserButton = (Button) findViewById(R.id.submitCreateUser);

        createUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        usernameSubmit = username.getText().toString();
        passwordSubmit = password.getText().toString();
        passwordCheck = rePassword.getText().toString();

        String usernameEmptyToast = "Enter username";
        String passwordEmptyToast = "Enter password";
        String passwordCheckToast = "Passwords do not match";

        final String[] newUserKey = new String[1];

        boolean flag = true;

        // Validate user input
        if (usernameSubmit.trim().equals("")) {
            Toast usernameToast = Toast.makeText(getApplicationContext(), usernameEmptyToast, Toast.LENGTH_SHORT);
            usernameToast.show();
            flag = false;
        }

        if (!passwordSubmit.equals(passwordCheck)) {
            Toast passwordCheckAlert = Toast.makeText(getApplicationContext(), passwordCheckToast, Toast.LENGTH_SHORT);
            passwordCheckAlert.show();
            flag = false;
        }

        if (passwordSubmit.trim().equals("")) {
            Toast passwordToast = Toast.makeText(getApplicationContext(), passwordEmptyToast, Toast.LENGTH_SHORT);
            passwordToast.show();
            flag = false;
        }

        if (flag) {

            // Submit the created user to the API, use the response to get the key and go to logged
            // in, sending the user's key to the next activity
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("username", usernameSubmit);
            data.put("password", passwordSubmit);

            AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {

                @Override
                public void processFinish(String output) {

                    newUserKey[0] = output;

                    Intent goToLoggedIn = new Intent(getApplicationContext(), UserActivity.class);
                    goToLoggedIn.putExtra("USER_ID", newUserKey[0]);
                    startActivity(goToLoggedIn);
                    finish();

                }
            }, data);
            asyncHttpPost.execute("http://user-api-1246.appspot.com/user");
        }
    }
}