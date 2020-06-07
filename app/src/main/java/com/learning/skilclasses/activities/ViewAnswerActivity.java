package com.learning.skilclasses.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.learning.skilclasses.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAnswerActivity extends AppCompatActivity {


    @BindView(R.id.answer)
    TextView answer;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.q_image)
    ImageView question_image;
    @BindView(R.id.answer_image)
    ImageView answer_image;
    String u_question, id, imageURL;
    String url = "http://digitalcatnyx.store/api/view_answer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View Answers");
        u_question = getIntent().getStringExtra("question");
        id = getIntent().getStringExtra("message_id");
        imageURL = getIntent().getStringExtra("question_image");
        question.setText("Q. " + u_question);
        if (!TextUtils.isEmpty(imageURL)) {
            Glide.with(this).load(imageURL).into(question_image);
        } else {
            question_image.setVisibility(View.GONE);
        }
        fetchClassDetails(id);
    }

    private void fetchClassDetails(String message_id) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("SUC_RES_A", response + "");
                                Log.d("MSG_ID", message_id + "");
                                JSONObject detail = new JSONObject(response);
                                if (detail.getString("answer_image").length() != 0) {
                                    // Toast.makeText(ViewAnswerActivity.this, "Visible", Toast.LENGTH_SHORT).show();
                                    answer_image.setVisibility(View.VISIBLE);
                                    try {
                                        Glide.with(ViewAnswerActivity.this).load("http://www.digitalcatnyx.store/Coaching/admin/uploads/doubtanswers/"+detail.getString("answer_image")).into(answer_image);
                                        //   answer_image.showImage(Uri.parse("http://www.digitalcatnyx.store/Coaching/admin/uploads/doubtanswers/" + detail.getString("answer_image")));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else
                                    answer_image.setVisibility(View.GONE);
                                if (detail.getString("answer").equals("")) {
                                    answer.setVisibility(View.GONE);
                                    answer.setText("Answer Not Uploaded Yet..");
                                } else {
                                    answer.setVisibility(View.VISIBLE);
                                    answer.setText("Answer. " + detail.getString("answer"));

                                }
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
                    params.put("id", message_id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
