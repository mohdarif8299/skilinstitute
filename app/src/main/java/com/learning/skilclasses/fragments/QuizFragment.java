package com.learning.skilclasses.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.QuizActivity;
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
import butterknife.OnClick;


public class QuizFragment extends Fragment {

    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/getListOptions.php";
    private static String CLASS_URL2 = "http://www.digitalcatnyx.store/api/getTestItemCount.php";
    List TestList;
    Object testname;
    UserSession userSession;
    TextView textView;

    @BindView(R.id.spinner)
    Spinner spinner;

    private static QuizFragment instance;

    public static QuizFragment getInstance() {
        if (instance == null)
            instance = new QuizFragment();

        return instance;
    }

    @OnClick(R.id.start)
    void startQuiz() {
        if (testname == spinner.getItemAtPosition(0))
            Toast.makeText(getContext(), "You have not seleted any Quiz ", Toast.LENGTH_LONG).show();

        else if (Integer.parseInt(textView.getText().toString()) == 0) {

            Toast.makeText(getContext(), "There is no question in the selected Test paper", Toast.LENGTH_LONG).show();

        } else {
            Intent intent = new Intent(getContext(), QuizActivity.class);
            intent.putExtra("papername", testname.toString());
            startActivity(intent);
        }

    }

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Quiz");
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        TestList = new ArrayList();
        textView = (view).findViewById(R.id.qcount1);
        TestList.add("Select MCQ's Series");
        ButterKnife.bind(this, view);
        userSession = new UserSession(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CLASS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray testArray = new JSONArray(response);

                    for (int i = 0; i < testArray.length(); i++) {
                        JSONObject TestName = testArray.getJSONObject(i);
                        TestList.add(TestName.getString("test_name"));


                    }
                    //Toast.makeText(getContext(),"testName"+TestList,Toast.LENGTH_LONG).show();

                    if (getActivity() == null) return;
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, TestList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                       /* params.put("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                        params.put("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));*/
                params.put("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                params.put("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                params.put("type", "testmcq");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                testname = adapterView.getItemAtPosition(i);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, CLASS_URL2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                        textView.setText(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                        params.put("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                        params.put("type", "testseries");
                        params.put("TestName", testname.toString());

                        return params;
                    }
                };

                RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                requestQueue1.add(stringRequest);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

}
