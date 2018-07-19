package com.example.user.merchant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    AutoCompleteTextView text1,text2,text3,text4,text5,text6;

    TextView text7;
    Button submit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        text1 = findViewById(R.id.organizationname);
        text2 = findViewById(R.id.username);
        text3 = findViewById(R.id.mail);
        text4 = findViewById(R.id.Mobilenumber);
        submit = findViewById(R.id.signupbutton);
        text5=findViewById(R.id.password);
        text6=findViewById(R.id.scpassword);
        text7 = findViewById(R.id.loginlink);
        progressDialog = new ProgressDialog(this);


        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(i);
            }
        });
        submit.setOnClickListener(this);
        }

    private void registerUser() {
        if (text1.getText().length() < 6) {
            Toast.makeText(Main2Activity.this, "  organizationname must be six charachters", Toast.LENGTH_SHORT).show();
            stay();
        } else {
            if (text2.getText().length() < 6) {
                Toast.makeText(Main2Activity.this, "username must be six charachters", Toast.LENGTH_SHORT).show();
stay();
            } else {
                if (!text5.getText().toString().equals(text6.getText().toString())) {
                    Toast.makeText(Main2Activity.this, "password does not match", Toast.LENGTH_SHORT).show();
stay();
                } else {
                    if (text4.getText().length() > 10) {
                        Toast.makeText(Main2Activity.this, "enter valid number", Toast.LENGTH_SHORT).show();
                        stay();
                    } else {
                        if (text4.getText().length() < 10) {
                            Toast.makeText(Main2Activity.this, "enter valid number", Toast.LENGTH_SHORT).show();
                            stay();
                        }
                        else{
                            if (text3.getText().toString().length()==0){
                                Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
                                stay();
                            }
                            else{if(text1.getText().length()!=0&&text2.getText().length()!=0&&text3.getText().length()!=0&&text4.getText().length()!=0&&text5.getText().length()!=0&&text6.getText().length()!=0) {
                                final String organization = text1.getText().toString().trim();
                                final String username = text2.getText().toString().trim();
                                final String email = text3.getText().toString().trim();
                                final String phone = text4.getText().toString().trim();
                                final String password = text5.getText().toString().trim();
                                progressDialog.setMessage("Registering user..");
                                progressDialog.show();

                                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                        Constants.URL_REGISTER, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.hide();
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("organization", organization);
                                        params.put("password", password);
                                        params.put("phone", phone);
                                        params.put("email", email);
                                        return params;
                                    }
                                };
                                //RequestQueue requestQueue = Volley.newRequestQueue(this);
                                //requestQueue.add(stringRequest);
                                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
                            }
                                Intent k=new Intent(Main2Activity.this,MainActivity.class);
                                startActivity(k);
                            }

                        }
                    }

                }
            }

        }





    }
public void stay(){
        Intent j= new Intent(Main2Activity.this,Main2Activity.class);
        startActivity(j);
}
    @Override
    public void onClick(View v) {

        registerUser();


    }


}
