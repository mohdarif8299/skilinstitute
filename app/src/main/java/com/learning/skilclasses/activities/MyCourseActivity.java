package com.learning.skilclasses.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.learning.skilclasses.R;
import com.learning.skilclasses.preferences.UserSession;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCourseActivity extends AppCompatActivity {

    @BindView(R.id.cclass)
    TextView cclass;
    @BindView(R.id.course)
    TextView couse;
    UserSession userSession;
    List<String> carsList;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.paynow)
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);
        userSession = new UserSession(this);
        cclass.setText(userSession.getUserDetails().get(UserSession.KEY_CATEGORY));
        couse.setText(userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));
        price.setText("\u20b9 " + userSession.getUserDetails().get(UserSession.KEY_PRICE));
        Gson gson = new Gson();
        String subs = userSession.getUserDetails().get(UserSession.KEY_SUBJECTS);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        try {
            carsList = gson.fromJson(subs, type);
            carsList.remove("");
            carsList.add(0, "Subjects");
            ArrayList<String> list = new ArrayList<>();
            for (String s : carsList) {
                list.add(capitalize(s));
            }
            spinner.setItems(list);
            spinner.setSelectedIndex(0);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getpaymentstatus()){
            pay.setEnabled(false);
            pay.setText("Subscribed");
        }
        else {

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!getpaymentstatus()) {
                        Toast.makeText(getApplicationContext(), "Paynow", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), PaymentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }

    private boolean getpaymentstatus() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
