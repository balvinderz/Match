package tiredcoder.com.match;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
EditText mobileno,password;
TextView bookingid,emailid;
Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mobileno=findViewById(R.id.mobileno);
        password=findViewById(R.id.password);
        emailid=findViewById(R.id.emailid);
        bookingid=findViewById(R.id.bookingid);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passw=password.getText().toString();
                String  mobile=mobileno.getText().toString();
                new SigninActivity(MainActivity.this,bookingid,emailid,1).execute(mobile,passw);
            }
        });
        signup=findViewById(R.id.createacount);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });
    }
}
