package com.sport365.badminton.base;


import android.app.Application;

public class CustomApplication extends Application {
    @Override 
    public void onCreate() { 
        
        super.onCreate(); 
        
        CustomException customException = CustomException.getInstance(); 
        
        customException.init(getApplicationContext()); 
    }  
}