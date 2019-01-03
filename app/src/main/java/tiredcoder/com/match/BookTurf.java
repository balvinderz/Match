package tiredcoder.com.match;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookTurf extends AppCompatActivity  {
    Button bookbutton;
    int mYear=0,mMonth=0,mDay=0;
    EditText yourEditText;
    RecyclerView timeslotrecycler;
    ArrayList<timeslot> timeslots;
    Button paylater;
    JSONObject jsonObject;
    Button secondbutton;
    TimeSlotAdapter timeslotadapter;
    public  static ArrayList<String> integerSet=new ArrayList<String>();
    public  static ArrayList<String> timeset=new ArrayList<String>();


    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
//        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000'>"+"Book Turf</font>"));
       bookbutton=findViewById(R.id.book);
       timeslots=new ArrayList<>();
      bookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setContentView(R.layout.selectdate);
            secondbutton=findViewById(R.id.bookbutton);
                yourEditText = (EditText) findViewById(R.id.edittext);
                yourEditText.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // To show current date in the datepicker
                        Calendar mcurrentDate = Calendar.getInstance();
                         mYear = mcurrentDate.get(Calendar.YEAR);
                         mMonth = mcurrentDate.get(Calendar.MONTH);
                        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                        final DatePickerDialog mDatePicker = new DatePickerDialog(BookTurf.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);

                                String myFormat = "dd/MM/yy"; //Change as you need
                                SimpleDateFormat sdf;
                                sdf = new SimpleDateFormat(myFormat);
                                yourEditText.setText(sdf.format(myCalendar.getTime()));
                                TextView invisible=findViewById(R.id.selecttimeslot);
                                invisible.setVisibility(View.VISIBLE);
                                timeslotrecycler.setVisibility(View.VISIBLE);
                                String date=yourEditText.getText().toString();
                                if(Constants.checknet(BookTurf.this))
                                    new getTimeSlots(date,BookTurf.this).execute();
                                mDay = selectedday;
                                mMonth = selectedmonth;
                                mYear = selectedyear;
                            }
                        }, mYear, mMonth, mDay);
                        //mDatePicker.setTitle("Select date");
                        mDatePicker.getDatePicker().setMinDate((System.currentTimeMillis() - 1000));
                        timeslotrecycler=findViewById(R.id.recyclerforslot);



                        mDatePicker.show();
                    }
                });
                Button button=findViewById(R.id.bookbutton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (integerSet.size() != 0) {
                                Intent intent = new Intent(BookTurf.this, Bookpage.class);
                                intent.putExtra("date", yourEditText.getText().toString());
                                intent.putExtra("slots", String.valueOf(integerSet.size()));
                                intent.putExtra("total", String.valueOf(integerSet.size() * 1200));
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(BookTurf.this,"Select time first",Toast.LENGTH_SHORT).show();
                        }

                    });

            }
       });
    }
    @SuppressLint("StaticFieldLeak")
    public class getTimeSlots extends AsyncTask<String,String,String>
    {
        String date;
        Context context;

        public getTimeSlots(String date, Context context) {
            this.date = date;
            this.context = context;
        }
        protected  String doInBackground(String ... args)
        {
            timeset.clear();
            integerSet.clear();
            try
            {
                String link=Constants.ip+"android/timeslots.php";
                String data;
                data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");

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
                android.util.Log.i("Loggingjson",jsonObject.toString());
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
            timeslots.clear();
            try {
                JSONArray jsonArray=jsonObject.getJSONArray("time_slots");
                for(int i=0;i< jsonArray.length();i++)
                {
                    int select=0;
                    String time=jsonArray.getJSONObject(i).getString("time_slot");
                    timeslot timeslot=new timeslot(select,time);
                    timeslots.add(timeslot);
                }
                timeslotadapter=new TimeSlotAdapter(timeslots,context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                timeslotrecycler.setLayoutManager(mLayoutManager);
                timeslotrecycler.setItemAnimator(new DefaultItemAnimator());
                timeslotrecycler.setAdapter(timeslotadapter);
                secondbutton.setVisibility(View.VISIBLE);
                paylater.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        integerSet.clear();
        timeset.clear();
        super.onBackPressed();
    }

}
