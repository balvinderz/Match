package tiredcoder.com.match;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        CircleImageView imageView;
        Button cancel;
        public  MyViewHolder(View view)
        {
            super(view);
            turfname=view.findViewById(R.id.turf);
            name=view.findViewById(R.id.nameinbooking);
            status=view.findViewById(R.id.status);
            Date=view.findViewById(R.id.Date);
            time=view.findViewById(R.id.Time);
            amount=view.findViewById(R.id.amountforbooking);
            cancel=view.findViewById(R.id.cancelbutton);
            number=view.findViewById(R.id.numberinbooking);
            imageView=view.findViewById(R.id.adminpic);
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
    public void onBindViewHolder(@NonNull final BookingAdapter.MyViewHolder holder, int position)
    {
        final Bookingclass booking=bookingclasses.get(position);
        //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        holder.number.setText("Mobile number : "+booking.getNumber());
        holder.amount.setText("Amount : "+booking.getAmount());
        holder.name.setText("Name : "+booking.getName());
        holder.status.setText("Payment Status : "+booking.getPaymentstatus());
        holder.turfname.setText("Turf name : "+booking.getTurfname());
        holder.Date.setText("Date : "+booking.getBookingdate());
        holder.time.setText("Time : "+booking.getSlot());
        if(holder.status.getText().toString().equals("Payment Status : unpaid"))
        {
            holder.cancel.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.cancel.setVisibility(View.GONE);
        }

        Picasso.get().load(Constants.ip+"img/"+booking.getImage()).into(holder.imageView);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new cancerbooking(context,booking.getId()).execute();
            bookingclasses.remove(booking);
            notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return bookingclasses.size();
    }

}
