package com.example.etanker_supplier;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etanker_supplier.Utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {
    TextView userRegister, forgotPassword;
    EditText loginEmail;
    EditText loginPassword;
    Button btnLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String UserID="";
    private ProgressDialog progressDialog;
    String TAG = "My Activity";

//    FirebaseDatabase database;
//    DatabaseReference tableUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRegister = (TextView) findViewById(R.id.registerUser);
        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_passwords);
        btnLogin = (Button) findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.forgot_Password);


//        database=FirebaseDatabase.getInstance();
//        tableUser=database.getReference(Common.User);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(loginActivity.this, "Please enter email and passsword...", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Processing...");
                progressDialog.show();
                login(email,password);

            }

        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setMessage("Enter email to reset the password");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString().trim();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(loginActivity.this, "a link has been sent to the email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginActivity.this, "error has occured", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.show();
            }
        });
    }


    private void login(final String email, final String password) {

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                    Log.d(TAG,UserID);

                    fstore.collection(Common.Suppliers).document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (Objects.requireNonNull(task.getResult()).exists()){
                                Toast.makeText(loginActivity.this, "Logged in ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this,SupplierHome.class));
                                finish();
                            }else {
                                Toast.makeText(loginActivity.this, "Provided email is not registered as Supplier", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });


                } else {
                    Toast.makeText(loginActivity.this, "Incorrect Password or email ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        });
    }


}