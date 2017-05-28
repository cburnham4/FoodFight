package letshangllc.foodfight.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import letshangllc.foodfight.R;

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
        setupFirebase();
        findViews();
    }

    private void setupFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void findViews(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    public void registerOnClick(View view){
        String email = etEmail.getText().toString(), password = etPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_LONG).show();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_LONG).show();
                            login(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void login(FirebaseUser user){
        finish();
    }
}
