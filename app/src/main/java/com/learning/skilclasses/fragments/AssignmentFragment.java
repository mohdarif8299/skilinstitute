package com.learning.skilclasses.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.AssignmentAdapter;
import com.learning.skilclasses.models.AssignmentBean;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AssignmentFragment extends Fragment {

    // Spinner spinner;
    // String classtype;
    TextView viewAssignment;
    List<AssignmentBean> list;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    static int count = 0;
    String category;
    String subcategory;
    static Context activity;
    String student_class;
    AssignmentAdapter adapter1;
    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/assignment.php";
    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    List<String> carsList;
    UserSession userSession;


    private static AssignmentFragment instance;

    public static AssignmentFragment getInstance() {
        if (instance == null)
            instance = new AssignmentFragment();

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Assignmnets");
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);


        //spinner=view.findViewById(R.id.ClassType);
        activity = getActivity();
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        list = new ArrayList<AssignmentBean>();
        userSession = new UserSession(activity);
        category = userSession.getUserDetails().get(UserSession.KEY_CATEGORY);
        Gson gson = new Gson();
        String subs = userSession.getUserDetails().get(UserSession.KEY_SUBJECTS);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        try {
            carsList = gson.fromJson(subs, type);
            carsList.remove("");
            carsList.add(0, "All Subjects");
            ArrayList<String> list = new ArrayList<>();
            for (String s : carsList) {
                list.add(capitalize(s));
            }
            spinner.setItems(list);
            spinner.setSelectedIndex(0);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    if (item.equals("All Subjects"))
                        fetchClassDetails(student_class, null);
                    else
                        fetchClassDetails(student_class, item.toLowerCase());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("S_CATEGORY", category);
        subcategory = userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY);
        //  student_class = userSession.getUserDetails().get(UserSession.KEY_COURSE);
        if (activity != null) {
            fetchClassDetails(student_class, null);
        }
        SearchView searchView = view.findViewById(R.id.search);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("Search for Assignments...");
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter1 != null)
                    adapter1.getFilter().filter(newText);
                return false;
            }
        });
        return view;
    }

    private void fetchClassDetails(String _class, String subject) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, CLASS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                list.clear();
                                JSONArray array = new JSONArray(response);
                                Log.d("A_RESPONSE", array + "");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    list.add(new AssignmentBean(detail.getString("id"),
                                            detail.getString("class"),
                                            detail.getString("category"),
                                            detail.getString("subcategory"),
                                            detail.getString("assignment_descp"),
                                            detail.getString("assign_file"),
                                            detail.getString("file_extension"),
                                            detail.getString("date")));

                                }
                                AssignmentAdapter adapter1 = new AssignmentAdapter(getContext(), list);
                                recyclerView.setAdapter(adapter1);
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
                    params.put("category", category);
                    params.put("subcategory", subcategory);
                    if (subject != null) {
                        params.put("subject", subject.toLowerCase());

                    }
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

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
