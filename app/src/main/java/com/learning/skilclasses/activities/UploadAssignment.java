package com.learning.skilclasses.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.learning.skilclasses.R;
import com.learning.skilclasses.Utilities.ApiUrl;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.VISIBLE;
/*import static com.learning.skilclasses.activities.ProfileActivity.isDownloadsDocument;
import static com.learning.skilclasses.activities.ProfileActivity.isExternalStorageDocument;
import static com.learning.skilclasses.activities.ProfileActivity.isMediaDocument;*/

public class UploadAssignment extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onBackPressed() {
        if (progressBar.getVisibility() == VISIBLE) {
            return;
        } else
            super.onBackPressed();
    }

    UserSession userSession;
    String subject;
    String a;
    private final static int FILE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assignment);
        progressBar = findViewById(R.id.progress_bar1);
//        userSession = new UserSession(this);
        subject = getIntent().getStringExtra("subject");
        textView = findViewById(R.id.text);
        userSession = new UserSession(getApplicationContext());
        progressBar.setVisibility(VISIBLE);
        textView.setVisibility(VISIBLE);
        //   uploadImagePhp();
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setMaxSelection(1)
                .setShowFiles(true)
                .setShowImages(false)
                .setShowAudios(false)
                .setShowVideos(false)
                .setSuffixes("pdf")
                .setSkipZeroSizeFiles(true)
                .build());
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    public void uploadImagePhp() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //  if (requestCode == 1) {
        switch (requestCode) {
            case FILE_REQUEST_CODE:
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                //Do something with files
                Log.d("FILE_PATH", files.get(0).getPath() + "");
                uploadFile(files.get(0).getPath());
                break;
            //    }
//            try {
//                Uri selectedFileUri = data.getData();
//                final String path = getFilePath(selectedFileUri);
//                File file = new File(path);
//                Log.d("LOCATION", file.getAbsolutePath());
//                uploadFile(file.getAbsolutePath());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

//    public String getFilePath(Uri uri) throws URISyntaxException {
//        String selection = null;
//        String[] selectionArgs = null;
//        // Uri is different in versions after KITKAT (Android 4.4), we need to
//        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(UploadAssignment.this, uri)) {
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                return Environment.getExternalStorageDirectory() + "/" + split[1];
//            } else if (isDownloadsDocument(uri)) {
//                final String id = DocumentsContract.getDocumentId(uri);
//                uri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//            } else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//                if ("image".equals(type)) {
//                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                } else if ("file".equals(type)) {
//                    uri = MediaStore.Files.getContentUri("external");
//                }
//                selection = "_id=?";
//                selectionArgs = new String[]{
//                        split[1]
//                };
//            }
//        }
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//
//           /* if (isGooglePhotosUri(uri)) {
//                return uri.getLastPathSegment();
//            }*/
//
//            String[] projection = null;/*{
//                    MediaStore.Images.Media.DATA
//            };*/
//            Cursor cursor = null;
//            try {
//                cursor = getContentResolver()
//                        .query(uri, projection, selection, selectionArgs, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }

    public void uploadFile(String sourceFileUri) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            final File file = new File(sourceFileUri);
            Uri uri = Uri.fromFile(file);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            String imageName = file.getAbsolutePath();
            //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", imageName, RequestBody.create(MediaType.parse(mime), file))
                    .addFormDataPart("email", userSession.getUserDetails().get(UserSession.KEY_EMAIL))
                    .addFormDataPart("class", userSession.getUserDetails().get(UserSession.KEY_CATEGORY))
                    .addFormDataPart("category", userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY))
                    .addFormDataPart("subcategory", subject)
                    .addFormDataPart("status", "true")
                    .build();

            Request request = new Request.Builder()
                    .url(ApiUrl.UPLOAD_ASSIGNMENT)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("RESPONSE_1", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        try {
                            a = response.body().string();
                            Log.d("RESPONSE_1", a);
                        } catch (IOException e) {
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        //Toast.makeText(UploadAssignment.this, "Updated", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(UploadAssignment.this, , Toast.LENGTH_SHORT).show();
                        /*FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.frame_layout, new AssignmentFragment());
                        fragmentTransaction4.commit();*/
                        try {

                            JSONObject jsonObject = new JSONObject(a);
                            Toast.makeText(getApplicationContext(), "Assignment Submitted Successfully", Toast.LENGTH_SHORT).show();
                            if (jsonObject.getString("status") == "true") {
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
