package tiredcoder.com.match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowBookings extends AppCompatActivity {
    TextView turfname, booking_date, slot, paymentstatus, amount;
    Button next, back;
    JSONObject jsonObject;
    int indexofpost = 0;
    int bookingsize;
    Bookingclass[] bookings;
    Button pay;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.bookingdetails);
        SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        next = findViewById(R.id.Next);
        turfname = findViewById(R.id.turfname);
        booking_date = findViewById(R.id.bookingdate);
        slot = findViewById(R.id.slots);
        paymentstatus = findViewById(R.id.status);
        amount = findViewById(R.id.amount);
        back = findViewById(R.id.Back);
        pay = findViewById(R.id.paybutton);
        if(Constants.checknet(ShowBookings.this))

        new getBookings(preferences.getString("mobileno", null)).execute();
    }

    public class getBookings extends AsyncTask<String, String, String> {
        String number;

        public getBookings(String number) {
            this.number = number;
        }

        getBookings() {

        }

        protected void onPreExecute() {

            next.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {

            try {
                String link = Constants.ip + "android/userbookings.php";
                String data;
                data = URLEncoder.encode("number", "UTF-8") + "=" + URLEncoder.encode(number, "UTF-8");

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
                jsonObject = new JSONObject(sb.toString());
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

        @Override
        protected void onPostExecute(String result) {

            // dialog.dismiss();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("bookings");
                bookingsize = jsonArray.length();
                bookings = new Bookingclass[bookingsize];
                if (bookingsize > 1) {
                    next.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < bookingsize; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    //  Log.i("postdate",(String) object.get("date"));
                    bookings[i] = new Bookingclass();
                    bookings[i].setAmount((String) object.get("amount"));
                    //  Log.i("postdate", posts[i].getDate());
                    bookings[i].setBookingdate((String) object.get("booking_date"));
                    //  Log.i("postmessage", posts[i].getMessage());
                    bookings[i].setPaymentstatus((String) object.get("payment_status"));
                    //    Log.i("postTime", posts[i].getTime());
                    bookings[i].setTurfname((String) object.get("turf_name"));
                    bookings[i].setSlot((String) object.getString("slot"));
                    bookings[i].setId((String) object.get("id"));
                }
                Log.i("sizeofarray", String.valueOf(jsonArray.length()));
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            if(bookings==null)
            {
                LinearLayout layout=findViewById(R.id.layoutinbooking);
                layout.setVisibility(View.GONE);
                TextView textView=findViewById(R.id.nobookings);
                textView.setVisibility(View.VISIBLE);
            }
            else {
                back.setVisibility(View.GONE);
                turfname.setText("Turf Name : " + bookings[indexofpost].getTurfname());
                slot.setText("Slot :" + bookings[indexofpost].getSlot());
                paymentstatus.setText("Payment Status : " + bookings[indexofpost].getPaymentstatus());
                amount.setText("Amount : " + bookings[indexofpost].getAmount());
                booking_date.setText("Booking date : " + bookings[indexofpost].getBookingdate());
                if (bookings[indexofpost].getPaymentstatus().equals("unpaid")) {
                    pay.setVisibility(View.VISIBLE);
                }
            }
            //  new ImageLoader("http://192.168.1.103/Turf/img/"+posts[indexofpost].getImagename(),imageView).execute();
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    indexofpost++;
                    turfname.setText("Turf Name : " + bookings[indexofpost].getTurfname());
                    slot.setText("Slot :" + bookings[indexofpost].getSlot());
                    paymentstatus.setText("Payment Status : "+bookings[indexofpost].getPaymentstatus());
                    amount.setText("Amount : "+bookings[indexofpost].getAmount());
                    booking_date.setText("Booking date : "+bookings[indexofpost].getBookingdate());
                    if (bookings[indexofpost].getPaymentstatus().equals("unpaid")) {
                        pay.setVisibility(View.VISIBLE);
                    } else {
                        pay.setVisibility(View.GONE);
                    }
                    if (indexofpost > 0)
                        back.setVisibility(View.VISIBLE);
                    if (indexofpost + 1 >= bookingsize)
                        next.setVisibility(View.GONE);
                }
            });
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Api.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final Paytm paytm = new Paytm(
                            Constants.M_ID,
                            Constants.CHANNEL_ID,
                            String.valueOf(10) + ".00",
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
                            Service.startPaymentTransaction(ShowBookings.this, true, true, new PaytmPaymentTransactionCallback() {
                                @Override
                                public void onTransactionResponse(Bundle inResponse) {
                                    String string = inResponse.toString();
                                    int index = string.indexOf("RESPMSG=");
                                    String reseponse = string.substring(index, string.length() - 2);
                                    com.paytm.pgsdk.Log.i("Response", reseponse);
                                    if (reseponse.equals("RESPMSG=Txn Success")) {
                                    new changeStatus(bookings[indexofpost].getId()).execute();
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
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexofpost--;
                    turfname.setText("Turf Name : " + bookings[indexofpost].getTurfname());
                    slot.setText("Slot :" + bookings[indexofpost].getSlot());
                    paymentstatus.setText("Payment Status : "+bookings[indexofpost].getPaymentstatus());
                    amount.setText("Amount : "+bookings[indexofpost].getAmount());
                    booking_date.setText("Booking date : "+bookings[indexofpost].getBookingdate());
                    if (bookings[indexofpost].getPaymentstatus().equals("unpaid")) {
                        pay.setVisibility(View.VISIBLE);
                    } else {
                        pay.setVisibility(View.GONE);
                    }
                    if (indexofpost < bookingsize)
                        next.setVisibility(View.VISIBLE);
                    if (indexofpost <= 0)
                        back.setVisibility(View.GONE);
                }
            });
        }
    }

    public class changeStatus extends AsyncTask<String, String, String> {
        String postid;

        changeStatus(String postid) {
            this.postid = postid;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String link = Constants.ip + "android/changestatus.php";
                String data;
                Log.i("loggingid",postid);
                data = URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(postid, "UTF-8");

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

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(ShowBookings.this, "Payed Successfully", Toast.LENGTH_SHORT).show();
            bookings[indexofpost].setPaymentstatus("paid");
            paymentstatus.setText(bookings[indexofpost].getPaymentstatus());

            pay.setVisibility(View.GONE);

        }
    }
}