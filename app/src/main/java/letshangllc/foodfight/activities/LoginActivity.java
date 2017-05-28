package letshangllc.foodfight.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import letshangllc.foodfight.R;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    /* Firebase */
    private FirebaseAuth firebaseAuth;

    /* Views */
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        findViews();
    }

    private void findViews(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Check if user is logged in */
        FirebaseUser user = firebaseAuth.getCurrentUser();


    }

    public void loginOnClick(View view){

    }

    public void registerOnClick(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
