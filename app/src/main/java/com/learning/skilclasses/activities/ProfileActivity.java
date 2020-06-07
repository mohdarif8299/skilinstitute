package com.learning.skilclasses.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.developer.filepicker.view.FilePickerDialog;
import com.learning.skilclasses.R;
import com.learning.skilclasses.Utilities.ApiUrl;
import com.learning.skilclasses.models.SubCategoriesModel;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    // @BindView(R.id.image)
    //   CircleImageView imageView;
    @BindView(R.id.upload_image)
    LinearLayout uploadImage;
    OkHttpClient okHttpClient;
    FilePickerDialog dialog;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    String category, subcategory, price;
    private String url = "http://www.digitalcatnyx.store/api/allClasses.php";
    String url1 = "http://www.digitalcatnyx.store/api/allCategory.php";

    @OnClick(R.id.upload_image)
    void uploadImagePhp() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);


    }

    public final static int RequestCode = 100;
    UserSession userSession;

    int count = 0;

    @OnClick(R.id.update_course)
    void updateCourse() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("update", "update");
        startActivity(intent);
    }

    @BindView(R.id.name)
    EditText editName;
    @BindView(R.id.username)
    EditText editUsername;
    @BindView(R.id._class)
    TextView _class;
    @BindView(R.id.course)
    TextView course;

    List<String> categories;
    List<SubCategoriesModel> subcategories;

    @BindView(R.id.progress_update)
    ProgressBar progress;

    @BindView(R.id.update)
    Button update;

    @OnClick(R.id.update)
    void updateUser() {
        try {
            update.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            String updateResponse = updateuser(userSession.getUserDetails().get(UserSession.KEY_ID), ApiUrl.UPDATE_USER_PROFILE, editName.getText().toString().trim());
            Log.d("UPD_RSP", updateResponse + "");
            JSONObject jsonObject = new JSONObject(updateResponse);
            if (jsonObject.getBoolean("status")) {
                userSession.setName(jsonObject.getString("name"));
                editName.setText(jsonObject.getString("name"));
                update.setEnabled(true);
            } else {
                update.setEnabled(true);
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            progress.setVisibility(View.GONE);
        } catch (Exception e) {
            update.setEnabled(true);
            progress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userSession = new UserSession(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        category = userSession.getUserDetails().get(UserSession.KEY_CATEGORY);
        Log.d("CATWGORY", category);
        subcategory = userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY);
        getSupportActionBar().setTitle(userSession.getUserDetails().get(UserSession.KEY_NAME));
        ButterKnife.bind(this);
        _class.setText(category);
        course.setText(subcategory);
        editName.setText(userSession.getUserDetails().get(UserSession.KEY_NAME));
        editUsername.setEnabled(false);
        editUsername.setText(userSession.getUserDetails().get(UserSession.KEY_EMAIL));
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() != 0) {
                    update.setEnabled(true);
                    update.setBackgroundColor(getColor(R.color.colorPrimary));
                } else {
                    update.setEnabled(false);
                    update.setBackgroundColor(getColor(R.color.disabled));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editName.getText().toString().equals(s)) {
                    count = 0;
                } else {
                    count = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editName.getText().toString().length()==0){
                    update.setEnabled(false);
                    update.setBackgroundColor(getColor(R.color.disabled));
                    count = 0;

                }
                else{
                    update.setEnabled(true);
                    update.setBackgroundColor(getColor(R.color.colorPrimary));
                    count = 1;
                }
                /*if (editName.getText().toString().equals(s)) {
                    count = 0;
                } else {
                    count = 1;
                }*/
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private String updateuser(String id, String url, String name) throws Exception {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", id)
                .addFormDataPart("name", name)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
