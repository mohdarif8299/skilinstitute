package com.learning.skilclasses.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.SubCategoryActivity;
import com.learning.skilclasses.preferences.UserSession;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<String> videoList;
    private Context context;
    private String updateCourse;
    UserSession userSession;

    public CategoriesAdapter(List<String> messageList, Context context, String updateCourse) {
        this.videoList = messageList;
        this.context = context;
        this.updateCourse = updateCourse;
        userSession = new UserSession(context);
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_layout, parent, false);
        return new CategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        String video = videoList.get(position);
        try {
            holder.categoryName.setText(video);
            holder.card.setOnClickListener(v -> {
                holder.card.setBackground(context.getDrawable(R.drawable.course_background_selected));
                holder.categoryName.setTextColor(Color.WHITE);
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("category", video);
                intent.putExtra("update", updateCourse);
                context.startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_name)
        TextView categoryName;
        @BindView(R.id.card)
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
