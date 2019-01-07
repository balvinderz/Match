package tiredcoder.com.match;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class Profile extends Activity {
    RelativeLayout userbookings,userposts,accountsetting;
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        userposts=findViewById(R.id.posts);
        accountsetting=findViewById(R.id.accountsetting);
        userposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,ShowPosts.class);
                startActivity(intent);
            }
        });
        accountsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,AccountSetting.class);
                startActivity(intent);
            }
        });
        userbookings=findViewById(R.id.mybookings);
        userbookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,ShowBookings.class);
                startActivity(intent
                );
            }
        });
    }
}
