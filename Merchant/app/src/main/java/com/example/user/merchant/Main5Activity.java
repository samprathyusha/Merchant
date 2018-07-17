package com.example.user.merchant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main5Activity extends AppCompatActivity {
    private ListView listView;

    String[] producttype;
    String[] url;
    String[] username;
    String[] orderid;
    BufferedInputStream is;
    String line=null;
    String result=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        if (!SharedPrefManager.getInstance(this).isLoggedin()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        listView=(ListView)findViewById(R.id.listView);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView=new CustomListView(this,producttype,url,username,orderid);
        listView.setAdapter(customListView);

    }

    private void collectData(){
        Intent j=getIntent();
        j.getStringExtra("product");
        j.getStringExtra("producttype");
        j.getStringExtra("organization");
        j.getIntExtra("orgid",0);
        String product=j.getStringExtra("producttype");

        //connection
        try{
            URL url= new URL("http://192.168.1.12/farmer.php?product="+product);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }//content
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
            username=new String[ja.length()];
            orderid=new String[ja.length()];


            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                producttype[i]=jo.getString("producttype");
                url[i]=jo.getString("url");
                username[i]=jo.getString("username");
                orderid[i]=jo.getString("orderid");

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }
    public  class CustomListView extends ArrayAdapter<String> {
        private String[] producttype;
        private String[]url;
        private  String[] username;
        private String[] orderid;
        private Activity context;
        Bitmap bitmap;


        public  CustomListView(Activity context, String[] producttype, String[] url,String[] username,String[] orderid) {
            super(context, R.layout.list_item, producttype);
            this.context = context;
            this.producttype = producttype;
            this.url = url;
            this.username=username;
            this.orderid=orderid;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View r = convertView;
            ViewHolder viewHolder = null;
            if (r == null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.text.setText(producttype[position]);
            viewHolder.text2.setText(orderid[position]);
            new GetImagefromURL(viewHolder.image).execute(url[position]);
            viewHolder.text4.setText(username[position]);
            final ViewHolder finalViewHolder = viewHolder;
            if(viewHolder.text.getText().length()!=0&& viewHolder.text2.getText().length()!=0&&viewHolder.text4.getText().length()!=0) {
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent R = getIntent();
                        String product = R.getStringExtra("product");
                        String producttype = R.getStringExtra("producttype");
                        String organization = R.getStringExtra("organization");
                        Integer orgid = R.getIntExtra("orgid", 0);

                        Intent j = new Intent(Main5Activity.this, Main6Activity.class);
                        j.putExtra("orderid", finalViewHolder.text2.getText().toString());
                        Log.e("3", finalViewHolder.text2.getText().toString());
                        j.putExtra("organization", organization);
                        j.putExtra("orgid", orgid);
                        j.putExtra("product", product);
                        j.putExtra("producttype", producttype);

                        startActivity(j);


                    }
                });
            }
            else{
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
            }
                return r;
            }

        class ViewHolder{


            TextView text,text4,text2;
            ImageView image;
            ViewHolder(View v){

                text=(TextView)v.findViewById(R.id.textName);
                text4=(TextView)v.findViewById(R.id.text2);
                text2=(TextView)v.findViewById(R.id.textName1);


                image=(ImageView)v.findViewById(R.id.imageView);

            }
        }


        public class GetImagefromURL extends AsyncTask<String,Void,Bitmap> {
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





