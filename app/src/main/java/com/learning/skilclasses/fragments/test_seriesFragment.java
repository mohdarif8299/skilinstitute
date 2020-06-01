package com.learning.skilclasses.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.QuizActivity;
import com.learning.skilclasses.activities.TestpaperActivity;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class test_seriesFragment extends Fragment {
    UserSession userSession;
    TextView textView;

    public test_seriesFragment() {
        // Required empty public constructor
    }

    private static test_seriesFragment instance;

    public static test_seriesFragment getInstance() {
        if (instance == null)
            instance = new test_seriesFragment();

        return instance;
    }



    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/getListOptions.php";
    private static String CLASS_URL2 = "http://www.digitalcatnyx.store/api/getTestItemCount.php";
    List TestList;
    Object testname;

    @BindView(R.id.spinner)
    Spinner spinner;

    @OnClick(R.id.start)
    void startQuiz() {
        if (testname==spinner.getItemAtPosition(0))
            Toast.makeText(getContext(),"You have not seleted any Quiz ",Toast.LENGTH_LONG).show();

        else if(Integer.parseInt(textView.getText().toString())==0){

            Toast.makeText(getContext(),"There is no question in the selected Test paper",Toast.LENGTH_LONG).show();

        }
        else{
            Intent intent = new Intent(getContext(), TestpaperActivity.class);
            intent.putExtra("papername",testname.toString());
            startActivity(intent);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Test Series");
        View view = inflater.inflate(R.layout.fragment_test_series, container, false);
        userSession=new UserSession(getContext());
        textView=(view).findViewById(R.id.qcount);
        TestList=new ArrayList();
        TestList.add("Select Test Paper");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StringRequest stringRequest= new StringRequest(Request.Method.POST, CLASS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();

                try {
                    JSONArray testArray=new JSONArray(response);


                    for (int i=0;i<testArray.length();i++){
                        JSONObject TestName=testArray.getJSONObject(i);
                        TestList.add(TestName.getString("test_name"));


                    }
                    //Toast.makeText(getContext(),"testName"+TestList,Toast.LENGTH_LONG).show();

                    ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,TestList);
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                       /* params.put("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                        params.put("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));*/
                params.put("class",userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                params.put("category",userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                params.put("type","testseries");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                testname=adapterView.getItemAtPosition(i);
                StringRequest stringRequest=new StringRequest(Request.Method.POST, CLASS_URL2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                        textView.setText(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                        params.put("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                        params.put("type","testseries");
                        params.put("TestName",testname.toString());

                        return params;
                    }
                };

                RequestQueue requestQueue1=Volley.newRequestQueue(getContext());
                requestQueue1.add(stringRequest);



                // Toast.makeText(getContext(),testname.toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,)
    }
}
