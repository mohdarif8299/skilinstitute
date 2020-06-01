package com.learning.skilclasses.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.models.AssignmentBean;
import com.learning.skilclasses.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHomeViewModel extends ViewModel {
    private Context context;
    private MutableLiveData<List<AssignmentBean>> assignmentList;
    private MutableLiveData<List<String>> carouselImages;
    private MutableLiveData<List<Video>> videoList;

    public LiveData<List<AssignmentBean>> getAssignments(String category, String subcategory, String subject, Context context) {
        //if the list is null
        if (assignmentList == null) {
            assignmentList = new MutableLiveData();
            //we will load it asynchronously from server in this method
            Log.d("DATA", category + subcategory + subject + "");
            loadVideos(category, subcategory, subject, context);
        }

        //finally we will return the list
        return assignmentList;
    }

    public LiveData<List<Video>> getVideos(String category, String subcategory, String subject, Context context) {
        if (videoList == null) {
            videoList = new MutableLiveData<>();
            getVideosList(category, subcategory, subject, context);
        }

        return videoList;
    }

    public LiveData<List<String>> getCarouselImages(Context context) {

        if (carouselImages == null) {
            carouselImages = new MutableLiveData();
            loadCarouselImages(context);
        }

        return carouselImages;
    }

    private void loadCarouselImages(Context context) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, "http://www.digitalcatnyx.store/api/corousel_fetch.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("CAROUSEL_IMAGE", response + "");
                                JSONArray array = new JSONArray(response);
                                List<String> images = new ArrayList<>();
                                Log.d("A_RESPONSE", array + "");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    images.add("http://www.digitalcatnyx.store/Coaching/admin/uploads/crousel/" + detail.getString("img"));
                                    Log.d("C_IMAGEA", images.get(i));
                                }
                                carouselImages.setValue(images);
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadVideos(String category, String subcategory, String subject, Context context) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, "http://www.digitalcatnyx.store/api/assignment.php",
                    response -> {
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d("ASSIGNMENT_RESPONSE", response + "");
                            List<AssignmentBean> assignmentBeanList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject detail = array.getJSONObject(i);
                                assignmentBeanList.add(new AssignmentBean(detail.getString("id"),
                                        detail.getString("class"),
                                        detail.getString("category"),
                                        detail.getString("subcategory"),
                                        detail.getString("assignment_descp"),
                                        detail.getString("assign_file"),
                                        detail.getString("file_extension"),
                                        detail.getString("date")));
                            }
                            assignmentList.setValue(assignmentBeanList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                Log.d("ASSIGNMENT_ERROR", error.getMessage() + "");
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("category", category);
                    params.put("subcategory", subcategory);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getVideosList(String s, String s1, String sub, Context context) {
        this.context = context;
        StringRequest str = new StringRequest(Request.Method.POST, "http://www.digitalcatnyx.store/api/class_detail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("VIDEO_RESPONSE", response + "");
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
                return params;
            }

            ;

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                List<Video> videosList = new ArrayList<>();
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

                    videosList.add(new Video(id, aClass, category, subcategory, video_descp, videoFile, file_extension, date, videoUrl, thumbImg.getString("640"), urls));
                    videoList.setValue(videosList);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
