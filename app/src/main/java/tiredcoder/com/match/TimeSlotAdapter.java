package tiredcoder.com.match;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder>{
    private ArrayList<timeslot> timeslots;
    private Context context;
    public  class  MyViewHolder extends RecyclerView.ViewHolder
    {
        public CheckBox checkBox;
        public TextView time;
        public  MyViewHolder(View view)
        {
            super(view);
            checkBox=view.findViewById(R.id.check);
            time=view.findViewById(R.id.select_time);
        }

    }
    public TimeSlotAdapter(ArrayList<timeslot> timeslots,Context context)
    {
        this.timeslots=timeslots;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeslot, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final timeslot timeslot=timeslots.get(position);
        //     new ImageLoader("192.168.1.103/Turf/img/"+comment.getImage(),holder.imageView).execute();
        holder.time.setText(timeslot.getTime());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
                                                {
                                                    if(isChecked){
                                                        BookTurf.integerSet.add(String.valueOf(holder.getAdapterPosition()));
                                                        BookTurf.timeset.add(timeslot.getTime());
                                                    }else{
                                                   //     BookTurf.integerSet.remove(holder.getAdapterPosition());
                                                        BookTurf.integerSet.remove(String.valueOf(holder.getAdapterPosition()));
                                                        BookTurf.timeset.remove(timeslot.getTime());

                                                    }

                                                }
                                            }
        );
        //     holder.name.setText(comment.getName());
    }
    @Override
    public int getItemCount() {
        return timeslots.size();
    }

}
