package com.example.user.merchant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.Map;

public class Main7Activity extends AppCompatActivity {
    AutoCompleteTextView text;
    String s1;
    String s2="request";
    String url="http://prathyushateja710.000webhostapp.com/connection/updetails.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        text=(AutoCompleteTextView) findViewById(R.id.text2);
        Button button=(Button)findViewById(R.id.button);
        final String offerprice=text.getText().toString();
        Intent k=getIntent();
     String orderid= k.getStringExtra("orderid");
       String organization= k.getStringExtra("organization");
      String status=  k.getStringExtra("status");
      String orgsdetails=  k.getStringExtra("orgsdetails");
      Integer orgid=  k.getIntExtra("orgid",0);
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final String offerprice=text.getText().toString();
              Intent k=getIntent();
              final String orderid= k.getStringExtra("orderid");
              String organization= k.getStringExtra("organization");
              String status=  k.getStringExtra("status");
              String orgsdetails=  k.getStringExtra("orgsdetails");
              Integer orgid=  k.getIntExtra("orgid",0);
              if(status.equals("request")){
                  s1=orgsdetails+"/"+organization+"*"+orgid+"#"+offerprice;



              }
              else{
                  s1=organization+"*"+orgid+"#"+offerprice;
              }

              StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                  @Override
                  public void onResponse(String ServerResponse) {

                      Toast.makeText(Main7Activity.this, ServerResponse, Toast.LENGTH_SHORT).show();

                  }
              }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      Toast.makeText(Main7Activity.this, error.toString(), Toast.LENGTH_SHORT).show();

                  }
              }){
                  @Override
                  protected Map<String, String> getParams()  {


                      Map<String,String> params=new HashMap<>();
                      params.put("status",s2);
                      params.put("orgsdetails",s1);
                      params.put("orderid",orderid);

                      return params;
                  }
              };
              RequestQueue requestQueue= Volley.newRequestQueue(Main7Activity.this);
              requestQueue.add(stringRequest);
              Intent l=new Intent(Main7Activity.this,Main3Activity.class);
              l.putExtra("organization",SharedPrefManager.getInstance(getApplicationContext()).getOrganisationname());
              l.putExtra("orgid",SharedPrefManager.getInstance(getApplicationContext()).getOrgid());
              startActivity(l);



          }
      });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.menuOrders:
                Intent j=getIntent();
                Integer orgid = j.getIntExtra("orgid", 0);
                String searchkey="*"+orgid+"#";
                Intent m=new Intent(Main7Activity.this,Main8Activity.class);
                m.putExtra("searchkey",searchkey);

                startActivity(m);
                break;


        }
        return  super.onOptionsItemSelected(item);
    }

}
