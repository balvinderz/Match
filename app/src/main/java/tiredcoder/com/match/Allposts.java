package tiredcoder.com.match;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Allposts extends AsyncTask<String,String,String> {
    JSONObject jsonObject;
    Context context;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    Home home;
    public Allposts(Context context, RecyclerView recyclerView, PostAdapter postAdapter,Home home) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.postAdapter = postAdapter;
        this.home=home;
    }

    @Override
    protected  void onPreExecute()
    {

    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String link = "http://192.168.1.101/myfiles/allposts.php";


            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                break;
            }
            Log.i("loggingthisposts", "marja"+sb.toString());
            jsonObject = new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected  void onPostExecute(String result)
    {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("posts");
          int  postsize = jsonArray.length();
           PostClass[] posts = new PostClass[postsize];

            for (int i = 0; i < postsize; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                //  Log.i("postdate",(String) object.get("date"));
                posts[i] = new PostClass();
                posts[i].setDate((String) object.get("date"));
                Log.i("postdate", posts[i].getDate());
                posts[i].setMessage((String) object.get("message"));
                Log.i("postmessage", posts[i].getMessage());
                posts[i].setName((String) object.get("name"));
                posts[i].setTime((String) object.get("time"));
                Log.i("postTime", posts[i].getTime());
                posts[i].setMobileno((String) object.get("mobile"));
                posts[i].setId(object.getInt("id"));
                posts[i].setImagename((String)object.getString("image"));

            }
            ArrayList<PostClass> arrayList=new ArrayList<>(Arrays.asList(posts));
            postAdapter=new PostAdapter(arrayList,context,home);
            RecyclerView.LayoutManager mLayoutManager = new CustomGridLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(postAdapter);
            Log.i("sizeofarray", String.valueOf(jsonArray.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
