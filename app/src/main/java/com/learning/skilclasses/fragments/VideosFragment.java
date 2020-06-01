package com.learning.skilclasses.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.VideoAdapter2;
import com.learning.skilclasses.models.Video;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
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

public class VideosFragment extends Fragment {
    RecyclerView rc;
    VideoAdapter2 videoAdapter;
    UserSession userSession;
    List<Video> videoList;
    static Context activity;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    List<String> carsList;
    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/class_detail.php";


    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private static VideosFragment instance;

    public static VideosFragment getInstance() {
        if (instance == null)
            instance = new VideosFragment();

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Videos");

        View view = inflater.inflate(R.layout.fragment_videos_fragmemt, container, false);
        rc = (view).findViewById(R.id.videos);

        ButterKnife.bind(this, view);
        activity = getActivity();
        userSession = new UserSession(getActivity());
        videoList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
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
                        getVideosList(CLASS_URL, userSession.getUserDetails().get(UserSession.KEY_CATEGORY), userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY), null);
                    else
                        getVideosList(CLASS_URL, userSession.getUserDetails().get(UserSession.KEY_CATEGORY), userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY), item.toLowerCase());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getVideosList(CLASS_URL, userSession.getUserDetails().get(UserSession.KEY_CATEGORY), userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SearchView searchView = view.findViewById(R.id.search);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("Search for Videos...");
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (videoAdapter != null)
                    videoAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    public void getVideosList(String classUrl, String s, String s1, String sub) {

        StringRequest str = new StringRequest(Request.Method.POST, classUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    videoList.clear();

                    JSONArray jarray = new JSONArray(response);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject detail = jarray.getJSONObject(i);
                        getLink(detail.getString("id"),
                                detail.getString("class"),
                                detail.getString("category"),
                                detail.getString("subcategory"),
                                detail.getString("video_descp"),
                                detail.getString("video_file"),
                                detail.getString("file_extension"),
                                detail.getString("date")
                        );
                        // Toast.makeText(activity,detail.getString("video_file"),Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("category", s);
                params.put("subcategory", s1);
                if (sub != null) {
                    params.put("subject", sub.toLowerCase());

                }
                return params;
            }

            ;

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(str);

    }

    public void getLink(String id, String aClass, String category, String subcategory, String video_descp, String video_file, String file_extension, String date) {

        video_file = video_file.replace("https://vimeo.com/", "https://player.vimeo.com/video/");
        video_file += "/config";
        String videoFile = video_file;
        //Toast.makeText(activity,""+video_file,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, video_file, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> urls = new HashMap<String, String>();

                try {
                    JSONObject josn = new JSONObject(response);
                    JSONObject json1 = josn.getJSONObject("request");
                    JSONObject json2 = json1.getJSONObject("files");
                    JSONArray videos = json2.getJSONArray("progressive");

                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject vurl2 = videos.getJSONObject(i);
                        urls.put(vurl2.getString("quality"), vurl2.getString("url"));
                    }
                    JSONObject vurl = videos.getJSONObject(1);
                    String videoUrl = vurl.getString("url");

                    JSONObject thumbnail = josn.getJSONObject("video");
                    JSONObject thumbImg = thumbnail.getJSONObject("thumbs");

                    //Toast.makeText(activity,""+thumbImg.getString("640"),Toast.LENGTH_LONG).show();

                    videoList.add(new Video(id, aClass, category, subcategory, video_descp, videoFile, file_extension, date, videoUrl, thumbImg.getString("640"), urls));

                    rc.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                    videoAdapter = new VideoAdapter2(videoList, activity);
                    videoAdapter.notifyDataSetChanged();
                    rc.setAdapter(videoAdapter);
                    videoAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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
