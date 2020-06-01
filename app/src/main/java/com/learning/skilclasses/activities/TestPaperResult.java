package com.learning.skilclasses.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.skilclasses.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestPaperResult extends AppCompatActivity {

    LinkedHashMap<JSONObject, String> result;
    LinearLayout linearLayout;
    String context;
    int Qno =0;
    TextView score;
    String userScore = "";
    String papername;
    ProgressBar scoreProgress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.again)
    Button play_again;

    @OnClick(R.id.again)
    void setPlay_again() {
        Intent intent=new Intent(this, TestpaperActivity.class);
        intent.putExtra("papername",papername);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paper_result);
        userScore = getIntent().getStringExtra("score");
        papername=getIntent().getStringExtra("papername");
        context=getIntent().getStringExtra("context");


      //  Toast.makeText(getApplicationContext(),"sa"+context.getClass().getName(),Toast.LENGTH_LONG).show();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        scoreProgress = findViewById(R.id.score_progress);
        linearLayout=findViewById(R.id.main_layout);
        // mBuilder.setContentTitle("Notification Alert, Click Me!");
//        if (userScore == 10) {
//            mBuilder.setContentText("Hi, Your Score is " + userScore + "   Congratulations You Scored 100%");
//        } else
//            mBuilder.setContentText("Hi, Your Score is " + userScore + "   Better Luck next time");
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(001, mBuilder.build());
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        scoreProgress.setProgress(Integer.parseInt(userScore));
        result = TestpaperActivity.arrayList;
//        linearLayout = findViewById(R.id.main_layout);
        score = findViewById(R.id.result);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        param1.setMargins(0, 20, 0, 0);
        param2.setMargins(0, 100, 0, 0);
        score.setText(userScore + "");
        for (Map.Entry<JSONObject, String> entry : result.entrySet()) {
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.didact_gothic);
            JSONObject jsonObject=(JSONObject) entry.getKey();
            try {
                String text_question = jsonObject.getString("question");
                String img_question = jsonObject.getString("question_img");

                LinearLayout linearLayout1 = new LinearLayout(this);
//                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                linearLayout1.setLayoutParams(param);
                linearLayout1.setOrientation(LinearLayout.VERTICAL);
                linearLayout1.setElevation(5);
                linearLayout1.setPadding(5,5,5,5);
                TextView questionText=new TextView(this);
                questionText.setText("Question "+(++Qno));
                questionText.setTextSize(18);
                questionText.setTextColor(getResources().getColor(R.color.incorrect));
                linearLayout1.addView(questionText);

                if(!text_question.isEmpty()) {
                    TextView text_view = new TextView(this);
                    text_view.setTypeface(typeface, Typeface.BOLD);
                    text_view.setTextSize(14);
                    text_view.setText(text_question);
                    linearLayout1.addView(text_view);
                }
                if(!img_question.isEmpty()) {
                    ImageView img_view = new ImageView(this);
                    img_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/"+img_question).into(img_view);
                    linearLayout1.addView(img_view);
                }

                TextView answerText=new TextView(this);
                answerText.setText("Answer "+Qno);
                answerText.setTextSize(18);
                answerText.setTextColor(getResources().getColor(R.color.correct));
                linearLayout1.addView(answerText);


                TextView answer_text_view = new TextView(this);
                answer_text_view.setTypeface(typeface, Typeface.BOLD);
                answer_text_view.setTextSize(14);
                answer_text_view.setTextColor(getResources().getColor(R.color.correct));

                ImageView answer_img_view = new ImageView(this);
                answer_img_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


                if(entry.getValue().equals("a")){
                    if( !jsonObject.getString("option1").isEmpty()) {
                        answer_text_view.setText(jsonObject.getString("option1"));
                        linearLayout1.addView(answer_text_view);

                    }
                    if(!jsonObject.getString("op1_img").isEmpty()){
                        Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/"+jsonObject.getString("op1_img")).into(answer_img_view);
                        linearLayout1.addView(answer_img_view);
                    }

                }
                if(entry.getValue().equals("b")){
                    if( !jsonObject.getString("option2").isEmpty()) {
                        answer_text_view.setText(jsonObject.getString("option2"));
                        linearLayout1.addView(answer_text_view);
                    }
                    if(!jsonObject.getString("op2_img").isEmpty()){
                        Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/"+jsonObject.getString("op2_img")).into(answer_img_view);
                        linearLayout1.addView(answer_img_view);
                    }

                }
                if(entry.getValue().equals("c")){
                    if( !jsonObject.getString("option3").isEmpty()) {
                        answer_text_view.setText(jsonObject.getString("option3"));
                        linearLayout1.addView(answer_text_view);
                    }
                    if(!jsonObject.getString("op3_img").isEmpty()){
                        Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/"+jsonObject.getString("op3_img")).into(answer_img_view);
                        linearLayout1.addView(answer_img_view);
                    }
                }
                if(entry.getValue().equals("d")){
                    if( !jsonObject.getString("option4").isEmpty()) {
                        answer_text_view.setText(jsonObject.getString("option4"));
                        linearLayout1.addView(answer_text_view);
                    }
                    if(!jsonObject.getString("op4_img").isEmpty()){
                        Picasso.get().load("http://www.digitalcatnyx.store/Coaching/admin/uploads/mcq/"+jsonObject.getString("op4_img")).into(answer_img_view);
                        linearLayout1.addView(answer_img_view);
                    }
                }

                linearLayout.addView(linearLayout1);

            }
            catch (JSONException ex){
                ex.printStackTrace();
            }







//            question.setText("Q" + i + " " + entry.getKey());
//            question.setTextColor(Color.BLACK);
//            TextView answer = new TextView(this);
//            answer.setText(entry.getValue());
//            answer.setLayoutParams(param1);
//            answer.setTextColor(getResources().getColor(R.color.correct));
//            answer.setTypeface(typeface);
//            answer.setTextSize(13);
//            LinearLayout linearLayout1 = new LinearLayout(this);
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            linearLayout1.setLayoutParams(param);
//            linearLayout1.setOrientation(LinearLayout.VERTICAL);
//            linearLayout1.addView(question);
//            linearLayout1.addView(answer);
//            linearLayout.addView(linearLayout1);
//            i++;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
                break;
            default:
                break;
        }
        return true;
    }
}
