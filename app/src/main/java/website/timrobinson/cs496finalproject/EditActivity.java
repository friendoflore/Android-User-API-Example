package website.timrobinson.cs496finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity implements OnClickListener{

    TextView username;
    EditText description;
    Button submitUpdate;

    String userKey;
    String usernameText;
    String descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        username = (TextView) findViewById(R.id.username);
        description = (EditText) findViewById(R.id.description);
        submitUpdate = (Button) findViewById(R.id.submitUpdate);

        submitUpdate.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userKey = extras.getString("USER_ID");
            usernameText = extras.getString("username");
            descriptionText = extras.getString("description");
        }

        username.setText(usernameText);
        description.setText(descriptionText, EditText.BufferType.EDITABLE);

        // We need the user data for this one and then we'll do a POST request to "../edit"
    }

    @Override
    public void onClick(View v) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("user_id", userKey);
        data.put("description", description.getText().toString());

        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHttpPost.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                //userKey[0] = output;
                Toast testToast = Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT);
                testToast.show();

                finish();
                return;
            }
        }, data);
        asyncHttpPost.execute("http://user-api-1246.appspot.com/edit");
    }
}
