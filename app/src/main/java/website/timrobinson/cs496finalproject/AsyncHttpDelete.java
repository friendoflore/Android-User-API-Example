package website.timrobinson.cs496finalproject;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by friendofdata on 3/11/16.
 */
public class AsyncHttpDelete extends AsyncTask<String, String, String> {
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public AsyncHttpDelete(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    String responseString;

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpDelete httpdelete = new HttpDelete(params[0]);

        try {
            HttpResponse response = httpclient.execute(httpdelete);
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
