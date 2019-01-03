package tiredcoder.com.match;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bookpage extends AppCompatActivity {
   SharedPreferences preferences;
   int total;
   int numberofslots;
   String email,mobile,name;
   TextView numberofslotsbooked,dateview,totalview,mobileview,nameview,emailview;
   CircleImageView imageView;
   Button paynow,paylater;
   String date;
   @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalbook);
        preferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        total=Integer.parseInt(getIntent().getStringExtra("total"));
        numberofslots=Integer.parseInt(getIntent().getStringExtra("slots"));
        date=getIntent().getStringExtra("date");
        email=preferences.getString("email",null);
        mobile=preferences.getString("mobileno",null);
        name=preferences.getString("name",null);
        imageView=findViewById(R.id.imageinfinalbook);
        numberofslotsbooked=findViewById(R.id.numberofslotsbooked);
        dateview=findViewById(R.id.dateinfinalbook);
        totalview=findViewById(R.id.totalinfinalbooking);
        emailview=findViewById(R.id.emailinfinalbook);
        mobileview=findViewById(R.id.mobileinfinalbook);
        nameview=findViewById(R.id.nameinfinalbook);
        dateview.setText(date);
        nameview.setText(name);
        mobileview.setText(mobile);
        emailview.setText(email);
        paylater=findViewById(R.id.paylaterinbook);
        paynow=findViewById(R.id.paynow);
        totalview.setText(String.valueOf(total));
        Picasso.get().load(Constants.ip+"img/"+preferences.getString("image",null)).into(imageView);
        numberofslotsbooked.setText(String.valueOf(numberofslots));
        paylater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < numberofslots; i++) {
                    String slot = BookTurf.timeset.get(i);

                    String id = preferences.getString("id", "1");
                    String number = preferences.getString("mobileno", null);
                    String name = preferences.getString("name", null);
                    String amount = "1200";
                    String payment_status = "unpaid";
                    String turfname = "Atletico Arena";
                    String bookingdate = date;
                    new book(slot, bookingdate, name, number, id, turfname, amount, payment_status).execute();
                }
                BookTurf.timeset.clear();
                BookTurf.integerSet.clear();
            //    new BookTurf.getTimeSlots(date),BookTurf.this).execute();
            }
        });
        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final Paytm paytm = new Paytm(
                        Constants.M_ID,
                        Constants.CHANNEL_ID,
                        String.valueOf(numberofslots*1200)+".00",
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
                        Service.startPaymentTransaction(Bookpage.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                String string = inResponse.toString();
                                int index = string.indexOf("RESPMSG=");
                                String reseponse = string.substring(index, string.length() - 2);
                                Log.i("Response", reseponse);
                                if (reseponse.equals("RESPMSG=Txn Success")) {
                                    for (int i = 0; i < numberofslots; i++) {
                                        String slot = BookTurf.timeset.get(i);

                                        String id = preferences.getString("id", "1");
                                        String number = preferences.getString("mobileno", null);
                                        String name = preferences.getString("name", null);
                                        String amount = "1200";
                                        String payment_status = "paid";
                                        String turfname = "Atletico Arena";
                                        String bookingdate = date;
                                        new book(slot, bookingdate, name, number, id, turfname, amount, payment_status).execute();
                                    }
                                    BookTurf.timeset.clear();
                                    BookTurf.integerSet.clear();
                              //      new BookTurf.getTimeSlots(date,BookTurf.this).execute();
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
        });
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
                String link = Constants.ip+"android/book.php";
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
            Toast.makeText(Bookpage.this,"Booked successfully",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Bookpage.this,Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("mobileno",mobile);
            intent.putExtra("pass",preferences.getString("password",null));
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    }


