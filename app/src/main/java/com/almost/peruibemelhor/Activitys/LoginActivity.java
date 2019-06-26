package com.almost.peruibemelhor.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.almost.peruibemelhor.Activitys.main.MainActivity;
import com.almost.peruibemelhor.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 100;
    private static final String USER_KEY = "current_user";

    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private Button mFacebookLogin;
    private Button mGoogleLogin;


    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        sharedPreferences = getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        startSession(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFacebookLogin = findViewById(R.id.sign_in_facebook);
        mGoogleLogin = findViewById(R.id.sign_in_google);

        mGoogleLogin.setOnClickListener(this);
        mFacebookLogin.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View v) {
        mFacebookLogin.setEnabled(false);
        mGoogleLogin.setEnabled(false);

        switch (v.getId()) {
            case R.id.sign_in_google:
                signInGoogle();
                break;
            case R.id.sign_in_facebook:
                signInFacebook();
                break;
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInFacebook() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Snackbar.make(findViewById(R.id.login_layout), "Sem acesso a internet.", Snackbar.LENGTH_SHORT).show();
                // ...
            }
        }

        mFacebookLogin.setEnabled(true);
        mGoogleLogin.setEnabled(true);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startSession(user);
                        } else {
                            try {
                                Toast.makeText(LoginActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                throw task.getException();

                            }catch (FirebaseNetworkException e){

                                GoogleSignInAccount lastLogin = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                                startSessionOffline(lastLogin);

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    private void startSession(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.i(TAG, "SignMode: Online");
            Log.i(TAG, "SignUser: "+currentUser.getEmail());
            openMain(currentUser.getEmail());
        }
    }

    private void startSessionOffline(GoogleSignInAccount lastLogin) {
        if (lastLogin != null) {
            Log.i(TAG, "SignMode: LastLogin");
            Log.i(TAG, "SignUser: "+lastLogin.getEmail());
            openMain(lastLogin.getEmail());
        }else{
            String result = sharedPreferences.getString(USER_KEY, null);
            openMain(result);
        }
    }

    private void openMain(String currentUserEmail){
        if (currentUserEmail != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_KEY, currentUserEmail);
            editor.apply();

            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
        }else{
            // If sign in fails, display a message to the user.
            Snackbar.make(findViewById(R.id.login_layout), "Crie uma conta para participar", Snackbar.LENGTH_SHORT).show();
        }
    }
}
