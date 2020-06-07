package com.learning.skilclasses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.NotficationAdapter;
import com.learning.skilclasses.models.MyNotificationsModel;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotficationAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private List<MyNotificationsModel> feedbackList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        feedbackList = new ArrayList<>();
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(() -> {
            getNotifications();
            if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
        });
        recyclerView = findViewById(R.id.feedbackRecycler);
        getNotifications();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getNotifications() {
        feedbackList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.digitalcatnyx.store/api/getAllNotifications.php",
                response -> {
                    try {
                        Log.d("FEEDBACKRESPONSE1", response);
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            feedbackList.add(new MyNotificationsModel(
                                    "" + jsonObject1.getString("category"),
                                    "" + jsonObject1.getString("announcement"),
                                    "" + jsonObject1.getString("date"),
                                    "" + jsonObject1.getString("class")));
                        }
                        Collections.reverse(feedbackList);
                        adapter = new NotficationAdapter(feedbackList, this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error ->

                {
                    Log.d("FEEDBACKRERROR", error + "");
                }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("class", "" + new UserSession(NotificationActivity.this).getUserDetails().get(UserSession.KEY_CATEGORY));
                map.put("category", "" + new UserSession(NotificationActivity.this).getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                return map;
            }
        };
        Volley.newRequestQueue(this).

                add(request);
    }
}