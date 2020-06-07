package com.learning.skilclasses.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.learning.skilclasses.R;
import com.learning.skilclasses.models.MyNotificationsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotficationAdapter extends RecyclerView.Adapter<NotficationAdapter.ViewHolder> {

    private List<MyNotificationsModel> myNotificationsModelList;
    private Context context;

    public NotficationAdapter(List<MyNotificationsModel> feedbackList, Context context) {
        this.myNotificationsModelList = feedbackList;
        this.context = context;
    }


    @NonNull
    @Override
    public NotficationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new NotficationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotficationAdapter.ViewHolder holder, int position) {

        MyNotificationsModel feedback = myNotificationsModelList.get(position);
        holder.nameText.setText(feedback.get_class() + ", " + feedback.getCategoryName());
        holder.messageText.setText(feedback.getMessage());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long millis = System.currentTimeMillis();
        Glide.with(context).load(R.drawable.logo).into(holder.image);
        Date currentDate = new Date(millis);
        simpleDateFormat.format(currentDate);
        try {
            java.util.Date feedbackDate = simpleDateFormat.parse(feedback.getDate());
            long difference = currentDate.getTime() - feedbackDate.getTime();
            float daysBetween = (difference / (1000 * 60 * 60 * 24));
            if (daysBetween == 0f)
                holder.dateText.setText("Today");
            else
                holder.dateText.setText(String.format("%.0f", daysBetween) + " days ago");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return myNotificationsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText, dateText, messageText;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.classandcategory);
            dateText = itemView.findViewById(R.id.date);
            messageText = itemView.findViewById(R.id.message);
            image = itemView.findViewById(R.id.image);
        }
    }
}
