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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main8Activity extends AppCompatActivity {
    private ListView listView;
    String[] producttype;
    String[] url;
    String[] orderid;
    String[] phone;
    String[] quantityin;
    String[] quality;
    String[] quantity;
    String[] username;
    String[] status;
    BufferedInputStream is;
    String line=null;
    String result=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);


        if (!SharedPrefManager.getInstance(this).isLoggedin()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        listView=(ListView)findViewById(R.id.listView);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView=new CustomListView(this,producttype,url,orderid,phone,quantityin,
                quality,quantity,username,status);
        listView.setAdapter(customListView);

    }

    private void collectData(){
        Intent j=getIntent();
       String orgid= j.getStringExtra("searchkey");

        //connection
        try{
            URL url= new URL("http://prathyushateja710.000webhostapp.com/connection/orgorder.php?orgid="+orgid);
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
            orderid=new String[ja.length()];
            phone=new String[ja.length()];
            quantityin=new String[ja.length()];
            quality=new String[ja.length()];
            quantity=new String[ja.length()];
            username=new String[ja.length()];
            status=new String[ja.length()];


            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                producttype[i]=jo.getString("producttype");
                url[i]=jo.getString("url");
                orderid[i]=jo.getString("orderid");
                phone[i]=jo.getString("phone");
                quantityin[i]=jo.getString("quantityin");
                quality[i]=jo.getString("quality");
                quantity[i]=jo.getString("quantity");
                username[i]=jo.getString("username");
                status[i]=jo.getString("status");


            }
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }



    public  class CustomListView extends ArrayAdapter<String> {
        private String[] producttype;
        private String[]url;
        private String[] orderid;
        private String[] phone;
        private String[] quantityin;
        private String[] quality;
        private String[] quantity;
        private String[] username;
        private String[] status;
        private Activity context;
        Bitmap bitmap;


        public  CustomListView(Activity context, String[] producttype, String[] url,String[] orderid, String[] phone,String[] quantityin,
                               String[] quality,String[] quantity,String[] username, String[] status) {
            super(context, R.layout.last_layout, producttype);
            this.context = context;
            this.producttype = producttype;
            this.url = url;
            this.orderid=orderid;
            this.phone=phone;
            this.quantityin=quantityin;
            this.quality=quality;
            this.quantity=quantity;
            this.username=username;
            this.status=status;
        }
        public View getView(final int position, View convertView, ViewGroup parent){
            View r=convertView;
            ViewHolder viewHolder=null;
            if(r==null){
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r=layoutInflater.inflate(R.layout.last_layout,parent,false);
                viewHolder= new ViewHolder(r);
                r.setTag(viewHolder);


            }


            else {
                viewHolder =(ViewHolder) convertView.getTag();

            }
            Intent k=getIntent();
           Integer orgid = k.getIntExtra("orgid",0);
           String org=orgid.toString();


            viewHolder.text1.setText(producttype[position]);
            new GetImagefromURL(viewHolder.image).execute(url[position]);
            viewHolder.text2.setText(phone[position]);
            viewHolder.text3.setText(quantity[position]);
            viewHolder.text4.setText(quality[position]);
            viewHolder.text5.setText(quantityin[position]);
            viewHolder.text6.setText(username[position]);
            viewHolder.text8.setText(orderid[position]);
            if(status[position].equals("request")){
                viewHolder.text7.setText(" this request is pending");

            }
            else if(status[position].equals(org)){
                viewHolder.text7.setText("congrats farmer has accepted your request");
            }
            else{
                viewHolder.text7.setText("sorry the farmer sold for other merchant");
            }


            final ViewHolder finalViewHolder = viewHolder;
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });

            return r;
        }
        class ViewHolder{


            TextView text1,text2,text3,text4,text5,text6,text7,text8;
            ImageView image;
            ViewHolder(View v){
                text2=(TextView)v.findViewById(R.id.textviewtwo);
                text1=(TextView)v.findViewById(R.id.textviewone);
                text3=(TextView)v.findViewById(R.id.textviewthree);
                text4=(TextView)v.findViewById(R.id.textviewfour);
                text5=(TextView)v.findViewById(R.id.textviewfive);
                text6=(TextView)v.findViewById(R.id.textviewsix);
                text7=(TextView)v.findViewById(R.id.textviewseven);
                text8=(TextView)v.findViewById(R.id.textvieweight);

                image=(ImageView)v.findViewById(R.id.imageButton);

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
