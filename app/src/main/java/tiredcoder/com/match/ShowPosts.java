package tiredcoder.com.match;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.ArrayList;

public class ShowPosts extends AppCompatActivity {
    TextView postedon,time,message;
    int indexofpost=0;
    PostClass[] posts;
    int postsize;
    private CommentAdapter mAdapter;
Dialog commentdialog;
    ArrayList<CommentClass> comments=new ArrayList<>();
    Button next,back;
    RecyclerView recyclerView;
    Button commentbutton;
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
    commentbutton=findViewById(R.id.Comments);
    SharedPreferences prefs=getSharedPreferences("userinfo",MODE_PRIVATE);
    String name=prefs.getString("name","soja");
    Log.i("marja",name);

     new getPosts(this,name).execute();

}
    public  class loadComments extends AsyncTask<String,String,String>
    {
        Context context;
        int postid;
        JSONObject jsonObject;
        loadComments(Context context,int postid)
        {
            this.context=context;
            this.postid=postid;
        }
        protected  void onPreExecute()
        {

        }
        protected  String doInBackground(String ... args)
        {
            try
            {
                String link="http://192.168.1.103/myfiles/comments.php";
                String data;
                data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(String.valueOf(postid),"UTF-8");
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
            }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return "";
        }
        @Override
        protected  void onPostExecute(String result)
        {
            comments.clear();
            try
            {
                JSONArray jsonArray=jsonObject.getJSONArray("comments");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    CommentClass comment=new CommentClass();
                        comment.setComment(jsonObject.getString("comment"));
                        comment.setCommenterid(Integer.parseInt(jsonObject.getString("commentid")));
                        comment.setName(jsonObject.getString("name"));
                        comment.setImage(jsonObject.getString("image"));
                        comment.setPostid(Integer.parseInt(jsonObject.getString("postid")));
                        comments.add(comment);

                }
                if(comments.size()==0)
                {
                    TextView textView=findViewById(R.id.tempid);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    mAdapter = new CommentAdapter(comments, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(mAdapter);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            final EditText comment=commentdialog.findViewById(R.id.textforcomment);
            ImageButton send=commentdialog.findViewById(R.id.postcomment);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comm=comment.getText().toString();
                    if(comm.length()==0)
                    {
                        comment.requestFocus();
                        comment.setError("Enter comment first");
                    }
                    else
                    {
                        comment.setText("");
                        new postcomment(comm).execute();
                    }
                }
            });
        }

    }
public class postcomment extends AsyncTask<String,String,String>
{
    Context context;
    String comment;
    postcomment(String comment)
    {
        this.comment=comment;
    }
    postcomment()
    {}
    protected  void onPreExecute()
    {

    }
    protected  String doInBackground(String ... args)
    {
        try {
            String link = "http://192.168.1.103/myfiles/postcomment.php";
            String data;
            SharedPreferences prefs = getSharedPreferences("userinfo", MODE_PRIVATE);
            String id=prefs.getString("id","soja");
            data = URLEncoder.encode("commentid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("post_id", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(posts[indexofpost].getId()), "UTF-8");
            data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(prefs.getString("name","name"), "UTF-8");
            data += "&" + URLEncoder.encode("comment", "UTF-8") + "=" +
                    URLEncoder.encode(comment, "UTF-8");
            data += "&" + URLEncoder.encode("poster_id", "UTF-8") + "=" +
                    URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" +
                    URLEncoder.encode(prefs.getString("image","profile.png"), "UTF-8");
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
            Log.i("loggingthiscomment",sb.toString());

        }catch (Exception e)
        {
            e.printStackTrace();
        }
       return  "";

    }
    @Override
    protected  void onPostExecute(String result)
    {
        Toast.makeText(ShowPosts.this,"Comment Posted Successfully",Toast.LENGTH_SHORT).show();

        new loadComments(commentdialog.getContext(),posts[indexofpost].getId()).execute();
    }
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
                String link="http://192.168.1.103/myfiles/userposts.php";
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
                   posts[i].setId(object.getInt("id"));

                }
                Log.i("sizeofarray", String.valueOf(jsonArray.length()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            commentbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     commentdialog=new Dialog(ShowPosts.this);
                    Context  context=ShowPosts.this;
                    commentdialog.setContentView(R.layout.recyclerforcomments);
                    recyclerView=commentdialog.findViewById(R.id.recyclerforcomments);


                    new loadComments(commentdialog.getContext(),posts[indexofpost].getId()).execute();
                    commentdialog.show();
                }
            });
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
