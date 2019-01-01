package tiredcoder.com.match;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingAdapter  extends RecyclerView.Adapter<BookingAdapter.MyViewHolder>{
    private ArrayList<Bookingclass> bookingclasses;
    private Context context;
    public  class  MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView turfname,name,status,Date,time,amount,number;
        public  MyViewHolder(View view)
        {
            super(view);
            turfname=view.findViewById(R.id.turf);
            name=view.findViewById(R.id.nameinbooking);
            status=view.findViewById(R.id.status);
            Date=view.findViewById(R.id.Date);
            time=view.findViewById(R.id.Time);
            amount=view.findViewById(R.id.amountforbooking);
            number=view.findViewById(R.id.numberinbooking);
        }

    }
    public BookingAdapter(ArrayList<Bookingclass> bookingclasses,Context context)
    {
        this.bookingclasses=bookingclasses;
        this.context=context;
    }
    @NonNull
    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookinglayout, parent, false);

        return new BookingAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.MyViewHolder holder, int position)
    {
        Bookingclass booking=bookingclasses.get(position);
        //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        holder.number.setText(booking.getNumber());
        holder.amount.setText(booking.getNumber());
        holder.name.setText(booking.getName());
        holder.status.setText(booking.getPaymentstatus());
        holder.turfname.setText(booking.getTurfname());
        holder.Date.setText(booking.getBookingdate());
        holder.time.setText(booking.getSlot());
    }
    @Override
    public int getItemCount() {
        return bookingclasses.size();
    }

}
