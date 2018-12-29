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
import android.widget.TextView;

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

public class ShowBookings extends AppCompatActivity {
    TextView turfname, booking_date, slot, paymentstatus, amount;
    Button next, back;
    JSONObject jsonObject;
    int indexofpost = 0;
    int bookingsize;
    Bookingclass[] bookings;

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
        }

        protected String doInBackground(String... args) {

            try {
                String link = "http://192.168.1.101/myfiles/userbookings.php";
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
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            // dialog.dismiss();
            Log.i("jsonfile", jsonObject.toString());
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

                }
                Log.i("sizeofarray", String.valueOf(jsonArray.length()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            back.setVisibility(View.GONE);
            turfname.setText("Turf Name : " + bookings[indexofpost].getTurfname());
            slot.setText("Slot :" + bookings[indexofpost].getSlot());
            paymentstatus.setText(bookings[indexofpost].getPaymentstatus());
            amount.setText(bookings[indexofpost].getAmount());
            booking_date.setText(bookings[indexofpost].getBookingdate());

            //  new ImageLoader("http://192.168.1.103/Turf/img/"+posts[indexofpost].getImagename(),imageView).execute();
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    indexofpost++;
                    turfname.setText("Turf Name : " + bookings[indexofpost].getTurfname());
                    slot.setText("Slot :" + bookings[indexofpost].getSlot());
                    paymentstatus.setText(bookings[indexofpost].getPaymentstatus());
                    amount.setText(bookings[indexofpost].getAmount());
                    booking_date.setText(bookings[indexofpost].getBookingdate());

                    if (indexofpost > 0)
                        back.setVisibility(View.VISIBLE);
                    if (indexofpost + 1 >= bookingsize)
                        next.setVisibility(View.GONE);
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexofpost--;
                    turfname.setText("Turf Name : " + bookings[indexofpost].getTurfname());
                    slot.setText("Slot :" + bookings[indexofpost].getSlot());
                    paymentstatus.setText(bookings[indexofpost].getPaymentstatus());
                    amount.setText(bookings[indexofpost].getAmount());
                    booking_date.setText(bookings[indexofpost].getBookingdate());
                    if (indexofpost < bookingsize)
                        next.setVisibility(View.VISIBLE);
                    if (indexofpost <= 0)
                        back.setVisibility(View.GONE);
                }
            });
        }
    }
}