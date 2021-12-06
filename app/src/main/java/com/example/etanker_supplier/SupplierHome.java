package com.example.etanker_supplier;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etanker_supplier.Interface.ItemClickListener;
import com.example.etanker_supplier.Model.Tanker;
import com.example.etanker_supplier.Service.ListenOrder;
import com.example.etanker_supplier.Utils.Common;
import com.example.etanker_supplier.ViewHolder.TankerViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SupplierHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    DocumentReference supplierData;
    String UserId;
    Intent intent;
    StorageReference storageReference;

    RecyclerView supplierTankerList;
    FirestoreRecyclerAdapter<Tanker, TankerViewHolder> adapter;
    private int photo_picker=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_home);
        Toolbar toolbar = findViewById(R.id.Suppliertoolbar);
        setSupportActionBar(toolbar);

        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        storageReference= FirebaseStorage.getInstance().getReference();




        DrawerLayout drawer = findViewById(R.id.supplier_drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.supplierNavView);
        navigationView.setNavigationItemSelectedListener(this);

        UserId= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        supplierData=fstore.collection(Common.Suppliers).document(UserId);

        View headerView=navigationView.getHeaderView(0);
        final TextView SupplierName=headerView.findViewById(R.id.SupplierName);
        final TextView SupplierEmailAddress=headerView.findViewById(R.id.SupplierEmailAddress);
        final CircleImageView supplierImage=headerView.findViewById(R.id.profile_image);
        final ImageView editOption=headerView.findViewById(R.id.editInHeader);
        assert supplierData!=null;
        supplierData.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (e!=null){
                    Log.d("My Activity","Error:"+e.getMessage());
                }else{
                    SupplierName.setText(Objects.requireNonNull(documentSnapshot.get(Common.Name)).toString());
                    SupplierEmailAddress.setText(Objects.requireNonNull(documentSnapshot.get(Common.Email)).toString());
                    loadImage(supplierImage);
                }
            }


        });



        supplierImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog= new AlertDialog.Builder(SupplierHome.this);
                alertDialog.setTitle("Do you want to upload your profile picture?").
                        setIcon(R.drawable.ic_baseline_cloud_upload_24)
                        .setPositiveButton("upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("My Activty","upload1");
                                // TODO: Intent to show an file picker
                                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                                startActivityForResult(Intent.createChooser(intent,"Complete action using"),photo_picker);
                            }
                        }).setNegativeButton("dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });


        editOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SupplierHome.this, "success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SupplierHome.this, EditSupplierDetails.class));
            }
        });


        supplierTankerList=findViewById(R.id.SupplierTankerList);
        supplierTankerList.hasFixedSize();

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        supplierTankerList.setLayoutManager(layoutManager);

        loadMytanker();

        //Register the service
        Intent service=new Intent(SupplierHome.this, ListenOrder.class);
        startService(service);

    }


    private void loadImage(final ImageView supplierImage){

        storageReference.child("Verification_details").child(UserId).child("photo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(supplierImage);
            }
        });


    }

    private void loadMytanker() {
        Query query=fstore.collection(Common.Suppliers).document(UserId).collection("tankers");

        FirestoreRecyclerOptions<Tanker> options=new FirestoreRecyclerOptions.Builder<Tanker>()
                .setQuery(query,Tanker.class).build();

        adapter=new FirestoreRecyclerAdapter<Tanker, TankerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TankerViewHolder tankerViewHolder, int i, @NonNull final Tanker tanker) {
                tankerViewHolder.tankerNumber.setText(tanker.getTankerNumber());
                tankerViewHolder.tankerDriverName.setText(tanker.getDriverName());

                tankerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent tankerDetails=new Intent(SupplierHome.this,tankerDetailsSupplier.class);
                        tankerDetails.putExtra("tankerNumber",tanker.getTankerNumber());
                        startActivity(tankerDetails);
                    }
                });

                tankerViewHolder.editTanker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editTanker=new Intent(SupplierHome.this,editTankerDetails.class);
                        editTanker.putExtra("tankerNum",tanker.getTankerNumber());
                        startActivity(editTanker);
                    }
                });
            }

            @NonNull
            @Override
            public TankerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tanker_view,parent,false);
                return new TankerViewHolder(itemView);
            }
        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        supplierTankerList.setAdapter(adapter);

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.AddTanker){

            Intent addTanker=new Intent(this,tankerRegistration.class);
            startActivity(addTanker);

        }else if(item.getItemId()==R.id.OrderDetailsSupplier){
            Intent list=new Intent(this,TankerList.class);
            list.putExtra("activity_id",1001);
            startActivity(list);
        }else if (item.getItemId()==R.id.FeedbackSupplier){
            Intent list1=new Intent(this,TankerList.class);
            list1.putExtra("activity_id",1002);
            startActivity(list1);
        }else if (item.getItemId()==R.id.logoutSupplier){

            fAuth.signOut();
            Toast.makeText(this, "Successfully sign out", Toast.LENGTH_SHORT).show();
            Intent signIn=new Intent(SupplierHome.this,loginActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }else if(item.getItemId()==R.id.SupplierLocationUpdate){
            Intent updateLocation=new Intent(SupplierHome.this, Location.class);
            startActivity(updateLocation);
        }else if(item.getItemId()==R.id.uploadFile){
            checkFiles();
        }

        DrawerLayout drawerLayout=findViewById(R.id.supplier_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==photo_picker && resultCode==RESULT_OK){
            Log.d("My Activity","First Phase");
            assert data != null;
            final Uri selectedFile=data.getData();

            final StorageReference photoRef=storageReference.child("Verification_details").child(UserId).child("photo");
            assert selectedFile != null;


            //uploading to the storage

            photoRef.putFile(selectedFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("My Activity","Second Phase");
                    Toast.makeText(SupplierHome.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SupplierHome.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkFiles(){
        final Intent intent=new Intent(SupplierHome.this,SupplierUploadSection.class);
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference().child("Verification_details").child(UserId);

        storageReference.child("photo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Intent intent=new Intent(SupplierHome.this,SupplierUploadSection.class);
                storageReference.child("citizenship").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        storageReference.child("registration").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                startActivity(new Intent(SupplierHome.this,verified.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startActivity(intent);
                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                startActivity(intent);

            }
        });

    }
}
