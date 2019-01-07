package tiredcoder.com.match;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyViewHolder>{
    private ArrayList<Dayclass> days;
    private Context context;
    TimeSlotAdapter timeslotadapter;
    CircularRevealCardView selectedcard=null;
    ArrayList<timeslot> timeslots=new ArrayList<>();
    RecyclerView timeslotrecycler;
    Dialog dialog;
    JSONObject jsonObject;
    public  class  MyViewHolder extends RecyclerView.ViewHolder
    {
        public  TextView date,month,day;
        CircularRevealCardView cardView;

        public MyViewHolder( View view) {
            super(view);
          date=view.findViewById(R.id.realdate);
          month=view.findViewById(R.id.month);
          day=view.findViewById(R.id.day);
          cardView=view.findViewById(R.id.datecard);
        }



    }
    public DateAdapter(ArrayList<Dayclass> days, Context context, Dialog dialog)
    {
        this.days=days;
        this.context=context;
        this.dialog=dialog;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        final Dayclass day=days.get(position);
        //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        holder.date.setText(day.getDate());
        holder.month.setText(day.getMonth());
        holder.day.setText(day.getDay());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedcard==null)
                {
                    selectedcard=holder.cardView;
             //      selectedcard.setBackgroundColor(context.getResources().getColor(R.color.green));

                  //  selectedcard.setRadius(10.0f);
                }
                else
                {
                    selectedcard.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    selectedcard=holder.cardView;
            //        selectedcard.setBackgroundColor(context.getResources().getColor(R.color.green));
               //     selectedcard.
             //       selectedcard.setRadius(10.0f);


                }
                BookTurf.selectedDate=day.myformat();
                dialog.findViewById(R.id.invisibletime).setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.invisibleview).setVisibility(View.VISIBLE);
                new getTimeSlots(day.myformat(),dialog.getContext()).execute();
            }
        });
    }
    @Override
    public int getItemCount() {
        return days.size();
    }
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
            BookTurf.timeset.clear();
            BookTurf.integerSet.clear();

            try
            {
                Log.i("dateis",date);
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
                    String available=jsonArray.getJSONObject(i).getString("available");
                    String time=jsonArray.getJSONObject(i).getString("time_slot");
                    timeslot timeslot=new timeslot(Integer.parseInt(available),time);
                    timeslots.add(timeslot);
                }
                timeslotrecycler=dialog.findViewById(R.id.recyclerforslot);
                timeslotadapter=new TimeSlotAdapter(timeslots,context);
                int numberOfColumns = 2;
                RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(context,numberOfColumns);
                timeslotrecycler.setLayoutManager(mLayoutManager);
                timeslotrecycler.setVisibility(View.VISIBLE);

                timeslotrecycler.setItemAnimator(new DefaultItemAnimator());
                timeslotrecycler.setAdapter(timeslotadapter);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
