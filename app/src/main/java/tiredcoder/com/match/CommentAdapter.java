package tiredcoder.com.match;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentslayout, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        CommentClass comment=comments.get(position);
  //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        if(Constants.checknet(context))

            Picasso.get().load(Constants.ip+"img/"+comment.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);        holder.comment.setText(comment.getComment());
        holder.name.setText(comment.getName());
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }

}
