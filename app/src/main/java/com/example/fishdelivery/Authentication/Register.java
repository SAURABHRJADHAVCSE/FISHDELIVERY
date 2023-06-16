package com.example.fishdelivery.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fishdelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputLayout fullName, username, email, phoneNo, password;
    EditText adminPasscode;
    Button gotoLogin, registerBtn;

    boolean valid = true;

    String correctPasscode = "ADMIN@3663";

    // FIREBASE

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    CheckBox isAdmin, isCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.registerName);
        username = findViewById(R.id.registerUsername);
        email = findViewById(R.id.registerEmail);
        phoneNo = findViewById(R.id.registerPhoneNo);
        password = findViewById(R.id.registerPassword);
        isAdmin = findViewById(R.id.checkboxAdmin);
        isCustomer = findViewById(R.id.checkboxCustomer);
        registerBtn = findViewById(R.id.registerBtn);
        gotoLogin = findViewById(R.id.logIn);
        adminPasscode = findViewById(R.id.adminPasscode);

        // FIREBASE

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // ONCLICK LISTENERS

        isAdmin.setEnabled(false);

        adminPasscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredPasscode = adminPasscode.getText().toString();
                if (enteredPasscode.equals(correctPasscode)) {
                    // Enable the checkbox
                    isAdmin.setEnabled(true);
                } else {
                    // Disable the checkbox
                    isAdmin.setEnabled(false);
                }
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkFieldFullName(fullName);
                checkFieldUsername(username);
                checkFieldEmail(email);
                checkFieldPhoneNo(phoneNo);
                checkFieldPassword(password);

                if (!(isAdmin.isChecked() || isCustomer.isChecked())) {
                    Toast.makeText(Register.this, "Select The Account Type", Toast.LENGTH_SHORT).show();
                }


                if (valid) {
                    fAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = fAuth.getCurrentUser();
                            boolean res = (verify_EmailId());
                            if (res == true) {
                                Toast.makeText(Register.this, "Enter Details Correctly", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            }

                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();


                            userInfo.put("FullName", fullName.getEditText().getText().toString().trim());
                            userInfo.put("UserName", username.getEditText().getText().toString().trim());
                            userInfo.put("UserEmail", email.getEditText().getText().toString().trim());
                            userInfo.put("PhoneNumber", phoneNo.getEditText().getText().toString().trim());
                            userInfo.put("Password", password.getEditText().getText().toString().trim());


                            if (isAdmin.isChecked()) {
                                userInfo.put("isAdmin", "1");
                            }

                            if (isCustomer.isChecked()) {
                                res = (verify_EmailId());
                                if (res == true)
                                    return;
                                userInfo.put("isCustomer", "1");
                            }

                            df.set(userInfo);

                            startActivity(new Intent(Register.this, Login.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed To Create Account", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        isCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isAdmin.setChecked(false);
                }
            }
        });

        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isCustomer.setChecked(false);
                }
            }
        });

    }


    // FIELD VALIDATION
    public boolean checkFieldFullName(TextInputLayout textField) {
        if (textField.getEditText().getText().toString().isEmpty()) {
            textField.setError("Please Enter Your Full Name!");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    public boolean checkFieldUsername(TextInputLayout textField) {
        if (textField.getEditText().getText().toString().isEmpty()) {
            textField.setError("Username Required!");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    public boolean checkFieldEmail(TextInputLayout textField) {
        if (textField.getEditText().getText().toString().isEmpty()) {
            textField.setError("Email Required!");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    public boolean checkFieldPhoneNo(TextInputLayout textField) {
        if (textField.getEditText().getText().toString().isEmpty()) {
            textField.setError("PhoneNo Required!");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    public boolean checkFieldPassword(TextInputLayout textField) {
        if (textField.getEditText().getText().toString().isEmpty()) {
            textField.setError("Password Required!");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    private boolean verify_EmailId() {
        String emailId = email.getEditText().getText().toString().trim();
        if (!emailId.endsWith("@gmail.com")) {
            email.setEnabled(true);
            email.setError("Enter Your E-Mail Id");
            return true;
        } else {
            email.setErrorEnabled(false);
            return false;
        }
    }
}