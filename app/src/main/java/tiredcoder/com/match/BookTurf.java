package tiredcoder.com.match;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookTurf extends AppCompatActivity {
    Button bookbutton;
    int mYear,mMonth,mDay;
    EditText yourEditText;
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentslayout);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000'>"+"Book Turf</font>"));
     //   bookbutton=findViewById(R.id.book);
   /*     bookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setContentView(R.layout.selectdate);
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

                                mDay = selectedday;
                                mMonth = selectedmonth;
                                mYear = selectedyear;
                            }
                        }, mYear, mMonth, mDay);
                        //mDatePicker.setTitle("Select date");
                        mDatePicker.getDatePicker().setMinDate((System.currentTimeMillis() - 1000));

                        mDatePicker.show();
                    }
                }); */

         //   Button button=findViewById(R.id.bookbutton);
         //   button.setOnClickListener(new View.OnClickListener() {
           //     @Override
           //     public void onClick(View v) {
               //     Intent intent=new Intent(BookTurf.this,Test.class);
          //         // startActivity(intent);
          //      }
          //  });
            }
      //  });
    }
//}
