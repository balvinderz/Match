package tiredcoder.com.match;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity {
   TextView email,name;
   String mobileno,pass;
   ImageView imageView;
   String bookingid,nameforposting,mobilenoforposting;
   JSONObject jsonObject;
    PostClass post=new PostClass();
    Button bookturf;
    Button profile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button postbutton;
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //imageView=findViewById(R.id.profileimage);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        postbutton=findViewById(R.id.PostButton);
    getSupportActionBar().setTitle(Html.fromHtml("<font color='#000'>"+"Home</font>"));
        mobileno=getIntent().getStringExtra("mobileno");
        Log.i("mobileno",mobileno);
       //  sharedPreferences=getPreferences(MODE_PRIVATE);
         editor=getSharedPreferences("userinfo", MODE_PRIVATE).edit();
        editor.putString("mobileno",mobileno);
        editor.putString("password",pass);
        editor.apply();
        bookturf=findViewById(R.id.bookturf);
        pass=getIntent().getStringExtra("pass");
        Log.i("mobileno",pass);
        bookturf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,BookTurf.class);
                startActivity(intent);
            }
        });
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,Profile.class);
                startActivity(intent);
            }
        });
        new  gettingsomething().execute();
        postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Home.this);
                dialog.setContentView(R.layout.postlayout);
                final TextView message=dialog.findViewById(R.id.message);

                dialog.setTitle("Post");
                final DatePicker datePicker=dialog.findViewById(R.id.date);
                datePicker.setMinDate((System.currentTimeMillis() - 1000));
                Button cancelbutton=dialog.findViewById(R.id.cancelbutton);
                cancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button okbutton=dialog.findViewById(R.id.okbutton);
                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String messageforpost=message.getText().toString();
                        String name=nameforposting;
                        int day=datePicker.getDayOfMonth();
                        int month=datePicker.getMonth()+1;
                        int year=datePicker.getYear();
                        Log.i("dateday",String.valueOf(day));

                        Log.i("datemonth",String.valueOf(month));
                        Log.i("dateyear",String.valueOf(year));

                        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        Date d=new Date(year,month,day);
                        String strDate=dateFormat.format(d);

                        post.setMessage(message.getText().toString());
                        post.setName(name);
                        post.setMobileno(mobilenoforposting);
                        post.setBooking_id(bookingid);
                        post.setDate(year+"-"+month+"-"+day);
                       new  CreatePost(Home.this,post).execute();
                       dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }
    private  class gettingsomething extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(Void... voids) {

            String data;
            String link="http://192.168.1.103/myfiles/profile.php";
            try {

                data = URLEncoder.encode("mobile_number","UTF-8")+"="+URLEncoder.encode(mobileno,"UTF-8");
                data+="&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(pass, "UTF-8");
                URL url=new URL(link);
                URLConnection urlConnection=url.openConnection();

                urlConnection.setDoOutput(true);
                OutputStreamWriter      wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write( data );
                wr.flush();

                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    break;

                }
                Log.i("loggingthis","balsadkasd\n"+sb.toString());

                jsonObject=new JSONObject(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void a){

            try {
                name.setText(jsonObject.getString("name"));
                email.setText(jsonObject.getString("email"));
                nameforposting=jsonObject.getString("name");
                editor.putString("name",jsonObject.getString("name"));
                editor.putString("email",jsonObject.getString("name"));
                editor.putString("image",jsonObject.getString("image"));
                bookingid=jsonObject.getString("booking_id");
                editor.putString("id",String.valueOf(jsonObject.getInt("id")));
                editor.putString("bookingid",bookingid);
                editor.apply();
                mobilenoforposting=jsonObject.getString("mobile");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
