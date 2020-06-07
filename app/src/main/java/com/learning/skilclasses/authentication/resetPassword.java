package com.learning.skilclasses.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class resetPassword extends AppCompatActivity {

    String email;
    EditText passID,cpassID;
    private static final String RESET_URL = "http://digitalcatnyx.store/api/resetpassword.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Bundle bundle=getIntent().getExtras();
        email=bundle.getString("email");
        passID=findViewById(R.id.password);
        cpassID=findViewById(R.id.cpassword);
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(passID.getText().toString().isEmpty() && cpassID.getText().toString().isEmpty())){
                    if (!isValidPassword(passID.getText().toString())){
                        Toast.makeText(getApplicationContext(), "password must contain atleast one digit, one small character,one capital character", Toast.LENGTH_SHORT).show();
                    }
                    else if(passID.getText().toString().equals(cpassID.getText().toString())){
                        reset();
                    }
                    else{
                        Toast.makeText(resetPassword.this,"Password Not Matched",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(resetPassword.this,"Must Fill the fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void reset() {

        StringRequest request = new StringRequest(Request.Method.POST, RESET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //progressBar.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(resetPassword.this,""+jsonObject,Toast.LENGTH_LONG).show();
                            if(jsonObject.getString("status").equals("1")){
                                Toast.makeText(resetPassword.this,"Reset Successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(resetPassword.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error: "+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Response errorr"+error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",passID.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
