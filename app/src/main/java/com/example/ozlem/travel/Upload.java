package com.example.ozlem.travel;

import com.google.firebase.database.Exclude;

/**
 * Created by ozlem on 10.06.2018.
 */

public class Upload {
    private  String mName;
    private  String mImageUrl;
    private  String mKey;

    public  Upload(){

    }
    public  Upload(String name,String imageurl){
        if(name.trim().equals("")){
            name="No name";
        }
        mName=name;
        mImageUrl=imageurl;
    }
    public  String getName(){
        return  mName;
    }
    public void setName(String name){
        mName=name;
    }
    public String getImageUrl(){
        return  mImageUrl;
    }
    public  void setImageUrl(String imageUrl){
        mImageUrl=imageUrl;
    }
    @Exclude
    public String getKey(){return mKey;}

    @Exclude
    public void setKey(String key){
        mKey=key;
    }
}

