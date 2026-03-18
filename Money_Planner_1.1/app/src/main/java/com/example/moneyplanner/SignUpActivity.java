package com.example.moneyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyplanner.classes.User;
import com.example.moneyplanner.firebase.Callback;
import com.example.moneyplanner.firebase.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private TextView signUpActivityEmail;
    private TextView signUpActivityPassword;
    private Button signUpActivitySignUpBtn;
    private ImageButton goBackBtn;

    private List<User> users = new ArrayList<>();

    private FirebaseService firebaseService;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        initComponents();
        firebaseService = FirebaseService.getInstance();
        firebaseService.attachDataChangeUserEventListener(dataChangeCallback());

        if(firebaseAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void initComponents() {
        signUpActivityEmail=findViewById(R.id.signUpActivityEmail);
        signUpActivityPassword=findViewById(R.id.signUpActivityPassword);
        signUpActivitySignUpBtn=findViewById(R.id.signUpActivitySignUpBtn);
        goBackBtn=findViewById(R.id.goBackBtn);

        signUpActivitySignUpBtn.setOnClickListener(createUserEventListener());
        goBackBtn.setOnClickListener(goBackEventListener());

    }

    private Callback<List<User>> dataChangeCallback() {
        return new Callback<List<User>>() {
            @Override
            public void runResultOnUiThread(List<User> result) {
                if (result != null) {
                    users.clear();
                    users.addAll(result);
                }
            }
        };
    }

    private View.OnClickListener createUserEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){

                    String email = signUpActivityEmail.getText().toString();
                    String password = signUpActivityPassword.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),
                                        R.string.user_creation_validation,
                                        Toast.LENGTH_LONG).show();

                                String id = firebaseAuth.getUid();

                                User user = new User(id,"EUR");
                                firebaseService.upsertUser(user);

                                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        R.string.user_creation_error,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        };
    }

    private boolean validate() {
        if(signUpActivityEmail.getText() == null || signUpActivityEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(signUpActivityEmail.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(),
                    R.string.email_address_error,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(signUpActivityPassword.getText() == null || signUpActivityPassword.getText().toString().trim().isEmpty() || signUpActivityPassword.length()<6){
            Toast.makeText(getApplicationContext(),
                    R.string.password_error,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private View.OnClickListener goBackEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        };
    }

}