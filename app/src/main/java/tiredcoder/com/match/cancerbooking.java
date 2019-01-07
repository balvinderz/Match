package tiredcoder.com.match;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class cancerbooking extends AsyncTask<String,String,String> {
    Context context;
    String id;

    public cancerbooking(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
       // super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
    }

    @Override
    protected String doInBackground(String... args) {

        try {
            String link = Constants.ip + "android/cancel.php";
            String data;
            data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                break;
            }
           JSONObject jsonObject = new JSONObject(sb.toString());
            Log.i("Loggingjson", jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();

        }
        return "";

    }

}
