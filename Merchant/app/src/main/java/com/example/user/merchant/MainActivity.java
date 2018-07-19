package com.example.user.merchant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AutoCompleteTextView username;
    AutoCompleteTextView password;
    TextView textView;

    private Button Submit ;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedin()){
            finish();
            Intent h=new Intent(MainActivity.this,Main3Activity.class);
            h.putExtra("organization",SharedPrefManager.getInstance(this).getOrganisationname());
            h.putExtra("orgid",SharedPrefManager.getInstance(this).getOrgid());

            startActivity(h);
            return;
        }
        username= (AutoCompleteTextView) findViewById(R.id.lmn);
        password=(AutoCompleteTextView)findViewById(R.id.userPassword);
        textView=(TextView)findViewById(R.id.signuplink);
        Submit = (Button) findViewById(R.id.loginbutton);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        Submit.setOnClickListener(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });
    }


    private void userLogin(){
        final  String Text = username.getText().toString().trim();
        final  String Text1= password.getText().toString().trim();
        StringRequest stringRequest= new StringRequest(
                Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    if(!obj.getBoolean("error")){
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                obj.getInt("orgid"),
                                obj.getString("organization"),
                                obj.getString("phone")
                        );

                        String k=obj.getString("organization");
                        Integer m=obj.getInt("orgid");
                        Intent i=new Intent(MainActivity.this,Main3Activity.class);
                        i.putExtra("organization",SharedPrefManager.getInstance(getApplicationContext()).getOrganisationname());

                        i.putExtra("orgid",SharedPrefManager.getInstance(getApplicationContext()).getOrgid());

                        startActivity(i);

                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("username", Text);
                params.put("password",Text1);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



    }


    @Override
    public void onClick(View v) {
        userLogin();
    }
}
