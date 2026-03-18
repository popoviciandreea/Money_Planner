package com.example.moneyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private TextView logInActivityEmail;

    private TextView logInActivityPassword;

    private Button logInActivityLogInBtn;

    private Button logInActivitySignUpBtn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();
        initComponents();

        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }
    }

    private void initComponents() {
        logInActivityEmail= findViewById(R.id.logInActivityEmail);
        logInActivityPassword= findViewById(R.id.logInActivityPassword);
        logInActivityLogInBtn= findViewById(R.id.logInActivityLogInBtn);
        logInActivitySignUpBtn= findViewById(R.id.logInActivitySignUpBtn);

        logInActivityLogInBtn.setOnClickListener(verifyUserEventListener());
        logInActivitySignUpBtn.setOnClickListener(createUserEventListener());
    }

    private View.OnClickListener createUserEventListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener verifyUserEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String email = logInActivityEmail.getText().toString();
                    String password = logInActivityPassword.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"User Successfully logged in!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"User failed to log in",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        };
    }

    private boolean validate(){
        if(logInActivityEmail.getText() == null
                || logInActivityEmail.getText().toString().trim().isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(logInActivityEmail.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(),"Invalid Email Address!",Toast.LENGTH_LONG).show();
            return false;
        }

        if(logInActivityPassword.getText() == null
                || logInActivityPassword.getText().toString().trim().isEmpty()
                || logInActivityPassword.length()<6){
            Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}