package tiredcoder.com.match;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SigninActivity extends AsyncTask<String,String,String> {
    private Context context;
    private  int flag;
    private TextView bookingid,emailid;
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
        String mobileno=(String) arg0[0];
        String pass=arg0[1];
        String link="http://192.168.1.100/myfiles/login.php";
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
return  sb.toString();

    }
    @Override
    protected void onPostExecute(String result){
        this.bookingid.setText("adssa");
        this.emailid.setText(result);
    }
}
