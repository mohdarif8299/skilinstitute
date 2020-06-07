package com.learning.skilclasses.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.instamojo.android.Instamojo;
import com.learning.skilclasses.R;
import com.learning.skilclasses.Utilities.ApiUrl;
import com.learning.skilclasses.preferences.UserSession;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements Instamojo.InstamojoPaymentCallback {
    Toolbar toolbar;
    UserSession userSession;
    EditText name, email, phone, purpose, amount;
    Button pay;
    TextView req;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        userSession = new UserSession(this);
        ButterKnife.bind(this, this);
        Instamojo.getInstance().initialize(getApplicationContext(), Instamojo.Environment.PRODUCTION);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        amount = findViewById(R.id.amount);
        purpose = findViewById(R.id.description);

        pay = findViewById(R.id.pay);

        req = findViewById(R.id.required);

        name.setText(userSession.getUserDetails().get(UserSession.KEY_NAME));
        email.setText(userSession.getUserDetails().get(UserSession.KEY_EMAIL));
        amount.setText(userSession.getUserDetails().get(UserSession.KEY_PRICE));
        purpose.setText(userSession.getUserDetails().get(UserSession.KEY_CATEGORY) + " " + userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY));


        pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String num = phone.getText() + "";
                if (num.length() == 0) {
                    req.setText("Required");
                    req.setVisibility(View.VISIBLE);
                } else if (num.length() < 10) {
                    req.setText("Required 10 Digit");
                    req.setVisibility(View.VISIBLE);
                } else {

                    Dialog dialog = new Dialog(PaymentActivity.this);
                    dialog.setContentView(R.layout.upi_layout);
                    Button nextBtn = dialog.findViewById(R.id.next);
                    nextBtn.setOnClickListener(v -> {
                        if (dialog.isShowing()) dialog.dismiss();
                        progressBar.setVisibility(View.VISIBLE);
                        phone.setEnabled(false);
                        pay.setEnabled(false);
                        sendPaymentRequest();
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    //CreatePayment("bfb58436-4901-469a-afb7-eb1186dc6673");
                }
                /*if (phone.getText().toString()!=null){
                String num=phone.getText().toString();
                Toast.makeText(getApplicationContext(),num.length(),Toast.LENGTH_LONG).show();

                }*/
                //    Toast.makeText(getApplicationContext(), "click" + userSession.getUserDetails().get(UserSession.KEY_ID), Toast.LENGTH_LONG).show();


            }
        });
    }

    private void sendPaymentRequest() {

        StringRequest str = new StringRequest(Request.Method.POST, "http://digitalcatnyx.store/Instamojo_API/Test.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d("RES111", response.toString());
                String orderId = response;
                //   Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                //Instamojo.getInstance().initiatePayment(MainActivity.this,response.toString(),);
                CreatePayment(orderId);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showToast("Something went wrong plase try again later");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("purpose", purpose.getText().toString());
                params.put("amount", amount.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("name", name.getText().toString());


                return params;
            }
        };
        str.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(str);

    }

    private void CreatePayment(String orderId) {

        Instamojo.getInstance().initiatePayment(this, orderId, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MyCourseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInstamojoPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {
        Log.d("!121", "Payment complete");
        showToast("Payment complete. Order ID: " + orderID + ", Transaction ID: " + transactionID
                + ", Payment ID:" + paymentID + ", Status: " + paymentStatus);
        Log.d("tID", transactionID);
        Log.d("oID", orderID);
        Log.d("psID", paymentStatus);
        if (paymentStatus.equals("Credit")) {
            userSession.setStatus("true");
            Log.d("Ust", userSession.getUserDetails().get(UserSession.KEY_STATUS));
            Toast.makeText(getApplicationContext(), "click" + userSession.getUserDetails().get(UserSession.KEY_STATUS), Toast.LENGTH_LONG).show();
            updatepaymentStatus();
        }


    }

    private void updatepaymentStatus() {
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.UPDATE_PAYMENT_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Psyt", response);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showToast("Something Wend wrong " + error + " please try again later");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", userSession.getUserDetails().get(UserSession.KEY_ID));
                params.put("P_status", "true");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPaymentCancelled() {
        Log.d("P_status", "Payment cancelled");
        showToast("Payment cancelled by user");
        progressBar.setVisibility(View.GONE);
        phone.setEnabled(true);
        pay.setEnabled(true);


    }

    @Override
    public void onInitiatePaymentFailure(String errorMessage) {
        Log.d("P_status", "Initiate payment failed");
        showToast("Initiating payment failed. Error: " + errorMessage);
        progressBar.setVisibility(View.GONE);
        phone.setEnabled(true);
        pay.setEnabled(true);

    }

}
