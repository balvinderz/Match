package tiredcoder.com.match;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class BookTurf extends AppCompatActivity {
    Button bookbutton;
    int mYear = 0, mMonth = 0, mDay = 0;
    EditText yourEditText;
    RecyclerView timeslotrecycler;
    ArrayList<timeslot> timeslots;
    Button paylater;
    int flag=1;
    public static String selectedDate;
    JSONObject jsonObject;
    Button secondbutton;
    String stringdate;
    MenuItem menuItem;
    Dialog mBottomSheetDialog;
    Date date;
    ArrayList<Dayclass> dates = new ArrayList<>();
    TimeSlotAdapter timeslotadapter;
    public static ArrayList<String> integerSet = new ArrayList<String>();
    public static ArrayList<String> timeset = new ArrayList<String>();
    DateAdapter dateAdapter;
    RecyclerView recyclerView;
    androidx.appcompat.widget.Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
//        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000'>"+"Book Turf</font>"));
        new gettingsomething().execute();
        bookbutton = findViewById(R.id.book);
        timeslots = new ArrayList<>();
        final androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarinbooking);
        setSupportActionBar(toolbar);


        bookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dates.clear();

                mBottomSheetDialog = new Dialog(BookTurf.this, R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(R.layout.dateandtime); // your custom view.
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                MaterialButton button = mBottomSheetDialog.findViewById(R.id.proceed);
                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //     Button button1=mBottomSheetDialog.findViewById(R.id.timeinnewlayout);
                //   button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                 date = new Date();
                Calendar cal = Calendar.getInstance();

                cal.setTime(date);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                 stringdate = sdf.format(date);
                Log.i("loggingdate", stringdate);
                String mon = returnMonth(stringdate);
                for (int i = 0; i < 6; i++) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        date = format.parse(stringdate);

                        final Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Dayclass dayclass = new Dayclass();
                        dayclass.setFulldate(stringdate);
                        stringdate = format.format(calendar.getTime());
                        dayclass.setDay(date.toString().substring(0, 3));
                        dayclass.setMonth(date.toString().substring(4, 7));
                        dayclass.setDate(date.toString().substring(8, 10));
                        dayclass.display();
                        dates.add(dayclass);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.i("date", date.toString());
                }
                Collections.reverse(dates);
                recyclerView = mBottomSheetDialog.findViewById(R.id.recyclerfordate);
                dateAdapter = new DateAdapter(dates, BookTurf.this, mBottomSheetDialog);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BookTurf.this, LinearLayoutManager.HORIZONTAL, true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                recyclerView.setAdapter(dateAdapter);
                mBottomSheetDialog.show();
                Button proceed = mBottomSheetDialog.findViewById(R.id.proceed);
                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (integerSet.size() != 0) {
                            Intent intent = new Intent(BookTurf.this, Bookpage.class);
                            intent.putExtra("date", selectedDate);
                            intent.putExtra("slots", String.valueOf(integerSet.size()));
                            intent.putExtra("total", String.valueOf(integerSet.size() * 1200));
                            startActivity(intent);
                        } else
                            Toast.makeText(BookTurf.this, "Select time first", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    /*  bookbutton.setOnClickListener(new View.OnClickListener() {
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
       }); */
    }

    public class gettingsomething extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... voids) {

            String data;
            String link = Constants.ip + "android/profile.php";
            try {
                SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);

                data = URLEncoder.encode("mobile_number", "UTF-8") + "=" + URLEncoder.encode(preferences.getString("mobileno", null), "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(preferences.getString("password", null), "UTF-8");
                URL url = new URL(link);
                URLConnection urlConnection = url.openConnection();

                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    break;

                }
                Log.i("loggingthis", "balsadkasd\n" + sb.toString());

                jsonObject = new JSONObject(sb.toString());
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
        protected void onPostExecute(Void a) {

            try {
//                name.setText(jsonObject.getString("name"));
                //              email.setText(jsonObject.getString("email"));
                //         nameforposting=jsonObject.getString("name");
                SharedPreferences.Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();
                editor.putString("name", jsonObject.getString("name"));
                editor.putString("email", jsonObject.getString("email"));
                editor.putString("image", jsonObject.getString("image"));
                editor.putString("id", String.valueOf(jsonObject.getInt("id")));
                editor.putString("bookingid", jsonObject.getString("booking_id"));
                editor.putString("mobileno", jsonObject.getString("mobile"));
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getTimeSlots extends AsyncTask<String, String, String> {
        String date;
        Context context;

        public getTimeSlots(String date, Context context) {
            this.date = date;
            this.context = context;
        }

        protected String doInBackground(String... args) {
            timeset.clear();
            integerSet.clear();
            try {
                Log.i("dateis", date);

                String link = Constants.ip + "android/timeslots.php";
                String data;
                data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");

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
                android.util.Log.i("Loggingjson", jsonObject.toString());
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
            int success = 0;
            timeslots.clear();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("time_slots");
                for (int i = 0; i < jsonArray.length(); i++) {
                    int select = 0;
                    String time = jsonArray.getJSONObject(i).getString("time_slot");
                    timeslot timeslot = new timeslot(select, time);
                    timeslots.add(timeslot);
                }
                timeslotadapter = new TimeSlotAdapter(timeslots, context);
                //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                int numberOfColumns = 2;
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numberOfColumns);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {


            mBottomSheetDialog.dismiss();

// close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    String returnMonth(String date) {
        String month = date.substring(5, 7);
        Log.i("month", month);
        switch (month) {
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            default:
                return "Dec";
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        {
            getMenuInflater().inflate(R.menu.book, menu);
             menuItem = menu.findItem(R.id.profilemenu);

                 menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(BookTurf.this, Profile.class);
                    startActivity(intent);
                    return false;
                }
            });

            return true;
        }
    }


}
