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
                                timeslotrecycler.setVisibility(View.VISIBLE);
                                String date=yourEditText.getText().toString();
                                new getTimeSlots(date,BookTurf.this).execute();
                                mDay = selectedday;
                                mMonth = selectedmonth;
                                mYear = selectedyear;
                            }
                        }, mYear, mMonth, mDay);
                        //mDatePicker.setTitle("Select date");
                        mDatePicker.getDatePicker().setMinDate((System.currentTimeMillis() - 1000));
                        timeslotrecycler=findViewById(R.id.recyclerforslot);
                        paylater=findViewById(R.id.paylater);
                        paylater.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < timeset.size(); i++) {
                                    String slot = timeset.get(i);

                                    SharedPreferences preferences = BookTurf.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                    String id = preferences.getString("id", "1");
                                    String number = preferences.getString("mobileno", null);
                                    String name = preferences.getString("name", null);
                                    String amount = "1200";
                                    String payment_status = "unpaid";
                                    String turfname = "Atletico Arena";
                                    String bookingdate = yourEditText.getText().toString();
                                    new book(slot, bookingdate, name, number, id, turfname, amount, payment_status).execute();
                                }
                                timeset.clear();
                                integerSet.clear();
                                new getTimeSlots(yourEditText.getText().toString(),BookTurf.this).execute();
                            }
                        });


                        mDatePicker.show();
                    }
                });
                Button button=findViewById(R.id.bookbutton);
            button.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   //     Intent intent=new Intent(BookTurf.this,Test.class);
                   // startActivity(intent);
                   if (timeset.size() == 0) {
                       Toast.makeText(getApplicationContext(), "Select time first", Toast.LENGTH_SHORT).show();
                   }
                   else{

                   Retrofit retrofit = new Retrofit.Builder()
                           .baseUrl(Api.BASE_URL)
                           .addConverterFactory(GsonConverterFactory.create())
                           .build();
                   final Paytm paytm = new Paytm(
                           Constants.M_ID,
                           Constants.CHANNEL_ID,
                           String.valueOf(integerSet.size()*1200)+".00",
                           Constants.WEBSITE,
                           Constants.CALLBACK_URL,
                           Constants.INDUSTRY_TYPE_ID
                   );
                   Api apiService = retrofit.create(Api.class);

                   Call<Checksum> call = apiService.getChecksum(
                           paytm.getmId(),
                           paytm.getOrderId(),
                           paytm.getCustId(),
                           paytm.getChannelId(),
                           paytm.getTxnAmount(),
                           paytm.getWebsite(),
                           paytm.getCallBackUrl(),
                           paytm.getIndustryTypeId()
                   );
                   call.enqueue(new Callback<Checksum>() {
                       @Override
                       public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                           //once we get the checksum we will initiailize the payment.
                           //the method is taking the checksum we got and the paytm object as the parameter
                           PaytmPGService Service = PaytmPGService.getStagingService();
                           HashMap<String, String> parammap = new HashMap<>();
                           HashMap<String, String> paramMap = new HashMap<>();
                           paramMap.put("MID", Constants.M_ID);
                           paramMap.put("ORDER_ID", paytm.getOrderId());
                           paramMap.put("CUST_ID", paytm.getCustId());
                           paramMap.put("CHANNEL_ID", paytm.getChannelId());
                           paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
                           paramMap.put("WEBSITE", paytm.getWebsite());
                           paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
                           paramMap.put("CHECKSUMHASH", response.body().getChecksumHash());
                           paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
                           PaytmOrder order = new PaytmOrder(paramMap);
                           Service.initialize(order, null);

                           //finally starting the payment transaction
                           Service.startPaymentTransaction(BookTurf.this, true, true, new PaytmPaymentTransactionCallback() {
                               @Override
                               public void onTransactionResponse(Bundle inResponse) {
                                   String string = inResponse.toString();
                                   int index = string.indexOf("RESPMSG=");
                                   String reseponse = string.substring(index, string.length() - 2);
                                   Log.i("Response", reseponse);
                                   if (reseponse.equals("RESPMSG=Txn Success")) {
                                       for (int i = 0; i < timeset.size(); i++) {
                                           String slot = timeset.get(i);

                                           SharedPreferences preferences = BookTurf.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                           String id = preferences.getString("id", "1");
                                           String number = preferences.getString("mobileno", null);
                                           String name = preferences.getString("name", null);
                                           String amount = "1200";
                                           String payment_status = "paid";
                                           String turfname = "Atletico Arena";
                                           String bookingdate = yourEditText.getText().toString();
                                           new book(slot, bookingdate, name, number, id, turfname, amount, payment_status).execute();
                                       }
                                       timeset.clear();
                                       integerSet.clear();
                                       new getTimeSlots(yourEditText.getText().toString(),BookTurf.this).execute();
                                   }

                               }

                               @Override
                               public void networkNotAvailable() {

                               }

                               @Override
                               public void clientAuthenticationFailed(String inErrorMessage) {

                               }

                               @Override
                               public void someUIErrorOccurred(String inErrorMessage) {

                               }

                               @Override
                               public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

                               }

                               @Override
                               public void onBackPressedCancelTransaction() {

                               }

                               @Override
                               public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

                               }
                           });
                       }

                       @Override
                       public void onFailure(Call<Checksum> call, Throwable t) {

                       }
                   });
               }
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

            try
            {
                String link=Constants.ip+"myfiles/timeslots.php";
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
    public class book extends AsyncTask<String,String,String>
    {
        String slot,bookingdate,name,number,id,turfname,amount,payment;

        public book(String slot, String bookingdate, String name, String number, String id, String turfname, String amount, String payment) {
            this.slot = slot;
            this.bookingdate = bookingdate;
            this.name = name;
            this.number = number;
            this.id = id;
            this.turfname = turfname;
            this.amount = amount;
            this.payment = payment;
        }
        protected void onPreExecute() {

        }
        protected String doInBackground(String... args) {
            try {
                String link = Constants.ip+"myfiles/book.php";
                String data;
                data = URLEncoder.encode("slot", "UTF-8") + "=" + URLEncoder.encode(slot, "UTF-8");
                data += "&" + URLEncoder.encode("uid", "UTF-8") + "=" +
                        URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("turf_name", "UTF-8") + "=" +
                        URLEncoder.encode(turfname, "UTF-8");
                data += "&" + URLEncoder.encode("booking_date", "UTF-8") + "=" +
                        URLEncoder.encode(bookingdate, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("number", "UTF-8") + "=" +
                        URLEncoder.encode(number, "UTF-8");
                data += "&" + URLEncoder.encode("amount", "UTF-8") + "=" +
                        URLEncoder.encode(amount, "UTF-8");
                data += "&" + URLEncoder.encode("payment_status", "UTF-8") + "=" +
                        URLEncoder.encode(payment, "UTF-8");

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
                android.util.Log.i("loggingthiscomment", sb.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";

        }
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(BookTurf.this,"Booked successfully",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        integerSet.clear();
        timeset.clear();
        super.onBackPressed();
    }

}
