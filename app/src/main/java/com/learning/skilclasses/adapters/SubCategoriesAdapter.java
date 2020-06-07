package com.learning.skilclasses.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.ProfileActivity;
import com.learning.skilclasses.authentication.SignupActivity;
import com.learning.skilclasses.models.SubCategoriesModel;
import com.learning.skilclasses.preferences.UserSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder> {

    private List<SubCategoriesModel> videoList;
    private Context context;
    private ArrayList<String> arrayList;
    UserSession userSession;
    String updateCourse;
    String category;
    OkHttpClient okHttpClient;

    public SubCategoriesAdapter(List<SubCategoriesModel> messageList, Context context, String category, String updateCourse, ArrayList<String> arrayList) {
        this.videoList = messageList;
        this.arrayList = arrayList;
        this.context = context;
        userSession = new UserSession(context);
        this.updateCourse = updateCourse;
        this.category = category;
    }

    @NonNull
    @Override
    public SubCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategories_layout, parent, false);
        return new SubCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesAdapter.ViewHolder holder, int position) {
        SubCategoriesModel subCategoriesModel = videoList.get(position);
        try {
            holder.categoryName.setText(subCategoriesModel.getSubcategory());
            holder.categoryPrice.setText("Price \u20B9 " + subCategoriesModel.getPrice());
            holder.card.setOnClickListener(v -> {
                holder.categoryName.setTextColor(Color.WHITE);
                holder.card.setBackground(context.getDrawable(R.drawable.course_background_selected));
                if (TextUtils.isEmpty(updateCourse)) {
                    Intent intent = new Intent(context, SignupActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("subcategory", subCategoriesModel.getSubcategory());
                    intent.putExtra("subcategory_price", subCategoriesModel.getPrice());
                    userSession.setUserCourse(category, subCategoriesModel.getSubcategory(), subCategoriesModel.getPrice(), arrayList);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("subcategory", subCategoriesModel.getSubcategory());
                    intent.putExtra("subcategory_price", subCategoriesModel.getPrice());
                    userSession.setUserCourse(category, subCategoriesModel.getSubcategory(), subCategoriesModel.getPrice(), arrayList);
//                    try {
//                        updateuser(userSession.getUserDetails().get(UserSession.KEY_ID), ApiUrl.UPDATE_USER_PROFILE, userSession.getUserDetails().get(UserSession.KEY_NAME),
//                                category, subCategoriesModel.getSubcategory(), subCategoriesModel.getPrice());
//                        Snackbar.make(v, "Course Updated", Snackbar.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private String updateuser(String id, String url, String name, String category, String subcategory, String price) throws Exception {
//        if (okHttpClient == null) {
//            okHttpClient = new OkHttpClient();
//        }
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("id", id)
//                .addFormDataPart("name", name)
//                .addFormDataPart("category", category)
//                .addFormDataPart("subcategory", subcategory)
//                .addFormDataPart("price", price)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .build();
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            return response.body().string();
//        }
//    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subcategory_name)
        TextView categoryName;
        @BindView(R.id.subcategory_price)
        TextView categoryPrice;
        @BindView(R.id.card)
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
