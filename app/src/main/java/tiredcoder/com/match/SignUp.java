package tiredcoder.com.match;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    EditText name,password,emailid,mobileno,username;
    Button signup;
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        emailid=findViewById(R.id.email);
        username=findViewById(R.id.username);
        mobileno=findViewById(R.id.mobileno);
        signup=findViewById(R.id.signup);
//        getActionBar().setTitle(Html.fromHtml("<font color='#0000FF'>SignUp</font>") + "tittle");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passw=password.getText().toString();
                String  mobile=mobileno.getText().toString();
                String email=emailid.getText().toString();
                String nam=name.getText().toString();
                String user=username.getText().toString();
                if(validate()==1)
                    if(Constants.checknet(SignUp.this))
                        new SignUpActivity(SignUp.this).execute(nam,email,mobile,passw,user);
            }
        });
    }
    int validate()
    {
        if(name.getText().toString().equals(""))
        {
            name.setError("Enter name first");
            name.requestFocus();
            return -1;
        }
        else
            if(!validemail(emailid.getText().toString()))
            {
                emailid.setError("Enter valid email id ");
                emailid.requestFocus();
                return  -1;
            }
            else
                if(username.getText().toString().equals(""))
                {
                    username.setError("Username cannot be empty");
                    username.requestFocus();
                    return -1;
                }
        else
            if(mobileno.getText().length()!=10)
            {
                mobileno.setError("Inavalid Phone number");
                mobileno.requestFocus();
                return  -1;
            }
            else
                if(password.getText().length()<8)
                {
                    password.setError("Password length should be greater than or equal to 8");
                    password.requestFocus();
                    return -1;
                }
                return 1;
    }
    boolean validemail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
