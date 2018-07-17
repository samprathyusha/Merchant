package com.example.user.merchant;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME="mysharedpref12";
    private static final String KEY_ORGANISATION_NAME="organization";
    private static final String KEY_ORG_NUMBER="phone";
    private static final String KEY_ORG_ID="orgid";
    private SharedPrefManager(Context context){
        mCtx =context;
    }
    public static synchronized  SharedPrefManager getInstance(Context context){
        if(mInstance==null){
            mInstance =  new SharedPrefManager(context);
        }
        return  mInstance;
    }
    public boolean userLogin(int orgid,String organization,String phone){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(KEY_ORG_ID,orgid);
        editor.putString(KEY_ORGANISATION_NAME,organization);
        editor.putString(KEY_ORG_NUMBER,phone);
        editor.apply();
        return  true;
    }

    public boolean isLoggedin(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_ORGANISATION_NAME,null)!=null){
            return true;
        }

        return  false;




    }


    public String getOrganisationname(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ORGANISATION_NAME,null);

    }
    public String getPhone(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ORG_NUMBER,null);

    }
    public Integer getOrgid(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ORG_ID,0);

    }

    public boolean logout(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
