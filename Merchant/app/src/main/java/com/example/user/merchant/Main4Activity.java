package com.example.user.merchant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
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

public class Main4Activity extends AppCompatActivity {
    private ListView listView;
    TextView Text;
    String[] producttype;
    String[] url;
    BufferedInputStream is;
    String line=null;
    String result=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        if (!SharedPrefManager.getInstance(this).isLoggedin()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        listView = (ListView) findViewById(R.id.listView);
        Text = (TextView) findViewById(R.id.Texttvv);
        Intent j = getIntent();
        String product = j.getStringExtra("product");
        Text.setText(product);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView=new CustomListView(this,producttype,url);
        listView.setAdapter(customListView);


    }

    private void collectData(){
        String text=Text.getText().toString();
        //connection
        try{
            URL url= new URL("http://192.168.1.12/get_product.php?product="+text);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());



        }catch(Exception e){
            e.printStackTrace();
        }

        //content
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while((line=br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        }catch(Exception e){
            e.printStackTrace();
        }
//JSON
        try{

            JSONArray ja=new JSONArray(result);
            JSONObject jo=null;
            producttype=new String[ja.length()];
            url= new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                producttype[i]=jo.getString("producttype");
                url[i]=jo.getString("url");



            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

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

                break;


        }
        return  super.onOptionsItemSelected(item);
    }
    public class CustomListView extends ArrayAdapter<String> {
        private String[] producttype;
        private String[]url;
        private Activity context;
        Bitmap bitmap;


        public  CustomListView(Activity context,String[] producttype,String[] url){
            super(context,R.layout.list_activity,producttype);
            this.context=context;
            this.producttype=producttype;
            this.url=url;
        }
        public View getView(final int position, View convertView, ViewGroup parent){
            View r=convertView;
            ViewHolder viewHolder=null;
            if(r==null){
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r=layoutInflater.inflate(R.layout.list_activity,parent,false);
                viewHolder= new CustomListView.ViewHolder(r);
                r.setTag(viewHolder);

            }


            else {
                viewHolder =(ViewHolder) convertView.getTag();

            }

            viewHolder.text.setText(producttype[position]);
            new GetImagefromURL(viewHolder.image).execute(url[position]);
            final ViewHolder finalViewHolder = viewHolder;
            r.setOnClickListener(new AdapterView.OnClickListener(){


                @Override
                public void onClick(View v) {
                    Intent R = getIntent();
                    String organization = R.getStringExtra("organization");
                    Integer orgid = R.getIntExtra("orgid", 0);
                    String product=R.getStringExtra("product");


                    //Bitmap bitmap=((BitmapDrawable)finalViewHolder.image.getDrawable()).getBitmap();
                    //ByteArrayOutputStream bs=new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.PNG,50,bs);
                    Intent j=new Intent(Main4Activity.this,Main5Activity.class);
                    //Drawable drawable=finalViewHolder.image.getDrawable();


                    // j.putExtra("image", bs.toByteArray());
                    j.putExtra("producttype", finalViewHolder.text.getText().toString());
                    j.putExtra("organization",organization);
                    j.putExtra("orgid",orgid);
                    j.putExtra("product",product);

                    startActivity(j);



                }



            });

            return r;
        }
        class ViewHolder{
            TextView text;
            ImageView image;
            ViewHolder(View v){
                text=(TextView)v.findViewById(R.id.textName);
                image=(ImageView)v.findViewById(R.id.imageView);

            }
        }

        public class GetImagefromURL extends AsyncTask<String,Void,Bitmap>{
            ImageView imgView;
            public GetImagefromURL(ImageView imgv){
                this.imgView=imgv;
            }

            @Override
            protected Bitmap doInBackground(String... url) {
                String urldisplay=url[0];
                bitmap=null;

                try{

                    InputStream ist=new java.net.URL(urldisplay).openStream();
                    bitmap= BitmapFactory.decodeStream(ist);

                }catch(Exception ex){
                    ex.printStackTrace();
                }
                return bitmap;
            }

            protected  void onPostExecute(Bitmap bitmap){
                super.onPostExecute(bitmap);
                imgView.setImageBitmap(bitmap);
            }


        }






    }



}











