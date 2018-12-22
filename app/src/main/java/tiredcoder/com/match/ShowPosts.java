package tiredcoder.com.match;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

public class ShowPosts extends AppCompatActivity {
    TextView postedon,time,message;
    int indexofpost=0;
    PostClass[] posts;
    int postsize;
    Button next,back;
    @Override
protected  void onCreate(Bundle savedInstancestate)
{
    super.onCreate(savedInstancestate);
    setContentView(R.layout.myposts);
    postedon=findViewById(R.id.postedon);
    time=findViewById(R.id.timeofposting);
    message=findViewById(R.id.messageofpost);
    next=findViewById(R.id.Next);
    back=findViewById(R.id.Back);
    SharedPreferences prefs=getSharedPreferences("userinfo",MODE_PRIVATE);
    String name=prefs.getString("name","soja");
    Log.i("marja",name);
     new getPosts(this,name).execute();

}

    public class getPosts extends AsyncTask<String,String,String> {
        Context context;
        String name;
        JSONObject jsonObject;
        ProgressDialog dialog;
        getPosts(Context context,String name)
        {
            this.context=context;
            this.name=name;
        }
        protected  void onPreExecute()
        {
            dialog=new ProgressDialog(context);
            dialog.setIndeterminate(false);
            dialog.setMessage("Creating Account");
            dialog.show();

        }
        protected  String doInBackground(String ... args)
        {

            try
            {
                String link="http://192.168.1.104/myfiles/userposts.php";
                String data;
                data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");

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
                Log.i("Loggingjson",jsonObject.toString());
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
            dialog.dismiss();
            Log.i("jsonfile",jsonObject.toString());
            try {
                JSONArray jsonArray=jsonObject.getJSONArray("posts");
                postsize=jsonArray.length();
                 posts=new PostClass[postsize];
                 if(postsize>1)
                 {
                     next.setVisibility(View.VISIBLE);
                     back.setVisibility(View.VISIBLE);
                 }
                for(int i=0;i<postsize;i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);
                  //  Log.i("postdate",(String) object.get("date"));
                    posts[i]=new PostClass();
                    posts[i].setDate((String) object.get("date"));
                   Log.i("postdate",posts[i].getDate());
                    posts[i].setMessage((String) object.get("message"));
                   Log.i("postmessage",posts[i].getMessage());
                   posts[i].setTime((String) object.get("time"));
                   Log.i("postTime",posts[i].getTime());

                }
                Log.i("sizeofarray", String.valueOf(jsonArray.length()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            back.setVisibility(View.GONE);
            postedon.setText("Posted on : "+posts[indexofpost].getDate());
            time.setText("Time :"+posts[indexofpost].getTime());
            message.setText(posts[indexofpost].getMessage());
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    indexofpost++;
                    postedon.setText("Posted on : "+posts[indexofpost].getDate());
                    time.setText("Time :"+posts[indexofpost].getTime());
                    message.setText(posts[indexofpost].getMessage());
                    if(indexofpost>0)
                        back.setVisibility(View.VISIBLE);
                    if(indexofpost+1>=postsize)
                        next.setVisibility(View.GONE);
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexofpost--;
                    postedon.setText("Posted on : "+posts[indexofpost].getDate());
                    time.setText("Time :"+posts[indexofpost].getTime());
                    message.setText(posts[indexofpost].getMessage());
                    if(indexofpost<postsize)
                        next.setVisibility(View.VISIBLE);
                    if(indexofpost<=0)
                        back.setVisibility(View.GONE);
                }
            });
        }
    }

}
