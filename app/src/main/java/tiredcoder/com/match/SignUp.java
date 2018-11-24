package tiredcoder.com.match;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {
    EditText name,password,emailid,mobileno;
    Button signup;
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        emailid=findViewById(R.id.email);
        mobileno=findViewById(R.id.mobileno);
        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passw=password.getText().toString();
                String  mobile=mobileno.getText().toString();
                String email=emailid.getText().toString();
                String nam=name.getText().toString();

                new SignUpActivity(SignUp.this).execute(nam,email,mobile,passw);
            }
        });
    }

}
