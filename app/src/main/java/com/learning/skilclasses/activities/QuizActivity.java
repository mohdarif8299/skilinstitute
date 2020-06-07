package com.learning.skilclasses.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.models.Exam;
import com.learning.skilclasses.preferences.UserSession;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {
    /* FirebaseDatabase firebaseDatabase, firebaseDatabase1;
     DatabaseReference databaseReference, databaseReference1;*/
    static LinkedHashMap<JSONObject, String> arrayList;
    public static final String url = "http://www.digitalcatnyx.store/api/mcqquestion.php";
    JSONArray jsonArray;
    int i = 0, flag = 0;
    TextView time, setName;
    int questions = 0;
    ProgressBar progressBar;
    String answer, myquestion;
    Vibrator vibe;
    int timer;
    String uid, username;
    TextView questionNo;
    Exam examination;
    Button submit;
    String date,b;
    ProgressDialog progressDialog;
    UserSession userSession;
    boolean isFinished = false;
    int score = 0, counter = 0;
    CountDownTimer counttimer;



    LinearLayout layoutOp1, layoutOp2, layoutOp3, layoutOp4;
    ImageView imgQ, imgOp1, imgOp2, imgOp3, imgOp4;
    TextView textQ, textOp1, textOp2, textOp3, textOp4;
    String text_option1, text_option2, text_option3, text_option4, text_question, img_op1, img_op2, img_op3, img_op4, img_question;
    String id, category, subcategory;
    String q_class;
    int qtime;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Online Quiz Skil Classes");

         b=getIntent().getStringExtra("papername");
        userSession=new UserSession(getApplicationContext());


        //Toast.makeText(getApplicationContext(),userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY),Toast.LENGTH_LONG).show();

        arrayList = new LinkedHashMap<>();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        time = findViewById(R.id.time);
        questionNo = findViewById(R.id.question_no);
        progressBar = findViewById(R.id.progress_timer);
        submit = findViewById(R.id.submit);


        progressDialog = new ProgressDialog(this, R.style.MyDialogTheme);
        progressDialog.setMessage("Loading Question, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        layoutOp1 = findViewById(R.id.LayoutOp1);
        layoutOp2 = findViewById(R.id.LayoutOp2);
        layoutOp3 = findViewById(R.id.LayoutOp3);
        layoutOp4 = findViewById(R.id.LayoutOp4);

        imgQ = findViewById(R.id.ImgQ);
        textQ = findViewById(R.id.TextQ);


        imgOp1 = findViewById(R.id.ImgOp1);
        imgOp2 = findViewById(R.id.ImgOp2);
        imgOp3 = findViewById(R.id.ImgOp3);
        imgOp4 = findViewById(R.id.ImgOp4);

        textOp1 = findViewById(R.id.TextOp1);
        textOp2 = findViewById(R.id.TextOp2);
        textOp3 = findViewById(R.id.TextOp3);
        textOp4 = findViewById(R.id.TextOp4);
        loadmyQuiz();
        layoutOp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = 0;
                progressDialog.show();
                if (questions == jsonArray.length()) {
                    if ("a".equals(answer)) {
                        layoutOp1.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                    } else {
                        vibe.vibrate(500);
                        layoutOp1.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                    }
                    completeQuestion();
                } else {
                    if ("a".equals(answer)) {
                        layoutOp1.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                        // nextQuestion();
                        counter += 1;
                        setQuiz(counter);

                    } else {
                        vibe.vibrate(500);
                        layoutOp1.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                        //nextQuestion();
                        counter += 1;
                        setQuiz(counter);
                    }
                }

            }
        });

        layoutOp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = 0;
                progressDialog.show();
                if (questions == jsonArray.length()) {
                    if ("b".equals(answer)) {
                        layoutOp2.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                    } else {
                        vibe.vibrate(500);
                        layoutOp2.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                    }
                    completeQuestion();
                } else {
                    if ("b".equals(answer)) {
                        layoutOp2.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                        // nextQuestion();
                        counter += 1;
                        setQuiz(counter);

                    } else {
                        vibe.vibrate(500);
                        layoutOp2.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                        //nextQuestion();
                        counter += 1;
                        setQuiz(counter);
                    }
                }

            }
        });


        layoutOp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = 0;
                progressDialog.show();
                if (questions == jsonArray.length()) {
                    if ("c".equals(answer)) {
                        layoutOp3.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                    } else {
                        vibe.vibrate(500);
                        layoutOp3.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                    }
                    completeQuestion();
                } else {
                    if ("c".equals(answer)) {
                        layoutOp3.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                        // nextQuestion();
                        counter += 1;
                        setQuiz(counter);

                    } else {
                        vibe.vibrate(500);
                        layoutOp3.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                        //nextQuestion();
                        counter += 1;
                        setQuiz(counter);
                    }
                }

            }
        });
        layoutOp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = 0;
                progressDialog.show();
                if (questions == jsonArray.length()) {
                    if ("d".equals(answer)) {
                        layoutOp4.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                    } else {
                        vibe.vibrate(500);
                        layoutOp4.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                    }
                    completeQuestion();
                } else {
                    if ("d".equals(answer)) {
                        layoutOp4.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        score++;
                        // nextQuestion();
                        counter += 1;
                        setQuiz(counter);

                    } else {
                        vibe.vibrate(500);
                        layoutOp4.setBackground(getResources().getDrawable(R.drawable.incorrect_answer));
                        //nextQuestion();
                        counter += 1;
                        setQuiz(counter);
                    }
                }

            }
        });




        submit.setOnClickListener(v -> {
            move();
        });
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    }
    public void CoundownTimer(int timer){

        if (counttimer!=null){
            counttimer.cancel();
        }

        counttimer=new CountDownTimer(timer*1000, 1000) {
            @Override
            public void onTick(long l) {
                int pro = (int) l / 1000;
                time.setText(pro + "");
                progressBar.setProgress(pro);
            }

            @Override
            public void onFinish() {
                if (questions==jsonArray.length()){
                layoutOp1.setEnabled(false);
                layoutOp2.setEnabled(false);
                layoutOp3.setEnabled(false);
                layoutOp4.setEnabled(false);
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("score", score + "");
                    intent.putExtra("papername",b);
                    startActivity(intent);
                    finish();
                }
                else{
                    counter=counter+1;
                    setQuiz(counter);
                }
            }

        }.start();

    }

    public void loadmyQuiz() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(getApplicationContext(),"json"+response,Toast.LENGTH_LONG).show();
                    jsonArray = new JSONArray(response);
                    progressDialog.dismiss();
                    setQuiz(counter);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR_RESPONSE", "" + error.getMessage());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
                params.put("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
                params.put("test_name",b);
                params.put("test_type","testmcq");

                return params;
            }



        };
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setQuiz(int counter) {
        //Toast.makeText(getApplicationContext(),"json"+jsonArray.length(),Toast.LENGTH_LONG).show();
        new Handler().postDelayed(() -> {
            layoutOp1.setBackground(getResources().getDrawable(R.drawable.doubt_background));
            layoutOp2.setBackground(getResources().getDrawable(R.drawable.doubt_background));
            layoutOp3.setBackground(getResources().getDrawable(R.drawable.doubt_background));
            layoutOp4.setBackground(getResources().getDrawable(R.drawable.doubt_background));
            //Toast.makeText(getApplicationContext(),"json"+jsonArray.length(),Toast.LENGTH_LONG).show();
            if (counter < jsonArray.length()) {
            //    Toast.makeText(getApplicationContext(),"json"+jsonArray.length(),Toast.LENGTH_LONG).show();
                imgOp1.setVisibility(View.GONE);
                imgOp2.setVisibility(View.GONE);
                imgOp3.setVisibility(View.GONE);
                imgOp4.setVisibility(View.GONE);
                textOp1.setVisibility(View.GONE);
                textOp2.setVisibility(View.GONE);
                textOp3.setVisibility(View.GONE);
                textOp4.setVisibility(View.GONE);
                imgQ.setVisibility(View.GONE);
                textQ.setVisibility(View.GONE);
                progressDialog.dismiss();
                try {
                    JSONObject object = jsonArray.getJSONObject(counter);
                    text_option1 = object.getString("option1");
                    text_option2 = object.getString("option2");
                    text_option3 = object.getString("option3");
                    text_option4 = object.getString("option4");
                    img_op1 = object.getString("op1_img");
                    img_op2 = object.getString("op2_img");
                    img_op3 = object.getString("op3_img");
                    img_op4 = object.getString("op4_img");
                    answer = object.getString("answer");
                    date = object.getString("date");
                    id = object.getString("id");
                    q_class = object.getString("class");
                    category = object.getString("category");
                    subcategory = object.getString("subcategory");
                    text_question = object.getString("question");
                    img_question = object.getString("question_img");
                    qtime=Integer.parseInt(object.getString("test_time"));
                    //Toast.makeText(getApplicationContext(),"ss"+qtime,Toast.LENGTH_LONG).show();
                    arrayList.put(object, answer);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                CoundownTimer(qtime);
                questions++;
                if (questions < 10) {
                    questionNo.setText("0" + questions);

                } else {
                    questionNo.setText("0" + questions);
                }


                if (!text_question.isEmpty()) {
                    textQ.setVisibility(View.VISIBLE);
                    textQ.setText(text_question);
                }
                if (!img_question.isEmpty()) {
                    imgQ.setVisibility(View.VISIBLE);


                    Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/" + img_question).into(imgQ);

                }

                if (!text_option1.isEmpty()) {
                    textOp1.setVisibility(View.VISIBLE);
                    textOp1.setText(text_option1);
                }
                if (!img_op1.isEmpty()) {
                    imgOp1.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/" + img_op1).into(imgOp1);
                }


                if (!text_option2.isEmpty()) {
                    textOp2.setVisibility(View.VISIBLE);
                    textOp2.setText(text_option2);
                }
                if (!img_op2.isEmpty()) {
                    imgOp2.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/" + img_op2).into(imgOp2);

                }
                if (!text_option3.isEmpty()) {
                    textOp3.setVisibility(View.VISIBLE);
                    textOp3.setText(text_option3);
                }
                if (!img_op3.isEmpty()) {
                    imgOp3.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/" + img_op3).into(imgOp3);

                }


                if (!text_option4.isEmpty()) {
                    textOp4.setVisibility(View.VISIBLE);
                    textOp4.setText(text_option4);
                }
                if (!img_op4.isEmpty()) {
                    imgOp4.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/" + img_op4).into(imgOp4);
                }

            } else {
             //   Toast.makeText(QuizActivity.this, "Question is finished", Toast.LENGTH_SHORT).show();
                return;
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alertDialog.setTitle("Exit Quiz");
        alertDialog.setMessage("Do you want to exit the quiz");
        alertDialog.setPositiveButton("Yes", (d, i) -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            counttimer.cancel();
            finish();
        }).setNegativeButton("No", (d, i) -> d.cancel());
        alertDialog.show();
    }

    public void completeQuestion() {
        isFinished = true;
        Handler h = new Handler();
        h.postDelayed(() -> {
            progressDialog.dismiss();
            layoutOp1.setEnabled(false);
            layoutOp2.setEnabled(false);
            layoutOp3.setEnabled(false);
            layoutOp4.setEnabled(false);
            move();
        }, 1000);
    }

    public void move() {
        // uploadResult();
        counttimer.cancel();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score + "");
        intent.putExtra("papername",b);
        intent.putExtra("context", "QuizActivity");
        startActivity(intent);
        finish();
    }


//    public void uploadResult() {
//        firebaseDatabase1 = FirebaseDatabase.getInstance();
//        databaseReference1 = firebaseDatabase1.getReference("Result").child(uid);
//        databaseReference1.push().setValue(setResult);
//        SendMail sm = new SendMail(this, username, "Engineers Zone", "Thanks for Playing the Quiz. Your Score for chapter " + chapter_name + " is " + score + " Date " + date + " Play again for better result");
//        sm.execute();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
                break;
        }
        return true;
    }
}
