package website.timrobinson.cs496finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button createButton;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createButton = (Button) findViewById(R.id.create);
        loginButton = (Button) findViewById(R.id.login);

        createButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.create:
                // Go to CreateActivity

                Intent goToCreateActivity = new Intent(getApplicationContext(), CreateUserActivity.class);
                startActivity(goToCreateActivity);
                break;
            case R.id.login:
                // Go to LoginActivity

                Intent goToLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToLoginActivity);
                break;
            default:
                break;
        }

    }
}
