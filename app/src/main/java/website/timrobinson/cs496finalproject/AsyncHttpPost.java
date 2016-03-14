package website.timrobinson.cs496finalproject;

import android.os.AsyncTask;

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
 * The Async series of events were inspired by the two following sources:
 *
 * https://gist.github.com/stanzheng/9467500
 *  - For the AsyncHttp[Post|Get|Delete|Put] classes, extending the AsyncTask class
 *
 * http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a/12575319#12575319
 *  - For using the result of the Async HTTP request in the activity.
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