package com.learning.skilclasses.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.MainActivity;
import com.learning.skilclasses.activities.MyCourseActivity;
import com.learning.skilclasses.activities.TestActivty;
import com.learning.skilclasses.adapters.AssignmentAdapter;
import com.learning.skilclasses.adapters.VideoAdapter2;
import com.learning.skilclasses.models.AssignmentBean;
import com.learning.skilclasses.models.Video;
import com.learning.skilclasses.preferences.UserSession;
import com.learning.skilclasses.viewModels.FragmentHomeViewModel;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

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


public class HomeFragment extends Fragment {

    ImageView Lassign,LTest,Lquiz;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.recycler_view1)
    RecyclerView recyclerView1;

    @OnClick(R.id.mycourse)
    void openMyCourse() {
        startActivity(new Intent(getActivity(), MyCourseActivity.class));
    }


    @OnClick(R.id.myassignment)
    void openMyAssignment() {
        if (userSession.getUserDetails().get(UserSession.KEY_STATUS).equals("true")){
        activity.changeNavPosition(2);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, AssignmentFragment.getInstance());
        fragmentTransaction.commit();
    }
        else{
            startActivity(new Intent(activity,MyCourseActivity.class));
        }
    }

    @OnClick(R.id.myvideos)
    void openMyVideos() {
        activity.changeNavPosition(1);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, VideosFragment.getInstance());
        fragmentTransaction.commit();
    }

    @OnClick(R.id.mydoubts)
    void openMyDoubts() {
        activity.changeNavPosition(4);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, DoubtsFragment.getInstance());
        fragmentTransaction.commit();
    }

    @OnClick(R.id.myquiz)
    void openMyQuiz() {
        if (userSession.getUserDetails().get(UserSession.KEY_STATUS).equals("true")){
        activity.changeNavPosition(3);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, QuizFragment.getInstance());
        fragmentTransaction.commit();
    }else{
            startActivity(new Intent(activity,MyCourseActivity.class));
        }
    }

    @OnClick(R.id.mytest)
    void openMyTest() {
        if (userSession.getUserDetails().get(UserSession.KEY_STATUS).equals("true")){
        startActivity(new Intent(getActivity(), TestActivty.class));
        }
        else{
            startActivity(new Intent(activity,MyCourseActivity.class));
        }
    }


    private List<AssignmentBean> list;
    private VideoAdapter2 videoAdapter;
    private List<Video> videoList;
    private MainActivity activity;
    private UserSession userSession;
    private String category;
    private String subcategory;
    private CarouselView carouselView;
    private List<String> images;
    private ImageListener imageListener;
    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/class_detail.php";
    private static String CLASS_URL1 = "http://www.digitalcatnyx.store/api/assignment.php";
    private String carouselURL = "http://www.digitalcatnyx.store/api/corousel_fetch.php";
    LinearLayout assignmntLayout,bottomcards;

    private static HomeFragment instance;

    public static HomeFragment getInstance() {
        if (instance == null)
            instance = new HomeFragment();

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Skil Classes");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        images = new ArrayList<>();
        carouselView = view.findViewById(R.id.carouselView);
        Lassign=view.findViewById(R.id.lockerassign);
        Lquiz=view.findViewById(R.id.locker);
        LTest=view.findViewById(R.id.lockerquiz);
        activity = (MainActivity) getActivity();
        list = new ArrayList<>();
        assignmntLayout=view.findViewById(R.id.online_Asssignment);
        bottomcards=view.findViewById(R.id.bottomcards);

        userSession = new UserSession(activity);
        category = userSession.getUserDetails().get(UserSession.KEY_CATEGORY);
        subcategory = userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY);
        //Toast.makeText(activity,)
        if (getPaymentstatus()){
            assignmntLayout.setVisibility(View.VISIBLE);
            bottomcards.setVisibility(View.VISIBLE);
            /*Lassign.setVisibility(View.GONE);
            LTest.setVisibility(View.GONE);
            Lquiz.setVisibility(View.GONE)*/;
        }


        if (activity != null) {
            getVideosList(CLASS_URL, category, subcategory, null);
            //     fetchClassDetailsAssignment(category, subcategory, null);
        }
        FragmentHomeViewModel model = ViewModelProviders.of(this).get(FragmentHomeViewModel.class);
        model.getAssignments(category, subcategory, null, getActivity()).observe(getActivity(), new Observer<List<AssignmentBean>>() {
            @Override
            public void onChanged(List<AssignmentBean> assignmentBean) {
                AssignmentAdapter adapter = new AssignmentAdapter(getContext(), assignmentBean);
                recyclerView1.setAdapter(adapter);
                recyclerView1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                adapter.notifyDataSetChanged();
            }
        });
        imageListener = (position, imageView) -> {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getActivity()).load(images.get(position)).into(imageView);
        };
        model.getCarouselImages(getActivity()).observe(getActivity(), strings -> {
            images = strings;
            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(strings.size());
        });
        return view;
    }

    private boolean getPaymentstatus() {
        if (userSession.getUserDetails().get(UserSession.KEY_STATUS).equals("true")){
            return true;
        }
        else if (userSession.getUserDetails().get(UserSession.KEY_STATUS)==null){
            return false;
        }
        else{
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() == null) return;
    }

    public void getVideosList(String classUrl, String s, String s1, String sub) {

        StringRequest str = new StringRequest(Request.Method.POST, classUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    videoList = new ArrayList<>();
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR_RESPONSE", error.getMessage() + " ");
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

                    videoList.add(new Video(id, aClass, category, subcategory, video_descp, videoFile, file_extension, date, videoUrl, thumbImg.getString("640"), urls));
                    SnapHelper snapHelper = new PagerSnapHelper();
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    videoAdapter = new VideoAdapter2(videoList, activity);
                    videoAdapter.notifyDataSetChanged();
                    snapHelper.attachToRecyclerView(recyclerView);
                    recyclerView.setAdapter(videoAdapter);
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


    private void fetchClassDetailsAssignment(String category, String subcategory, String subject) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, CLASS_URL1,
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
                                recyclerView1.setAdapter(adapter1);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
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
}
