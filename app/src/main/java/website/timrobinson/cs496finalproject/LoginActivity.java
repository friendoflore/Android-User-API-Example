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

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    EditText loginUsername;
    EditText loginPassword;

    Button loginButton;

    String loginUsernameSubmit = "";
    String loginPasswordSubmit = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        loginUsernameSubmit = loginUsername.getText().toString();
        loginPasswordSubmit = loginPassword.getText().toString();

        String usernameEmptyToast = "Enter username";
        String passwordEmptyToast = "Enter password";

        final String[] userKey = new String[1];

        boolean flag = true;

        if (loginUsernameSubmit.trim().equals("")) {
            Toast usernameToast = Toast.makeText(getApplicationContext(), usernameEmptyToast, Toast.LENGTH_SHORT);
            usernameToast.show();
            flag = false;
        }

        if (loginPasswordSubmit.trim().equals("")) {
            Toast passwordToast = Toast.makeText(getApplicationContext(), passwordEmptyToast, Toast.LENGTH_SHORT);
            passwordToast.show();
            flag = false;
        }

        if (flag) {
            // Async POST to see if username matches password
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("username", loginUsernameSubmit);
            data.put("password", loginPasswordSubmit);

            AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {

                @Override
                public void processFinish(String output) {
                    userKey[0] = output;
                    Toast testToast = Toast.makeText(getApplicationContext(), userKey[0], Toast.LENGTH_SHORT);
                    testToast.show();

                    Intent goToLoggedIn = new Intent(getApplicationContext(), UserActivity.class);
                    goToLoggedIn.putExtra("USER_ID", userKey[0]);
                    startActivity(goToLoggedIn);
                }
            }, data);
            asyncHttpPost.execute("http://user-api-1246.appspot.com/user/search");


        }


    }
}
