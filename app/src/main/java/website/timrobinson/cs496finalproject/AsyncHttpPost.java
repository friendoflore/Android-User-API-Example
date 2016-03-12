package website.timrobinson.cs496finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by friendofdata on 3/10/16.
 */
public class AsyncHttpPost extends AsyncTask<String, String, String> {
    private HashMap<String, String> mData = null;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public AsyncHttpPost(AsyncResponse delegate, HashMap<String, String> data) {
        mData = data;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);

        String userkey = "";

        try {
            List<NameValuePair> postPairs = new ArrayList<NameValuePair>(2);

            Iterator<String> it = mData.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next();
                postPairs.add(new BasicNameValuePair(key, mData.get(key)));
            }

            httppost.setEntity(new UrlEncodedFormEntity(postPairs));

            HttpResponse response = httpclient.execute(httppost);

            String responseString = EntityUtils.toString(response.getEntity());
            JSONObject jsonResponse = new JSONObject(responseString);
            userkey = jsonResponse.get("key").toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userkey;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}