package tiredcoder.com.match;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
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

public class  SigninActivity extends AsyncTask<String,String,String> {
    private Context context;
    private  int flag;
    private TextView bookingid,emailid;
    JSONObject jsonObject;
    String mobileno,pass;
    public SigninActivity(Context context,TextView bookingid,TextView emailid,int flag)
    {
        this.flag=flag;
        this.context=context;
        this.bookingid=bookingid;
        this.emailid=emailid;
    }
    protected  void onPreExecute()
    {

    }
    @Override
    protected String doInBackground(String... arg0)  {
         mobileno=(String) arg0[0];
         pass=arg0[1];
        String link=Constants.ip+"myfiles/login.php";
        String data= null;
        try {
            data = URLEncoder.encode("mobile_number","UTF-8")+"="+URLEncoder.encode(mobileno,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            data+="&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(pass, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URL url= null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn= null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoOutput(true);
        OutputStreamWriter wr= null;
        try {
            wr = new OutputStreamWriter(conn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            wr.write( data );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        String line = null;

        // Read Server Response
        assert reader != null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            jsonObject=new JSONObject(sb.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  sb.toString();

    }
    @Override
    protected void onPostExecute(String result){
     //   this.bookingid.setText("adssa");
     //   this.emailid.setText(result);
        int success=0;
        try {
            success=jsonObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(success==1)
        {
            try {
                int admin=jsonObject.getInt("admin");
                //    Intent
                Intent intent;
                if(admin==1) {
                    intent = new Intent(context, Admin.class);
                    context.startActivity(intent);
                }
                else {
                    intent = new Intent(context, Home.class);

       //             intent = new Intent(context, Admin.class);
                    intent.putExtra("mobileno", mobileno);
                    intent.putExtra("pass", pass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    context.startActivity(intent)
                    ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else
            Toast.makeText(context,"Wrong mobile or Id",Toast.LENGTH_SHORT).show();

    }
}
