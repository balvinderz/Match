package tiredcoder.com.match;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class CreatePost extends AsyncTask<String,String,String> {
    Context context;
    PostClass postClass;
    JSONObject jsonObject;
    CreatePost(Context context,PostClass postClass)
    {
        this.context=context;
        this.postClass=postClass;
    }
    protected  void onPreExecute()
    {

    }
    protected  String doInBackground(String ... args)
    {

        try
        {
            String link="http://192.168.1.101/myfiles/post.php";
            String data;

            data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(postClass.getName(),"UTF-8");
            data+="&" + URLEncoder.encode("message", "UTF-8") + "=" +
                    URLEncoder.encode(postClass.getMessage(), "UTF-8");
            data+="&" + URLEncoder.encode("mobile", "UTF-8") + "=" +
                    URLEncoder.encode(postClass.getMobileno(), "UTF-8");
            data+="&" + URLEncoder.encode("booking_id", "UTF-8") + "=" +
                    URLEncoder.encode(postClass.getBooking_id(), "UTF-8");
            data+="&" + URLEncoder.encode("date", "UTF-8") + "=" +
                    URLEncoder.encode(postClass.getDate(), "UTF-8");
            data+="&" + URLEncoder.encode("image", "UTF-8") + "=" +
                    URLEncoder.encode(postClass.getImagename(), "UTF-8");
            data+="&" + URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(postClass.getId()), "UTF-8");
            URL url=new URL(link);
            URLConnection connection=url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();
            BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String line=null;
            while ((line=br.readLine())!=null)
            {
                sb.append(line);
                break;
            }
            jsonObject=new JSONObject(sb.toString());
            Log.i("Loggingjson",jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }
    @Override
    protected void onPostExecute(String result){
       // dialog.dismiss();
        int success= 0;
        try {
            success = jsonObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(success==1)
        {
            Toast.makeText(context,"Posted Succesffuly Successfully",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(context,"Email Id or Mobile No already exists",Toast.LENGTH_SHORT).show();

        }
    }
}
