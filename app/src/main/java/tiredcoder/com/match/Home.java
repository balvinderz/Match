package tiredcoder.com.match;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    EditText yourEditText;
    int mYear,mMonth,mDay;
    SharedPreferences sharedPreferences;
    PostAdapter postAdapter;
    SharedPreferences.Editor editor;
    Button postbutton;
    RecyclerView recyclerView;
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);

        editor=getSharedPreferences("userinfo", MODE_PRIVATE).edit();

        editor.putString("name",sharedPreferences.getString("name",null));
        editor.putString("email",sharedPreferences.getString("email",null));
        editor.putString("password",sharedPreferences.getString("password",null));
        editor.apply();
        new Allposts(this,recyclerView,postAdapter,Home.this).execute();


    }
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //imageView=findViewById(R.id.profileimage);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        postbutton=findViewById(R.id.PostButton);
//    getSupportActionBar().setTitle(Html.fromHtml("<font color='#000'>"+"Home</font>"));
        mobileno=getIntent().getStringExtra("mobileno");
     //   Log.i("mobileno",mobileno);
        recyclerView=findViewById(R.id.recyclerforposts);
       //  sharedPreferences=getPreferences(MODE_PRIVATE);
         editor=getSharedPreferences("userinfo", MODE_PRIVATE).edit();
        pass=getIntent().getStringExtra("pass");
        editor.putString("password",pass);
        editor.putString("mobileno",mobileno);
        editor.apply();
        bookturf=findViewById(R.id.bookturf);


//        Log.i("mobileno",pass);
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
        if(Constants.checknet(Home.this))

            new Allposts(this,recyclerView,postAdapter,Home.this).execute();
        if(Constants.checknet(Home.this))
            new  gettingsomething().execute();
        postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Home.this);
                dialog.setContentView(R.layout.postlayout);
                final TextView message=dialog.findViewById(R.id.message);
                dialog.setTitle("Post");
                 yourEditText = (EditText) dialog.findViewById(R.id.datepick);
                yourEditText.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // To show current date in the datepicker
                        Calendar mcurrentDate = Calendar.getInstance();
                        mYear = mcurrentDate.get(Calendar.YEAR);
                            mMonth = mcurrentDate.get(Calendar.MONTH);
                             mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                        final DatePickerDialog mDatePicker = new DatePickerDialog(Home.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                                String myFormat = "dd/MM/yy"; //Change as you need
                                SimpleDateFormat sdf;
                                sdf = new SimpleDateFormat(myFormat);
                                yourEditText.setText(sdf.format(myCalendar.getTime()));

                                mDay = selectedday;
                                mMonth = selectedmonth;
                                mYear = selectedyear;
                            }
                        }, mYear, mMonth, mDay);
                        //mDatePicker.setTitle("Select date");
                        mDatePicker.getDatePicker().setMinDate((System.currentTimeMillis() - 1000));



                        mDatePicker.show();
                    }
                });
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

                        SharedPreferences prefs = getSharedPreferences("userinfo", MODE_PRIVATE);

                        if (message.getText().toString().equals("")) {
                            message.setError("Write something first");
                            message.requestFocus();
                        }
                        else
                            if (yourEditText.getText().toString().equals(""))
                            {
                                yourEditText.setError("Select Date first");
                            Toast.makeText(Home.this,"Select Date first",Toast.LENGTH_SHORT).show();
                            }
                                else {
                            String messageforpost = message.getText().toString();

                            String name = prefs.getString("name", null);

                           // String strDate = dateFormat.format(d);
                            String image = prefs.getString("image", "soja");
                            post.setMessage(message.getText().toString());
                            post.setName(name);
                            post.setMobileno(mobilenoforposting);
                            post.setBooking_id(bookingid);
                            post.setDate(yourEditText.getText().toString());
                            post.setImagename(image);
                            post.setId(Integer.parseInt(prefs.getString("id", null)));
                                if(Constants.checknet(Home.this))

                            new CreatePost(Home.this, post, recyclerView, postAdapter, Home.this).execute();
                            dialog.dismiss();
                        }
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
            String link=Constants.ip+"android/profile.php";
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
//                name.setText(jsonObject.getString("name"));
  //              email.setText(jsonObject.getString("email"));
                nameforposting=jsonObject.getString("name");
                editor.putString("name",jsonObject.getString("name"));
                editor.putString("email",jsonObject.getString("email"));
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
