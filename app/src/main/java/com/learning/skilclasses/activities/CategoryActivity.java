package com.learning.skilclasses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.CategoriesAdapter;
import com.learning.skilclasses.authentication.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.categories_recycler)
    RecyclerView recyclerView;
    List<String> categoriesList;
    CategoriesAdapter adapter;
    String updateCourse;
    String category;
    private String url = "http://www.digitalcatnyx.store/api/allClasses.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        updateCourse = getIntent().getStringExtra("update");
        category = getIntent().getStringExtra("category");
        Log.d("UPDATE_C", updateCourse + "");
        fetchClassDetails();
    }

    private void fetchClassDetails() {
        try {
            categoriesList = new ArrayList<>();
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                categoriesList.clear();
                                Log.d("SUC_RES", response);

                                //JSONObject jsonObject = new JSONObject(response);

                                //Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                // progressBar.setVisibility(View.GONE);
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    categoriesList.add(detail.getString("class_name"));
                                }

                                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(CategoryActivity.this);
                                layoutManager.setFlexDirection(FlexDirection.ROW);
                                layoutManager.setJustifyContent(JustifyContent.FLEX_START);

                                recyclerView.setLayoutManager(layoutManager);
                              //  if (TextUtils.isEmpty(updateCourse))
                                    adapter = new CategoriesAdapter(categoriesList, CategoryActivity.this, updateCourse);

                              //  else
                                //    adapter = new CategoriesAdapter(categoriesList, CategoryActivity.this, true);

                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR_MESSAGE", error.getMessage() + "");
                    //     Toast.makeText(getContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(CategoryActivity.this);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(updateCourse)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
