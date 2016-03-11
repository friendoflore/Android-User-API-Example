package website.timrobinson.cs496finalproject;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by friendofdata on 3/11/16.
 */
public class AsyncHttpPut extends AsyncTask<String, String, String> {
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public AsyncHttpPut(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    String responseString;

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpput = new HttpPut(params[0]);

        try {
            HttpResponse response = httpclient.execute(httpput);
            responseString = EntityUtils.toString(response.getEntity());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}

