package tiredcoder.com.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {
    TextView userposts,accountsetting;
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
    }
}
