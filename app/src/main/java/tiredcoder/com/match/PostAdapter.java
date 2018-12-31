package tiredcoder.com.match;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private ArrayList<PostClass> posts;
    private Context context;
    Dialog dialog;
    ArrayList<CommentClass> comments;
    RecyclerView commentsrecylerview;
    CommentAdapter mAdapter;
    private  Home home;
    public  class  MyViewHolder extends RecyclerView.ViewHolder
    {
        public CircleImageView imageView;
        public TextView postmessage,name,date;
        Button comments,mobileno;
        public  MyViewHolder(View view)
        {
            super(view);
            imageView=view.findViewById(R.id.imageofpost);
            postmessage=view.findViewById(R.id.postmessage);
            name=view.findViewById(R.id.nameofposter);
            date=view.findViewById(R.id.date);
            comments=view.findViewById(R.id.commentsbutton);
            mobileno=view.findViewById(R.id.contactno);

        }

    }

    public PostAdapter( ArrayList<PostClass> posts, Context context,Home home) {
        this.posts = posts;
        this.context = context;
        this.home=home;
        comments=new ArrayList<>();
    }
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.otherposts, parent, false);

        return new PostAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final PostAdapter.MyViewHolder holder, int position)
    {
        final PostClass post=posts.get(position);
        //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        new ImageLoader(Constants.ip+"Turf/img/"+post.getImagename(),holder.imageView).execute();
        holder.date.setText(post.getDate());
        holder.name.setText(post.getName());
        holder.postmessage.setText(post.getMessage());
       holder.mobileno.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final Dialog dialog=new Dialog(context);
               dialog.setContentView(R.layout.mobileno);
               TextView name=dialog.findViewById(R.id.nameinmobile);
               name.setText("Contact "+post.getName());
               TextView mobile=dialog.findViewById(R.id.realmobileno);
               mobile.setText("Mobile Number :"+post.getMobileno());
               Button button=dialog.findViewById(R.id.buttonclose);
               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });
                dialog.show();
           }
       });
    holder.comments.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
    dialog=new Dialog(context);
    dialog.setContentView(R.layout.recyclerforcomments);
    commentsrecylerview=dialog.findViewById(R.id.recyclerforcomments);

            new loadComments(dialog.getContext(), post.getId()).execute();
            dialog.show();
                   }
    });
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }
    @SuppressLint("StaticFieldLeak")
    public class loadComments extends AsyncTask<String, String, String> {

        Context context;
        int postid;
        JSONObject jsonObject;

        loadComments(Context context, int postid) {
            this.context = context;
            this.postid = postid;
        }

        protected void onPreExecute() {

        }

        protected String doInBackground(String... args) {
            try {
                String link = Constants.ip+"myfiles/comments.php";
                String data;
                data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(postid), "UTF-8");
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                jsonObject = new JSONObject(sb.toString());
                Log.i("Loggingjson", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            comments.clear();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("comments");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CommentClass comment = new CommentClass();
                    comment.setComment(jsonObject.getString("comment"));
                    comment.setCommenterid(Integer.parseInt(jsonObject.getString("commentid")));
                    comment.setName(jsonObject.getString("name"));
                    comment.setImage(jsonObject.getString("image"));
                    comment.setPostid(Integer.parseInt(jsonObject.getString("postid")));
                    comments.add(comment);

                }
                if (comments.size() == 0) {
                    commentsrecylerview.setVisibility(View.GONE);
                } else {
                    mAdapter = new CommentAdapter(comments, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    commentsrecylerview.setLayoutManager(mLayoutManager);
                    commentsrecylerview.setItemAnimator(new DefaultItemAnimator());

                    commentsrecylerview.setAdapter(mAdapter);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            final EditText comment = dialog.findViewById(R.id.textforcomment);
            ImageButton send = dialog.findViewById(R.id.postcomment);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comm = comment.getText().toString();
                    if (comm.length() == 0) {
                        comment.requestFocus();
                        comment.setError("Enter comment first");
                    } else {
                        comment.setText("");
                        new PostAdapter.postcomment(comm,postid).execute();

                     }
                }
            });
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class postcomment extends AsyncTask<String, String, String> {
        Context context;
        String comment;
        int postid;

        postcomment(String comment,int postid) {
            this.comment = comment;
            this.postid=postid;
        }

        postcomment() {
        }

        protected void onPreExecute() {

        }

        protected String doInBackground(String... args) {
            try {
                String link = Constants.ip+"myfiles/postcomment.php";
                String data;
                SharedPreferences prefs = home.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                String id = prefs.getString("id", "soja");
                data = URLEncoder.encode("commentid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("post_id", "UTF-8") + "=" +
                        URLEncoder.encode(String.valueOf(postid), "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(prefs.getString("name", "name"), "UTF-8");
                data += "&" + URLEncoder.encode("comment", "UTF-8") + "=" +
                        URLEncoder.encode(comment, "UTF-8");
                data += "&" + URLEncoder.encode("poster_id", "UTF-8") + "=" +
                        URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("image", "UTF-8") + "=" +
                        URLEncoder.encode(prefs.getString("image", "profile.png"), "UTF-8");
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                Log.i("loggingthiscomment", sb.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(home, "Comment Posted Successfully", Toast.LENGTH_SHORT).show();

            new PostAdapter.loadComments(dialog.getContext(), postid).execute();
        }
    }
}
