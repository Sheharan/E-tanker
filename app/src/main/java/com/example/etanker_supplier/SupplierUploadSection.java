package com.example.etanker_supplier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import info.hoang8f.widget.FButton;

public class SupplierUploadSection extends AppCompatActivity {

    private FButton UploadButton;
    private TextView registrationText;
    private TextView photoText,citizenshipText;

    private static final int File_Picker=2;
    private static final int Photo_Picker=3;
    private static final int citizenship_Picker=4;
    private FirebaseAuth fAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference,storageReference1,docRef;
    private FloatingActionButton uploadPhoto,uploadCitizenship,uploadFiles;

    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fAuth= FirebaseAuth.getInstance();
        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference1=FirebaseStorage.getInstance().getReference();
        storageReference=firebaseStorage.getReference().child("Verification_details").child(UserId);


        storageReference.child("photo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               storageReference.child("citizenship").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        storageReference.child("registration").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                setContentView(R.layout.verification);
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SupplierUploadSection.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

        setContentView(R.layout.activity_supplier_upload_section);


//        fAuth= FirebaseAuth.getInstance();
//        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
//        firebaseStorage=FirebaseStorage.getInstance();
//        storageReference=firebaseStorage.getReference().child("Verification_details").child(UserId);

        Log.d("My Activity",UserId);
        registrationText=findViewById(R.id.uploadRegistration);
        photoText =findViewById(R.id.uploadPhoto);
        citizenshipText=findViewById(R.id.uploadCitizenship);
        UploadButton= findViewById(R.id.UploadButton);
        uploadPhoto=findViewById(R.id.photoUpload);
        uploadCitizenship=findViewById(R.id.citizenshipUpload);
        uploadFiles=findViewById(R.id.RegistrationUpload);

        registrationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("My Activty","upload1");
                // TODO: Intent to show an file picker
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("pdf/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),File_Picker);
            }
        });

        photoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("My Activty","upload1");
                // TODO: Intent to show an file picker
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),Photo_Picker);
            }
        });

        citizenshipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("My Activty","upload1");
                // TODO: Intent to show an file picker
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("pdf/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),citizenship_Picker);
            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupplierUploadSection.this, SupplierHome.class));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Photo_Picker && resultCode==RESULT_OK){
            Log.d("My Activity","First Phase");
            assert data != null;
            final Uri selectedFile=data.getData();

            final StorageReference photoRef=storageReference.child("photo");
            assert selectedFile != null;
            photoText.setText(selectedFile.getLastPathSegment());

            //uploading to the storage
            uploadPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoRef.putFile(selectedFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("My Activity","Second Phase");
                            Toast.makeText(SupplierUploadSection.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SupplierUploadSection.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else if(requestCode==citizenship_Picker && resultCode==RESULT_OK){
            assert data != null;
            final Uri selectedFile=data.getData();

            final StorageReference citizenshipRef=storageReference.child("citizenship");
            citizenshipText.setText(selectedFile.getLastPathSegment());

            //uploading to the storage
            uploadCitizenship.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    citizenshipRef.putFile(selectedFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("My Activity","Second Phase");
                            Toast.makeText(SupplierUploadSection.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SupplierUploadSection.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else if(requestCode==File_Picker && resultCode==RESULT_OK){
            assert data != null;
            final Uri selectedFile=data.getData();

            final StorageReference registrationRef=storageReference.child("registration");
            assert selectedFile != null;
            registrationText.setText(selectedFile.getLastPathSegment());

            //uploading to the storage
            uploadFiles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrationRef.putFile(selectedFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("My Activity","Second Phase");
                            Toast.makeText(SupplierUploadSection.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SupplierUploadSection.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

//    private void loadNewView(){
//        LinearLayout yourLinearLayout=new LinearLayout(this);
//        View.inflate(getApplicationContext(), R.layout.verification, yourLinearLayout);
//        Toast.makeText(this, "view", Toast.LENGTH_SHORT).show();
//    }

}