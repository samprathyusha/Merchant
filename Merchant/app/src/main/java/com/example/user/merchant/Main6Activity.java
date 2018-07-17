package com.example.user.merchant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;




public class Main6Activity extends AppCompatActivity {
    TextView result,result2;
    ImageView image;
    Button button;
    private ProgressDialog pd;
    BufferedInputStream is;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        ImageView imagview= (ImageView) findViewById(R.id.logo);

        result = (TextView) findViewById(R.id.textView);
        result2 = (TextView) findViewById(R.id.textview1);
        button=(Button)findViewById(R.id.button);


        pd = new ProgressDialog(Main6Activity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        if (!SharedPrefManager.getInstance(this).isLoggedin()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }


        getSqlDetails();


        Intent k = getIntent();
        String orderid = k.getStringExtra("orderid");
        Log.e("orderid",orderid);
        String producttype = k.getStringExtra("producttype");
        String organization = k.getStringExtra("organization");
        Integer orgid = k.getIntExtra("orgid",0);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent l=new Intent(Main6Activity.this,Main7Activity.class);
            startActivity(l);
        }
    });


    }

    public   void getSqlDetails() {
        Intent k = getIntent();
        String orderid = k.getStringExtra("orderid");
        Log.e("order",orderid);

        String url = "http://192.168.1.12/farmer1.php?orderid=" + orderid;
        getDetails();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String description = jsonobject.getString("description");
                                String quantity = jsonobject.getString("quantity");
                                String quantityin = jsonobject.getString("quantityin");
                                String quality = jsonobject.getString("quality");
                                String price = jsonobject.getString("price");
                                String dateofpost=jsonobject.getString("dateofpost");
                                String phone=jsonobject.getString("phone");
                                result.setText(" Description -" + description + "\n Quantity -" + quantity + "\n Quantityin -" + quantityin + "\n Quality -" + quality + "\n Price -" + price+"\n DateOfPost-"+dateofpost+"\n Phone -"+phone);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        } }
                }

        );
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(Main6Activity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


    public   void getDetails() {

        Intent m = getIntent();
        String orderid = m.getStringExtra("orderid");

        String url = "http://192.168.1.12/downloadimage.php?orderid="+orderid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String image = jsonobject.getString("image");
                                ImageView imagview= (ImageView) findViewById(R.id.logo);
                                String src = (image);
                                new AsyncTaskLoadImage(imagview).execute(src);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        } }
                }

        );
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(Main6Activity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    public class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;

        public AsyncTaskLoadImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                int width, height;
                height = bitmap.getHeight();
                width = bitmap.getWidth();

                Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bmpGrayscale);
                Paint paint = new Paint();
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                //ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                //paint.setColorFilter(f);
                c.drawBitmap(bitmap, 0, 0, paint);
                imageView.setImageBitmap(bmpGrayscale);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}





