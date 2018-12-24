package tiredcoder.com.match;

import android.app.ProgressDialog;
import android.content.Context;
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

public class SignUpActivity extends AsyncTask<String,String,String> {
    Context context;
    JSONObject jsonObject;
    ProgressDialog dialog;
    SignUpActivity(Context context)
    {
        this.context=context;
    }
    protected  void onPreExecute()
    {
        dialog=new ProgressDialog(context);
        dialog.setIndeterminate(false);
        dialog.setMessage("Creating Account");
        dialog.show();

    }
    protected  String doInBackground(String ... args)
    {
        String name=args[0];
        String emailid=args[1];
        String mobileno=args[2];
        String password=args[3];
        try
        {
            String link="http://192.168.1.103/myfiles/login.php";
            String data;
            data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");
            data+="&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(emailid, "UTF-8");
            data+="&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            data+="&" + URLEncoder.encode("mobile_number", "UTF-8") + "=" +
                    URLEncoder.encode(mobileno, "UTF-8");
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
      dialog.dismiss();
        int success= 0;
        try {
            success = jsonObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(success==1)
      {
          Toast.makeText(context,"Id created Successfully",Toast.LENGTH_SHORT).show();

      }
      else
        {
            Toast.makeText(context,"Email Id or Mobile No already exists",Toast.LENGTH_SHORT).show();

        }
    }
}
