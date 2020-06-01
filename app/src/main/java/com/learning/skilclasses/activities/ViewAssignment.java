package com.learning.skilclasses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.CustomRecyclerAdapter;
import com.learning.skilclasses.models.AssignmentUtils;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAssignment extends AppCompatActivity {
    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/uploaded_assignment.php";
    RecyclerView recyclerView;
    CustomRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<AssignmentUtils> list;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    UserSession userSession;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View Assignments");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        userSession = new UserSession(getApplicationContext());
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleViewContainer);
        /*layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
        try {
            StringRequest request = new StringRequest(Request.Method.POST, CLASS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("CAROUSEL_IMAGE", response + "");
                                System.out.println("--------------------" + response + "-------------");
                                JSONArray array = new JSONArray(response);
                                Log.d("A_RESPONSE", array + "");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    Log.d("deatil", detail + "");
                                    Log.d("hahahahahaha", "=========" + detail.getString("category"));
                                    list.add(
                                            new AssignmentUtils(
                                                    detail.getString("category"),
                                                    detail.getString("subcategory"),
                                                    detail.getString("class"),
                                                    detail.getString("assignment_path")
                                            )
                                    );

                                    /*images.add("http://www.digitalcatnyx.store/Coaching/admin/uploads/crousel/" + detail.getString("img"));
                                    Log.d("C_IMAGEA", images.get(i));
                                    carouselView.setImageListener(imageListener);
                                    carouselView.setPageCount(images.size());*/
                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                mAdapter = new CustomRecyclerAdapter(getApplicationContext(), list);
                                mAdapter.notifyDataSetChanged();
                                //list.clear();
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                //   Toast.makeText(getContext(), "Login Error" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getContext(), "Login Error" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("subcategory", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                    params.put("myclass", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(ViewAssignment.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
