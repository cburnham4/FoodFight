package letshangllc.foodfight.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.Utils;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    /* Firebase */
    private FirebaseAuth firebaseAuth;

    /* Views */
    private EditText etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        setupToolbar();
        findViews();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void findViews(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
    }

    public void registerOnClick(View view){
        String email = etEmail.getText().toString(), password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Utils.makeToast(this, "Please fill out both fields");
            return;
        }
        if(!password.equals(confirmPassword)){
            Utils.makeToast(this, "Passwords do not match");
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Logging in", "Please Wait...", true, false);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Utils.makeToast(RegisterActivity.this, "User Created");
                            login(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Utils.makeToast(RegisterActivity.this, "Registration failed.");

                        }

                        // ...
                    }
                });
    }

    private void login(FirebaseUser user){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
