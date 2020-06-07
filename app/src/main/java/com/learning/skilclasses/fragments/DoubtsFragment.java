package com.learning.skilclasses.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.learning.skilclasses.Utilities.ApiUrl;
import com.learning.skilclasses.activities.AskDoubtActivity;
import com.learning.skilclasses.adapters.MessageAdapter;
import com.learning.skilclasses.models.Message;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class DoubtsFragment extends Fragment {
    private List<Message> messageList;

    @BindView(R.id.doubts)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

//    @BindView(R.id.swipe)
//    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.ask_doubts2)
    void askDoubt2() {
        startActivity(new Intent(getActivity(), AskDoubtActivity.class));
    }

    @OnClick(R.id.ask_doubts1)
    void askDoubt1() {
        startActivity(new Intent(getActivity(), AskDoubtActivity.class));
    }

    @OnClick(R.id.ask_doubts)
    void askDoubt() {
        startActivity(new Intent(getActivity(), AskDoubtActivity.class));
    }


    UserSession userSession;
    OkHttpClient okHttpClient;


    MessageAdapter adapter;


    private static DoubtsFragment instance;

    public static DoubtsFragment getInstance() {
        if (instance == null)
            instance = new DoubtsFragment();

        return instance;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ask Your Doubts");
        View view = inflater.inflate(R.layout.fragment_doubts, container, false);
        ButterKnife.bind(this, view);
        userSession = new UserSession(getActivity());
        messageList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        try {
            getMessages(ApiUrl.GET_MESSAGES, userSession.getUserDetails().get(UserSession.KEY_CATEGORY), userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
//            swipeRefreshLayout.setOnRefreshListener(() -> {
//                try {
//                    getMessages(ApiUrl.GET_MESSAGES, userSession.getUserDetails().get(UserSession.KEY_CATEGORY), userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
//                    swipeRefreshLayout.setRefreshing(false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

//    public void getMessage(String url, String course) throws Exception {
//        AndroidNetworking.post(url)
//                .addBodyParameter("course", course)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            progressBar.setVisibility(View.GONE);
//                            messageList.clear();
//                            JSONArray jsonArray = response;
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                messageList.add(new Message(jsonObject.getString("message"), jsonObject.getString("message_image"), jsonObject.getString("course"), jsonObject.getString("user_id"), jsonObject.getString("sentat")));
//                            }
//                            Collections.reverse(messageList);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                            adapter = new MessageAdapter(messageList, getActivity());
//                            adapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//    }

    public void getMessages(String url, String category, String subcategory) throws Exception {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("DOUBTS", response + "");
                                progressBar.setVisibility(View.GONE);
                                messageList.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    messageList.add(new Message(
                                                    jsonObject.getString("id"),
                                                    jsonObject.getString("message"),
                                                    jsonObject.getString("category"),
                                                    jsonObject.getString("subcategory"),
                                                    jsonObject.getString("user_id"),
                                                    jsonObject.getString("sentat"),
                                                    jsonObject.getString("message_image"),
                                                    jsonObject.getString("permission")
                                            )
                                    );
                                }
                                Collections.reverse(messageList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                adapter = new MessageAdapter(messageList, getActivity());
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
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
                    params.put("category", category);
                    params.put("subcategory", subcategory);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() == null) return;
    }

}
