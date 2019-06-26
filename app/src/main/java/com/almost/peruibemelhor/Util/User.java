package com.almost.peruibemelhor.Util;

import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;

public class User {
    String TAG = "User";

    private final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

    public String getUserUID(){
        Log.i(TAG,"UserUID: "+mAuth.getUid());
        return mAuth.getUid();
    }
}
