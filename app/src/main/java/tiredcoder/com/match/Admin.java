package tiredcoder.com.match;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Admin extends AppCompatActivity {
RecyclerView recyclerView;
JSONObject jsonObject;
ArrayList<Bookingclass> bookingclass;
BookingAdapter bookingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin);
    recyclerView=findViewById(R.id.recyclerforbooking);
    bookingclass=new ArrayList<>();
    if(Constants.checknet(Admin.this))

    new getbookings().execute();
 }
public class getbookings extends AsyncTask<String,String,String>
{
    @Override
    protected String doInBackground(String... strings) {
        try
        {
            String link=Constants.ip+"android/getbookings.php";

            URL url=new URL(link);
            URLConnection connection=url.openConnection();
            connection.setDoOutput(true);

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("bookings");
            Log.i("loggintlength",String.valueOf(jsonArray.length()));
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object=jsonArray.getJSONObject(i);
                Bookingclass booking=new Bookingclass();
                booking.setSlot((String)object.get("slot"));
                booking.setTurfname((String)object.get("turf_name"));
                booking.setAmount((String) object.get("amount"));
                booking.setNumber((String)object.get("number"));
                booking.setName((String)object.get("name"));
                booking.setBookingdate((String)object.get("booking_date"));
                booking.setTime((String)object.get("booking_time"));
                booking.setPaymentstatus((String)object.get("payment_status"));
                Log.i("loggingdetailsturf","marja");
                booking.setId((String) object.get("id"));
                booking.setImage((String) object.get("image"));
                bookingclass.add(booking);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        bookingAdapter=new BookingAdapter(bookingclass,Admin.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Admin.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookingAdapter);
    }
}
}
