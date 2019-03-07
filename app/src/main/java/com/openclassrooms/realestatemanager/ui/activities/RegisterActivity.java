package com.openclassrooms.realestatemanager.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.realestatemanager.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText email;
    private EditText password;
    private EditText passwordHint;
    private FirebaseAuth auth;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        setViews();
        setListeners();
    }

    private void setViews() {
        registerBtn = findViewById(R.id.activity_register_register_button);
        email = findViewById(R.id.activity_register_email_edit_text);
        password = findViewById(R.id.activity_register_password_edit_text);
        passwordHint = findViewById(R.id.activity_register_password_hint_edit_text);
    }

    private void setListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String passwordHintString = passwordHint.getText().toString();

                if (emailString == null
                        || emailString.isEmpty()
                        || passwordString == null
                        || passwordString.isEmpty()
                        || passwordHintString == null
                        || passwordHintString.isEmpty()
                ) {
                    displayToast("All fields must be answer, please try again");
                } else {
                    registerUser(emailString, passwordString, passwordHintString);
                }
            }
        });
    }

    private void displayToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void registerUser(final String email, String password, final String passwordHintString) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e);
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            displayToast("Register succeed");
                            savePasswordHint(email, passwordHintString);
                            //  saveUserInfoInFirebase();
                        } else {
                            // If sign in fails, display a message to the user.
                            displayToast("Register failed, please try again");
                        }
                    }
                });
    }

    private void savePasswordHint(String email, String passwordHintString) {
        Map<String, Object> passwordHintMap = new HashMap<>();
        passwordHintMap.put("passwordHint", passwordHintString);

        FirebaseFirestore.getInstance().collection("Users")
                .document(email)
                .collection("Password Hint")
                .add(passwordHintMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error message NOT sent", Toast.LENGTH_SHORT).show();
                    }
                });;
    }

    private class PasswordHintClass {
        private String hint;

        public PasswordHintClass(String hint) {
            this.hint = hint;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }
    }

}
