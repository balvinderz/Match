package tiredcoder.com.match;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{
    private ArrayList<CommentClass> comments;
    private Context context;
    public  class  MyViewHolder extends RecyclerView.ViewHolder
    {
        public CircleImageView imageView;
        public TextView comment,name;
        public  MyViewHolder(View view)
        {
            super(view);
            imageView=view.findViewById(R.id.image);
            comment=view.findViewById(R.id.comment);
            name=view.findViewById(R.id.nameofcommenter);
        }

    }
    public CommentAdapter(ArrayList<CommentClass> comments,Context context)
    {
            this.comments=comments;
            this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentslayout, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,int position)
    {
        CommentClass comment=comments.get(position);
  //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        new ImageLoader(Constants.ip+"Turf/img/"+comment.getImage(),holder.imageView).execute();
        holder.comment.setText(comment.getComment());
        holder.name.setText(comment.getName());
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }

}
