package tiredcoder.com.match;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetting extends AppCompatActivity {
    EditText password,email,name;
    Button button;
    int flag=0;
    SharedPreferences preferences;
    CircleImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        name=findViewById(R.id.changablename);
        password=findViewById(R.id.changablepassword);
        email=findViewById(R.id.changableemail);
        preferences   =getSharedPreferences("userinfo",MODE_PRIVATE);
        imageView=findViewById(R.id.changableimage);
        name.setText(preferences.getString("name",null));
        email.setText(preferences.getString("email",null));
        password.setText(preferences.getString("password",null));
        new ImageLoader("http://192.168.1.103/Turf/img/"+preferences.getString("image",null),imageView).execute();
        button=findViewById(R.id.changesettings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0) {
                    button.setText("Save");
                    name.setEnabled(true);
                    password.setEnabled(true);
                    email.setEnabled(true);
                    name.requestFocus();
                    flag=1;
                Drawable res=getResources().getDrawable(R.drawable.plus);
                    imageView.setImageDrawable(res);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            getIntent.setType("image/*");

                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");

                            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                            startActivityForResult(chooserIntent, 300);
                        }
                    });

                }
                else
                {
                    button.setText("Change");
                    name.setEnabled(false);
                    password.setEnabled(false);
                    email.setEnabled(false);
                    Drawable res=getResources().getDrawable(R.drawable.placeholder);
                    new ImageLoader("http://192.168.1.103/Turf/img/"+preferences.getString("image",null),imageView).execute();

                    imageView.setImageDrawable(res);
                    flag=0;
                }
            }
        });
    }

}
