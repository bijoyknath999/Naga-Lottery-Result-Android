package nagalandlottery.result.daily.utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetChecker extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000); // Set the connection timeout to 3 seconds
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            // There is an internet connection
        } else {
            // There is no internet connection
        }
    }
}
