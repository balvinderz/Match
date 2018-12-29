package tiredcoder.com.match;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private ArrayList<PostClass> posts;
    private Context context;
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
        new ImageLoader("http://192.168.1.101/Turf/img/"+post.getImagename(),holder.imageView).execute();
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

    }
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
